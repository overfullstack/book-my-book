package com.gakshintala.bookmybook.core.usecases.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.ports.UseCase;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindAvailableBook;
import com.gakshintala.bookmybook.core.ports.repositories.library.HandlePatronEventInLibrary;
import com.gakshintala.bookmybook.core.ports.repositories.patron.FindPatron;
import com.gakshintala.bookmybook.core.ports.repositories.patron.HandlePatronEvent;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PatronPlaceBookOnHold implements UseCase<PatronPlaceBookOnHold.PlaceOnHoldCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>>> {
    private final FindAvailableBook findAvailableBook;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> execute(@NonNull PlaceOnHoldCommand command) {
        return findAvailableBook.findAvailableBook(command.getCatalogueBookInstanceUUID())
                .map(availableBook -> findPatron.findBy(command.getPatronId())
                        .map(patron -> patron.canPatronPlaceOnHold(availableBook, command.getHoldDuration()))
                        .map(this::handleResult)
                        .map(Try::get)
                        .get());
    }

    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> handleResult(Either<BookHoldFailed, BookPlacedOnHold> result) {
        return result
                .map(bookPlacedOnHold -> handlePatronEvent.handle(bookPlacedOnHold)
                        .map(patron -> handlePatronEventInLibrary.handle(bookPlacedOnHold)
                                .map(catalogueBookInstanceUUID -> Tuple.of((PatronEvent) bookPlacedOnHold, patron, catalogueBookInstanceUUID)))
                        .get())
                .getOrElseGet(bookHoldFailed -> Try.success(Tuple.of(bookHoldFailed, null, null)));
    }

    @Value
    public static class PlaceOnHoldCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull CatalogueBookInstanceUUID catalogueBookInstanceUUID;
        @NonNull HoldDuration holdDuration;

        public static PlaceOnHoldCommand closeEnded(PatronId patronId, CatalogueBookInstanceUUID bookId, int days) {
            return new PlaceOnHoldCommand(Instant.now(), patronId, bookId, HoldDuration.forNoOfDays(days));
        }

        public static PlaceOnHoldCommand openEnded(PatronId patronId, CatalogueBookInstanceUUID bookId) {
            return new PlaceOnHoldCommand(Instant.now(), patronId, bookId, HoldDuration.openEnded());
        }
    }
}
