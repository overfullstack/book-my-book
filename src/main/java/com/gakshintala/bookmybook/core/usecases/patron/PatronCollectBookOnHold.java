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
import com.gakshintala.bookmybook.core.ports.UseCase;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected.bookCollectedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollectingFailed.bookCollectingFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.Rejection.withReason;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Service
@RequiredArgsConstructor
public class PatronCollectBookOnHold implements UseCase<PatronCollectBookOnHold.CollectBookCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>>> {
    private final FindBookOnHold findBookOnHold;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> execute(@NonNull CollectBookCommand command) {
        return findBookOnHold.findBookOnHold(command.getCatalogueBookInstanceUUID())
                .map(bookOnHold -> findPatron.findBy(command.getPatronId())
                        .map(patron -> collect(patron, bookOnHold, command.getCheckoutDuration()))
                        .map(this::handleResult)
                        .map(Try::get)
                        .get());
    }

    private Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> handleResult(Either<BookCollectingFailed, BookCollected> result) {
        return result
                .map(bookCollected -> handlePatronEvent.handle(bookCollected)
                        .map(patron -> handlePatronEventInLibrary.handle(bookCollected)
                                .map(catalogueBookInstanceUUID -> Tuple.of((PatronEvent) bookCollected, patron, catalogueBookInstanceUUID)))
                        .get())
                .getOrElseGet(bookCollectingFailed -> Try.success(Tuple.of(bookCollectingFailed, null, null)));
    }

    private Either<BookCollectingFailed, BookCollected> collect(Patron patron, BookOnHold book, CheckoutDuration duration) {
        return patron.getPatronHolds().a(book)
                ? right(bookCollectedNow(book.getBookId(), book.type(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId(), duration))
                : left(bookCollectingFailedNow(withReason("Patron doesn't hold this book to Collect"),
                book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId()));
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

