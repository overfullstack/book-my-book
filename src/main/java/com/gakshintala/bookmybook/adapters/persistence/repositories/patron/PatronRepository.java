package com.gakshintala.bookmybook.adapters.persistence.repositories.patron;


import com.gakshintala.bookmybook.adapters.persistence.mappers.PatronDomainModelMapper;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldExpired;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldNow;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.ports.persistence.patron.FindPatron;
import com.gakshintala.bookmybook.core.ports.persistence.patron.HandlePatronEvent;
import com.gakshintala.bookmybook.core.ports.persistence.patron.PersistPatron;
import io.vavr.API;
import io.vavr.Function2;
import io.vavr.collection.Set;
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

@Repository
@RequiredArgsConstructor
public class PatronRepository implements FindPatron, PersistPatron, HandlePatronEvent {
    private final PatronEntityRepository patronEntityRepository;

    private final Function2<PatronDatabaseEntity, BookPlacedOnHoldNow, PatronDatabaseEntity> handleBookPlacedOnHoldNow =
            (entity, event) -> entity.withBooksOnHold(Stream.ofAll(entity.getBooksOnHold())
                    .append(new HoldDatabaseEntity(event.getBookId(), event.getPatronId(), event.getLibraryBranchId(), event.getHoldTill()))
                    .toJavaSet());

    private final Function2<PatronDatabaseEntity, BookPlacedOnHold, PatronDatabaseEntity> handleBookPlacedOnHold =
            (entity, placedOnHold) -> handleBookPlacedOnHoldNow.apply(entity, placedOnHold.getBookPlacedOnHoldNow());

    private final Function2<PatronDatabaseEntity, BookCollected, PatronDatabaseEntity> handleBookCollected =
            (entity, event) -> removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);
    private final Function2<PatronDatabaseEntity, BookHoldCanceled, PatronDatabaseEntity> handleBookHoldCanceled =
            (entity, event) -> removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);
    private final Function2<PatronDatabaseEntity, BookHoldExpired, PatronDatabaseEntity> handleBookHoldExpired =
            (entity, event) -> removeHoldIfPresent(event.getPatronId(), event.getBookId(), event.getLibraryBranchId(), entity);

    private final Function2<PatronEvent, PatronDatabaseEntity, PatronDatabaseEntity> handle =
            (event, entity) -> API.Match(event).of(
                    Case($(instanceOf(BookPlacedOnHold.class)), handleBookPlacedOnHold.curried().apply(entity)),
                    Case($(instanceOf(BookPlacedOnHoldNow.class)), handleBookPlacedOnHoldNow.curried().apply(entity)),
                    Case($(instanceOf(BookCollected.class)), handleBookCollected.curried().apply(entity)),
                    Case($(instanceOf(BookHoldCanceled.class)), handleBookHoldCanceled.curried().apply(entity)),
                    Case($(instanceOf(BookHoldExpired.class)), handleBookHoldExpired.curried().apply(entity))
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
        final Set<HoldDatabaseEntity> nonPatronBookHolds = Stream.ofAll(entity.getBooksOnHold())
                .filter(holdDatabaseEntity -> !holdDatabaseEntity.is(patronId, bookId, libraryBranchId))
                .toSet();
        return entity.withBooksOnHold(nonPatronBookHolds.toJavaSet());
    }

}

interface PatronEntityRepository extends CrudRepository<PatronDatabaseEntity, Long> {

    @Query("SELECT p.* FROM patron_database_entity p where p.patron_id = :patronId")
    PatronDatabaseEntity findByPatronId(@Param("patronId") UUID patronId);

}

