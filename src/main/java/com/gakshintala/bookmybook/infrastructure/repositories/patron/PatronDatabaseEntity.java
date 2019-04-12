package com.gakshintala.bookmybook.infrastructure.repositories.patron;


import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldEvents;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import io.vavr.API;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

@Data
@NoArgsConstructor
public class PatronDatabaseEntity {

    @Id
    Long id;
    UUID patronId;
    PatronType patronType;
    Set<HoldDatabaseEntity> booksOnHold;
    Set<OverdueCheckoutDatabaseEntity> checkouts;

     PatronDatabaseEntity(PatronId patronId, PatronType patronType) {
        this.patronId = patronId.getPatronId();
        this.patronType = patronType;
        this.booksOnHold = new HashSet<>();
        this.checkouts = new HashSet<>();
    }

    PatronDatabaseEntity handle(PatronEvent event) {
        return API.Match(event).of(
                Case($(instanceOf(BookPlacedOnHoldEvents.class)), this::handle),
                Case($(instanceOf(PatronEvent.BookPlacedOnHold.class)), this::handle),
                Case($(instanceOf(PatronEvent.BookCollected.class)), this::handle),
                Case($(instanceOf(PatronEvent.BookHoldCanceled.class)), this::handle),
                Case($(instanceOf(PatronEvent.BookHoldExpired.class)), this::handle),
                Case($(instanceOf(PatronEvent.OverdueCheckoutRegistered.class)), this::handle),
                Case($(instanceOf(PatronEvent.BookReturned.class)), this::handle)

        );
    }

    private PatronDatabaseEntity handle(BookPlacedOnHoldEvents placedOnHoldEvents) {
        PatronEvent.BookPlacedOnHold event = placedOnHoldEvents.getBookPlacedOnHold();
        return handle(event);
    }

    private PatronDatabaseEntity handle(PatronEvent.BookPlacedOnHold event) {
        booksOnHold.add(new HoldDatabaseEntity(event.getBookId(), event.getPatronId(), event.getLibraryBranchId(), event.getHoldTill()));
        return this;
    }

    private PatronDatabaseEntity handle(PatronEvent.BookHoldCanceled event) {
        return removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId());
    }


    private PatronDatabaseEntity handle(PatronEvent.BookCollected event) {
        return removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId());
    }

    private PatronDatabaseEntity handle(PatronEvent.BookHoldExpired event) {
        return removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId());
    }

    private PatronDatabaseEntity handle(PatronEvent.OverdueCheckoutRegistered event) {
        checkouts.add(new OverdueCheckoutDatabaseEntity(event.getBookId(), event.getPatronId(), event.getLibraryBranchId()));
        return this;
    }

    private PatronDatabaseEntity handle(PatronEvent.BookReturned event) {
        return removeOverdueCheckoutIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId());
    }

    private PatronDatabaseEntity removeHoldIfPresent(UUID patronId, UUID bookId, UUID libraryBranchId) {
        booksOnHold
                .stream()
                .filter(entity -> entity.is(patronId, bookId, libraryBranchId))
                .findAny()
                .ifPresent(entity -> booksOnHold.remove(entity));
        return this;
    }

    private PatronDatabaseEntity removeOverdueCheckoutIfPresent(UUID patronId, UUID bookId, UUID libraryBranchId) {
        checkouts
                .stream()
                .filter(entity -> entity.is(patronId, bookId, libraryBranchId))
                .findAny()
                .ifPresent(entity -> checkouts.remove(entity));
        return this;
    }

}


