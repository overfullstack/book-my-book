package com.gakshintala.bookmybook.core.usecases.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldEvents;
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

import static com.gakshintala.bookmybook.core.domain.common.EitherResult.failure;
import static com.gakshintala.bookmybook.core.domain.common.EitherResult.success;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed.bookHoldFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold.bookPlacedOnHoldNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldEvents.events;
import static com.gakshintala.bookmybook.core.domain.patron.PatronHolds.MAX_NUMBER_OF_HOLDS;

@Service
@RequiredArgsConstructor
public class PatronPlaceBookOnHold implements UseCase<PatronPlaceBookOnHold.PlaceOnHoldCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>>> {
    private final FindAvailableBook findAvailableBook;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> execute(@NonNull PlaceOnHoldCommand command) {
        return Try.of(() -> {
            AvailableBook availableBook = find(command.getCatalogueBookInstanceUUID());
            Patron patron = find(command.getPatronId());
            Either<BookHoldFailed, BookPlacedOnHoldEvents> result = placeOnHold(patron, availableBook, command.getHoldDuration());
            return result.isRight() 
                    ? Tuple.of(result.get(), handlePatronEvent.handle(result.get()), handlePatronEventInLibrary.handle(result.get()))
                    : Tuple.of(result.getLeft(), null, null);
        });
    }
    
    private AvailableBook find(CatalogueBookInstanceUUID id) {
        return findAvailableBook
                .findBy(id)
                .getOrElseThrow(() -> new IllegalArgumentException("Cannot find available book with Id: " + id.getBookInstanceUUID()));
    }

    private Patron find(PatronId patronId) {
        return findPatron
                .findBy(patronId)
                .getOrElseThrow(() -> new IllegalArgumentException("Patron with given Id does not exists: " + patronId.getPatronId()));
    }

    private Either<BookHoldFailed, BookPlacedOnHoldEvents> placeOnHold(Patron patron, AvailableBook aBook, HoldDuration duration) {
        Option<Rejection> rejection = patronCanHold(patron, aBook, duration);
        if (rejection.isEmpty()) {
            PatronEvent.BookPlacedOnHold bookPlacedOnHold = bookPlacedOnHoldNow(aBook.getBookInstanceId(), aBook.type(), 
                    aBook.getLibraryBranchId(), patron.getPatronInformation().getPatronId(), duration);
            if (patron.getPatronHolds().maximumHoldsAfterHolding()) {
                return success(events(bookPlacedOnHold, PatronEvent.MaximumNumberOhHoldsReached.now(patron.getPatronInformation(), MAX_NUMBER_OF_HOLDS)));
            }
            return success(events(bookPlacedOnHold));
        }
        return failure(bookHoldFailedNow(rejection.get(), aBook.getBookInstanceId(), aBook.getLibraryBranchId(), patron.getPatronInformation()));
    }

    private Option<Rejection> patronCanHold(Patron patron, AvailableBook aBook, HoldDuration forDuration) {
        return patron.getPlacingOnHoldPolicies()
                .toStream()
                .map(policy -> policy.apply(aBook, patron, forDuration))
                .find(Either::isLeft)
                .map(Either::getLeft);
    }

    @Value
    public static class PlaceOnHoldCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull LibraryBranchId libraryBranchId;
        @NonNull CatalogueBookInstanceUUID catalogueBookInstanceUUID;
        @NonNull HoldDuration holdDuration;
    }
    
}
