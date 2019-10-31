package com.gakshintala.bookmybook.usecases.patron;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.BookOnHold;
import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldCancelingFailed;
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

import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldCanceled.holdCanceledNow;
import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldCancelingFailed.holdCancelingFailedNow;
import static com.gakshintala.bookmybook.domain.patron.Rejection.withReason;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Service
@RequiredArgsConstructor
public class PatronCancelBookOnHold implements UseCase<PatronCancelBookOnHold.CancelHoldCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>>> {
    private final FindBookOnHold findBookOnHold;
    private final FindPatron findPatron;
    private final PatronEventHandler patronEventHandler;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> execute(@NonNull CancelHoldCommand command) {
        return findBookOnHold.findBookOnHold(command.getCatalogueBookInstanceId())
                .flatMap(bookOnHold -> findPatron.findBy(command.getPatronId())
                        .map(patron -> cancelHold(patron, bookOnHold))
                        .flatMap(this::handleResult));
    }

    private Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> handleResult(Either<BookHoldCancelingFailed, BookHoldCanceled> result) {
        return result
                .map(bookHoldCanceled -> patronEventHandler.handle(bookHoldCanceled)
                        .flatMap(patron -> handlePatronEventInLibrary.handle(bookHoldCanceled)
                                .map(catalogueBookInstanceId -> Tuple.of((PatronEvent) bookHoldCanceled, patron, catalogueBookInstanceId)))) // The nesting id done only to use args from upper layer compositions
                .getOrElseGet(bookHoldCancelingFailed -> Try.success(Tuple.of(bookHoldCancelingFailed, null, null)));
    }

    private Either<BookHoldCancelingFailed, BookHoldCanceled> cancelHold(Patron patron, BookOnHold book) {
        return patron.getPatronHolds().a(book)
                ? right(holdCanceledNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId()))
                : left(holdCancelingFailedNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId(),
                withReason("Patron doesn't hold this book to Cancel")));
    }

    @Value
    public static class CancelHoldCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull CatalogueBookInstanceId catalogueBookInstanceId;
    }
}

