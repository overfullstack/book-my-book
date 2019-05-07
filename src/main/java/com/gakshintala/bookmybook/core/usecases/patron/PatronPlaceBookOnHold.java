package com.gakshintala.bookmybook.core.usecases.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
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
public class PatronPlaceBookOnHold implements UseCase<PatronPlaceBookOnHold.PlaceOnHoldCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>>> {
    private final FindAvailableBook findAvailableBook;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> execute(@NonNull PlaceOnHoldCommand command) {
        return findAvailableBook.findAvailableBook(command.getCatalogueBookInstanceId())
                .map(availableBook -> findPatron.findBy(command.getPatronId())
                        .map(patron -> patron.canPatronPlaceOnHold(availableBook, command.getHoldDuration()))
                        .map(this::handleResult)
                        .map(Try::get)
                        .get());
    }

    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> handleResult(Either<BookHoldFailed, BookPlacedOnHold> result) {
        return result
                .map(bookPlacedOnHold -> handlePatronEvent.handle(bookPlacedOnHold)
                        .map(patron -> handlePatronEventInLibrary.handle(bookPlacedOnHold)
                                .map(catalogueBookInstanceId -> Tuple.of((PatronEvent) bookPlacedOnHold, patron, catalogueBookInstanceId)))
                        .get())
                .getOrElseGet(bookHoldFailed -> Try.success(Tuple.of(bookHoldFailed, null, null)));
    }

    @Value
    public static class PlaceOnHoldCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull CatalogueBookInstanceId catalogueBookInstanceId;
        @NonNull HoldDuration holdDuration;

        public static PlaceOnHoldCommand closeEnded(PatronId patronId, CatalogueBookInstanceId bookId, int days) {
            return new PlaceOnHoldCommand(Instant.now(), patronId, bookId, HoldDuration.forNoOfDays(days));
        }

        public static PlaceOnHoldCommand openEnded(PatronId patronId, CatalogueBookInstanceId bookId) {
            return new PlaceOnHoldCommand(Instant.now(), patronId, bookId, HoldDuration.openEnded());
        }
    }
}
