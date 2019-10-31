package com.gakshintala.bookmybook.usecases.patron;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.BookOnHold;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.domain.patron.CheckoutDuration;
import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookCollectingFailed;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.ports.persistence.library.FindBookOnHold;
import com.gakshintala.bookmybook.ports.persistence.library.HandlePatronEventInLibrary;
import com.gakshintala.bookmybook.ports.persistence.patron.FindPatron;
import com.gakshintala.bookmybook.ports.persistence.patron.PatronEventHandler;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookCollected.bookCollectedNow;
import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookCollectingFailed.bookCollectingFailedNow;
import static com.gakshintala.bookmybook.domain.patron.Rejection.withReason;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Service
@RequiredArgsConstructor
public class PatronCollectBookOnHold implements UseCase<PatronCollectBookOnHold.CollectBookCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>>> {
    private final FindBookOnHold findBookOnHold;
    private final FindPatron findPatron;
    private final PatronEventHandler patronEventHandler;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> execute(@NonNull CollectBookCommand command) {
        return findBookOnHold.findBookOnHold(command.getCatalogueBookInstanceId())
                .flatMap(bookOnHold -> findPatron.findBy(command.getPatronId())
                        .map(patron -> collect(patron, bookOnHold, command.getCheckoutDuration()))
                        .flatMap(this::handleResult));
    }

    private Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> handleResult(Either<BookCollectingFailed, BookCollected> result) {
        return result
                .map(bookCollected -> patronEventHandler.handle(bookCollected)
                        .flatMap(patron -> handlePatronEventInLibrary.handle(bookCollected)
                                .map(catalogueBookInstanceId -> Tuple.of((PatronEvent) bookCollected, patron, catalogueBookInstanceId)))) // Just simulating the need where downstream need upstream results
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
        @NonNull CatalogueBookInstanceId catalogueBookInstanceId;
        @NonNull CheckoutDuration checkoutDuration;
    }
}

