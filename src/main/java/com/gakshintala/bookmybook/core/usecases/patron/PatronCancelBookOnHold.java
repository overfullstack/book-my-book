package com.gakshintala.bookmybook.core.usecases.patron;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCancelingFailed;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled.holdCanceledNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCancelingFailed.holdCancelingFailedNow;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@Service
@RequiredArgsConstructor
public class PatronCancelBookOnHold implements UseCase<PatronCancelBookOnHold.CancelHoldCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>>> {
    private final FindBookOnHold findBookOnHold;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> execute(@NonNull CancelHoldCommand command) {
        return findBookOnHold.findBookOnHold(command.getCatalogueBookInstanceUUID())
                .map(bookOnHold -> findPatron.findBy(command.getPatronId())
                        .map(patron -> cancelHold(patron, bookOnHold))
                        .map(this::handleResult)
                        .map(Try::get)
                        .get());
    }

    private Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> handleResult(Either<BookHoldCancelingFailed, BookHoldCanceled> result) {
        return result
                .map(bookHoldCanceled -> handlePatronEvent.handle(bookHoldCanceled)
                        .map(patron -> handlePatronEventInLibrary.handle(bookHoldCanceled)
                                .map(catalogueBookInstanceUUID -> Tuple.of((PatronEvent) bookHoldCanceled, patron, catalogueBookInstanceUUID)))
                        .get())
                .getOrElseGet(bookHoldCancelingFailed -> Try.success(Tuple.of(bookHoldCancelingFailed, null, null)));
    }

    private Either<BookHoldCancelingFailed, BookHoldCanceled> cancelHold(Patron patron, BookOnHold book) {
        return patron.getPatronHolds().a(book)
                ? right(holdCanceledNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId()))
                : left(holdCancelingFailedNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId(),
                "Patron doesn't hold this book to Cancel"));
    }

    @Value
    public static class CancelHoldCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull CatalogueBookInstanceUUID catalogueBookInstanceUUID;
    }
}

