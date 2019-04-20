package com.gakshintala.bookmybook.core.usecases.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldOnce;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.MaximumNumberOfHoldsReached;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.Rejection;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindAvailableBook;
import com.gakshintala.bookmybook.core.ports.repositories.library.HandlePatronEventInLibrary;
import com.gakshintala.bookmybook.core.ports.repositories.patron.FindPatron;
import com.gakshintala.bookmybook.core.ports.repositories.patron.HandlePatronEvent;
import com.gakshintala.bookmybook.core.usecases.UseCase;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed.bookHoldFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldOnce.bookPlacedOnHoldNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold.events;
import static com.gakshintala.bookmybook.core.domain.patron.PatronHolds.MAX_NUMBER_OF_HOLDS;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

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
                        .map(patron -> placeOnHold(patron, availableBook, command.getHoldDuration()))
                        .map(this::handleResult)
                        .map(Try::get)
                        .get());
    }

    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> handleResult(Either<BookHoldFailed, BookPlacedOnHold> result) {
        return result.map(bookPlacedOnHold -> Try.of(() ->
                Tuple.of((PatronEvent) bookPlacedOnHold,
                        handlePatronEvent.handle(bookPlacedOnHold).get(),
                        handlePatronEventInLibrary.handle(bookPlacedOnHold).get())))
                .getOrElseGet(bookHoldFailed -> Try.success(Tuple.of(bookHoldFailed, null, null)));
    }

    public Either<BookHoldFailed, BookPlacedOnHold> placeOnHold(Patron patron, AvailableBook aBook, HoldDuration duration) {
        return patronCanHold(patron, aBook, duration)
                .map(rejection -> getBookHoldFailed(patron, aBook, rejection))
                .getOrElse(() -> getBookPlacedOnHoldEvents(patron, aBook, duration));
    }

    private Either<BookHoldFailed, BookPlacedOnHold> getBookHoldFailed(Patron patron, AvailableBook aBook, Rejection rejection) {
        return left(bookHoldFailedNow(rejection, aBook.getBookInstanceId(), aBook.getLibraryBranchId().getLibraryBranchUUID(),
                patron.getPatronInformation().getPatronId()));
    }

    private Either<BookHoldFailed, BookPlacedOnHold> getBookPlacedOnHoldEvents(Patron patron, AvailableBook aBook, HoldDuration duration) {
        return patron.getPatronHolds().maximumHoldsAfterHolding()
                ? right(events(getBookPlacedOnHold(patron, aBook, duration), MaximumNumberOfHoldsReached.now(patron.getPatronInformation(), MAX_NUMBER_OF_HOLDS)))
                : right(events(getBookPlacedOnHold(patron, aBook, duration)));
    }

    private static BookPlacedOnHoldOnce getBookPlacedOnHold(Patron patron, AvailableBook aBook, HoldDuration duration) {
        return bookPlacedOnHoldNow(aBook.getBookInstanceId(), aBook.type(),
                aBook.getLibraryBranchId(), patron.getPatronInformation().getPatronId(), duration);
    }

    private Option<Rejection> patronCanHold(Patron patron, AvailableBook availableBook, HoldDuration forDuration) {
        return patron.getPlacingOnHoldPolicies()
                .toStream()
                .map(policy -> policy.apply(availableBook, patron, forDuration))
                .find(Either::isLeft)
                .map(Either::getLeft);
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
