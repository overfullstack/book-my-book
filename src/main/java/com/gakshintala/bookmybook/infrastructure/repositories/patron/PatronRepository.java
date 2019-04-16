package com.gakshintala.bookmybook.infrastructure.repositories.patron;


import com.gakshintala.bookmybook.adapters.db.PatronDomainModelMapper;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldExpired;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldEvents;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookReturned;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.OverdueCheckoutRegistered;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.ports.repositories.patron.FindPatron;
import com.gakshintala.bookmybook.core.ports.repositories.patron.HandlePatronEvent;
import com.gakshintala.bookmybook.core.ports.repositories.patron.PersistPatron;
import io.vavr.API;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

interface PatronEntityRepository extends CrudRepository<PatronDatabaseEntity, Long> {

    @Query("SELECT p.* FROM patron_database_entity p where p.patron_id = :patronId")
    PatronDatabaseEntity findByPatronId(@Param("patronId") UUID patronId);

}

@Repository
@RequiredArgsConstructor
class PatronRepository implements FindPatron, PersistPatron, HandlePatronEvent {
    private final PatronEntityRepository patronEntityRepository;
    
    private Function2<PatronDatabaseEntity, BookPlacedOnHold, PatronDatabaseEntity> handleBookPlacedOnHold = (entity, event) -> {
        Set<HoldDatabaseEntity> booksOnHold = entity.booksOnHold;
        booksOnHold.add(new HoldDatabaseEntity(event.getBookId(), event.getPatronId(), event.getLibraryBranchId(), event.getHoldTill()));
        return entity.withBooksOnHold(booksOnHold);
    };
    private Function2<PatronDatabaseEntity, BookPlacedOnHoldEvents, PatronDatabaseEntity> handleBookPlacedOnHoldEvents = (entity, placedOnHoldEvents) -> {
        BookPlacedOnHold event = placedOnHoldEvents.getBookPlacedOnHold();
        return handleBookPlacedOnHold.apply(entity, event);
    };
    private Function2<PatronDatabaseEntity, BookCollected, PatronDatabaseEntity> handleBookCollected =
            (entity, event) -> removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);
    private Function2<PatronDatabaseEntity, BookHoldCanceled, PatronDatabaseEntity> handleBookHoldCanceled =
            (entity, event) -> removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);
    private Function2<PatronDatabaseEntity, BookHoldExpired, PatronDatabaseEntity> handleBookHoldExpired =
            (entity, event) -> removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);
    private Function2<PatronDatabaseEntity, OverdueCheckoutRegistered, PatronDatabaseEntity> handleOverdueCheckoutRegistered = (entity, event) -> {
        Set<OverdueCheckoutDatabaseEntity> checkouts = entity.checkouts;
        checkouts.add(new OverdueCheckoutDatabaseEntity(event.getBookId(), event.getPatronId(), event.getLibraryBranchId()));
        return entity.withCheckouts(checkouts);
    };
    private Function2<PatronDatabaseEntity, BookReturned, PatronDatabaseEntity> handleBookReturned =
            (entity, event) -> removeOverdueCheckoutIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);
    private Function2<PatronEvent, PatronDatabaseEntity, PatronDatabaseEntity> handle =
            (event, entity) -> API.Match(event).of(
                    Case($(instanceOf(BookPlacedOnHoldEvents.class)), handleBookPlacedOnHoldEvents.curried().apply(entity)),
                    Case($(instanceOf(BookPlacedOnHold.class)), handleBookPlacedOnHold.curried().apply(entity)),
                    Case($(instanceOf(BookCollected.class)), handleBookCollected.curried().apply(entity)),
                    Case($(instanceOf(BookHoldCanceled.class)), handleBookHoldCanceled.curried().apply(entity)),
                    Case($(instanceOf(BookHoldExpired.class)), handleBookHoldExpired.curried().apply(entity)),
                    Case($(instanceOf(OverdueCheckoutRegistered.class)), handleOverdueCheckoutRegistered.curried().apply(entity)),
                    Case($(instanceOf(BookReturned.class)), handleBookReturned.curried().apply(entity))
            );

    @Override
    public Try<Patron> findBy(PatronId patronId) {
        return Try.of(() -> 
                Option.of(patronEntityRepository.findByPatronId(patronId.getPatronId()))
                .map(PatronDomainModelMapper::toDomainModel)
                .getOrElseThrow(() -> new IllegalArgumentException("No Patron found with Id: " + patronId))
        );
    }

    @Override
    public Try<Patron> persist(PatronInformation patronInformation) {
        return Try.of(() -> patronEntityRepository
                .save(new PatronDatabaseEntity(patronInformation.getPatronId(), patronInformation.getType())))
                .map(PatronDomainModelMapper::toDomainModel);
    }

    @Override
    public Try<Patron> handle(PatronEvent domainEvent) {
        return Try.of(() -> patronEntityRepository.findByPatronId(domainEvent.patronId().getPatronId()))
                .map(handle.curried().apply(domainEvent))
                .map(patronEntityRepository::save)
                .map(PatronDomainModelMapper::toDomainModel);
    }

    private PatronDatabaseEntity removeHoldIfPresent(UUID patronId, UUID bookId, UUID libraryBranchId, PatronDatabaseEntity entity) {
        final Set<HoldDatabaseEntity> nonPatronBookHolds = entity.booksOnHold.stream()
                .filter(holdDatabaseEntity -> !holdDatabaseEntity.is(patronId, bookId, libraryBranchId))
                .collect(Collectors.toSet());
        return entity.withBooksOnHold(nonPatronBookHolds);
    }

    private PatronDatabaseEntity removeOverdueCheckoutIfPresent(UUID patronId, UUID bookId, UUID libraryBranchId, PatronDatabaseEntity entity) {
        final Set<OverdueCheckoutDatabaseEntity> nonPatronOverdueCheckouts = entity.checkouts.stream()
                .filter(overdueCheckoutDatabaseEntity -> overdueCheckoutDatabaseEntity.is(patronId, bookId, libraryBranchId))
                .collect(Collectors.toSet());
        return entity.withCheckouts(nonPatronOverdueCheckouts);
    }

}

