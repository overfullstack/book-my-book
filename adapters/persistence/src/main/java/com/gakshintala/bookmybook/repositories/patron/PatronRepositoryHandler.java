package com.gakshintala.bookmybook.repositories.patron;


import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldExpired;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHoldNow;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.mappers.PatronDomainModelMapper;
import com.gakshintala.bookmybook.ports.persistence.patron.FindPatron;
import com.gakshintala.bookmybook.ports.persistence.patron.PatronEventHandler;
import com.gakshintala.bookmybook.ports.persistence.patron.PersistPatron;
import io.vavr.API;
import io.vavr.Function2;
import io.vavr.Function4;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

interface PatronEntityRepository extends CrudRepository<PatronEntity, Long> {

    @Query("SELECT p.* FROM patron_entity p where p.patron_id = :patronId")
    PatronEntity findByPatronId(@Param("patronId") UUID patronId);

}

/**
 * This repository is implemented using Spring Data JDBC.
 */
@Repository
@RequiredArgsConstructor
public class PatronRepositoryHandler implements FindPatron, PersistPatron, PatronEventHandler {

    private static Function4<UUID, UUID, UUID, PatronEntity, PatronEntity> removeHoldIfPresent = (patronId, bookId, libraryBranchId, patronEntity) ->
            patronEntity.withBooksOnHold(Stream.ofAll(patronEntity.getBooksOnHold())
                    .filter(holdEntity -> !holdEntity.is(patronId, bookId, libraryBranchId)).toJavaSet());
    private static final Function2<PatronEntity, BookCollected, PatronEntity> handleBookCollected =
            (patronEntity, bookCollected) -> removeHoldIfPresent.apply(bookCollected.getPatronId(), bookCollected.getBookId(), bookCollected.getLibraryBranchId(), patronEntity);

    private static final Function2<PatronEntity, BookHoldCanceled, PatronEntity> handleBookHoldCanceled =
            (patronEntity, bookHoldCanceled) -> removeHoldIfPresent.apply(bookHoldCanceled.getPatronId(), bookHoldCanceled.getBookId(), bookHoldCanceled.getLibraryBranchId(), patronEntity);

    private static final Function2<PatronEntity, BookHoldExpired, PatronEntity> handleBookHoldExpired =
            (patronEntity, bookHoldExpired) -> removeHoldIfPresent.apply(bookHoldExpired.getPatronId(), bookHoldExpired.getBookId(), bookHoldExpired.getLibraryBranchId(), patronEntity);
    
    /**
     * Appending new hold to current holds.
     */
    private static final Function2<PatronEntity, BookPlacedOnHoldNow, PatronEntity> handleBookPlacedOnHoldNow = (patronEntity, bookPlacedOnHoldNow) ->
            patronEntity.withBooksOnHold(Stream.ofAll(patronEntity.getBooksOnHold())
                    .append(new HoldEntity(bookPlacedOnHoldNow.getBookId(), bookPlacedOnHoldNow.getPatronId(), bookPlacedOnHoldNow.getLibraryBranchId(), bookPlacedOnHoldNow.getHoldTill()))
                    .toJavaSet());
    private static final Function2<PatronEntity, BookPlacedOnHold, PatronEntity> handleBookPlacedOnHold =
            (patronEntity, bookPlacedOnHold) -> handleBookPlacedOnHoldNow.apply(patronEntity, bookPlacedOnHold.getBookPlacedOnHoldNow());
    /**
     * Functions over Inheritance.
     */
    private static final Function2<PatronEvent, PatronEntity, PatronEntity> handle =
            (patronEvent, patronEntity) -> API.Match(patronEvent).of(
                    Case($(instanceOf(BookPlacedOnHold.class)), handleBookPlacedOnHold.curried().apply(patronEntity)),
                    Case($(instanceOf(BookPlacedOnHoldNow.class)), handleBookPlacedOnHoldNow.curried().apply(patronEntity)),
                    Case($(instanceOf(BookCollected.class)), handleBookCollected.curried().apply(patronEntity)),
                    Case($(instanceOf(BookHoldCanceled.class)), handleBookHoldCanceled.curried().apply(patronEntity)),
                    Case($(instanceOf(BookHoldExpired.class)), handleBookHoldExpired.curried().apply(patronEntity))
            );

    private final PatronEntityRepository patronEntityRepository;

    @Override
    public Try<Patron> findBy(PatronId patronId) {
        return Try.of(() -> Option.of(patronEntityRepository.findByPatronId(patronId.getPatronId()))
                .map(PatronDomainModelMapper::toDomainModel)
                .getOrElseThrow(() -> new IllegalArgumentException("No Patron found with Id: " + patronId))
        );
    }

    @Override
    public Try<Patron> persist(PatronInformation patronInformation) {
        return Try.of(() -> patronEntityRepository
                .save(new PatronEntity(patronInformation.getPatronId(), patronInformation.getType())))
                .map(PatronDomainModelMapper::toDomainModel);
    }

    @Override
    public Try<Patron> handle(PatronEvent patronEvent) {
        return Try.of(() -> patronEntityRepository.findByPatronId(patronEvent.patronId().getPatronId()))
                .map(handle.curried().apply(patronEvent))
                .map(patronEntityRepository::save)
                .map(PatronDomainModelMapper::toDomainModel);
    }

}

