package com.gakshintala.bookmybook.core.usecases.patron;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.CheckoutDuration;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollectingFailed;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindBookOnHold;
import com.gakshintala.bookmybook.core.ports.repositories.library.HandlePatronEventInLibrary;
import com.gakshintala.bookmybook.core.ports.repositories.patron.FindPatron;
import com.gakshintala.bookmybook.core.ports.repositories.patron.HandlePatronEvent;
import com.gakshintala.bookmybook.core.usecases.UseCase;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gakshintala.bookmybook.core.domain.common.EitherResult.failure;
import static com.gakshintala.bookmybook.core.domain.common.EitherResult.success;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected.bookCollectedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollectingFailed.bookCollectingFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.Rejection.withReason;

@Service
@AllArgsConstructor
public class PatronCollectBookOnHold implements UseCase<PatronCollectBookOnHold.CollectBookCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>>> {
    private final FindBookOnHold findBookOnHold;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;
    
    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> execute(@NonNull CollectBookCommand command) {
        return Try.of(() -> {
            BookOnHold bookOnHold = find(command.getCatalogueBookInstanceUUID(), command.getPatronId());
            Patron patron = find(command.getPatronId());
            Either<BookCollectingFailed, BookCollected> result = collect(patron, bookOnHold, command.getCheckoutDuration());
            return result.isRight()
                    ? Tuple.of(result.get(), handlePatronEvent.handle(result.get()), handlePatronEventInLibrary.handle(result.get()))
                    : Tuple.of(result.getLeft(), null, null);
        });
    }

    private BookOnHold find(CatalogueBookInstanceUUID id, PatronId patronId) {
        return findBookOnHold.findBy(id, patronId)
                .getOrElseThrow(() -> new IllegalArgumentException("Cannot find book on hold with Id: " + id.getBookInstanceUUID()));
    }

    private Patron find(PatronId patronId) {
        return findPatron
                .findBy(patronId)
                .getOrElseThrow(() -> new IllegalArgumentException("Patron with given Id does not exists: " + patronId.getPatronId()));
    }

    private Either<BookCollectingFailed, BookCollected> collect(Patron patron, BookOnHold book, CheckoutDuration duration) {
        if (patron.getPatronHolds().a(book)) {
            return success(bookCollectedNow(book.getBookId(), book.type(), book.getHoldPlacedAt(), 
                    patron.getPatronInformation().getPatronId(), duration));
        }
        return failure(bookCollectingFailedNow(withReason("library is not on hold by patronInformation"), 
                book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation()));
    }

    @Value
    public static class CollectBookCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull LibraryBranchId libraryId;
        @NonNull CatalogueBookInstanceUUID catalogueBookInstanceUUID;
        @NonNull CheckoutDuration checkoutDuration;
    }
}

