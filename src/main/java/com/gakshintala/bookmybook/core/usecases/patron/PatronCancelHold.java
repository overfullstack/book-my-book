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
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.gakshintala.bookmybook.core.domain.common.EitherResult.failure;
import static com.gakshintala.bookmybook.core.domain.common.EitherResult.success;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled.holdCanceledNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCancelingFailed.holdCancelingFailedNow;

@Service
@AllArgsConstructor
public class PatronCancelHold implements UseCase<PatronCancelHold.CancelHoldCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>>> {
    private final FindBookOnHold findBookOnHold;
    private final FindPatron findPatron;
    private final HandlePatronEvent handlePatronEvent;
    private final HandlePatronEventInLibrary handlePatronEventInLibrary;
    
    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceUUID>> execute(@NonNull CancelHoldCommand command) {
        return Try.of(() -> {
            BookOnHold bookOnHold = find(command.getCatalogueBookInstanceUUID(), command.getPatronId());
            Patron patron = find(command.getPatronId());
            Either<BookHoldCancelingFailed, BookHoldCanceled> result = cancelHold(patron, bookOnHold);
            return result.isRight()
                    ? Tuple.of(result.get(), handlePatronEvent.handle(result.get()), handlePatronEventInLibrary.handle(result.get()))
                    : Tuple.of(result.getLeft(), null, null);
        });
    }

    private BookOnHold find(CatalogueBookInstanceUUID bookId, PatronId patronId) {
        return findBookOnHold
                .findBy(bookId, patronId)
                .getOrElseThrow(() -> new IllegalArgumentException("Cannot find book on hold with Id: " + bookId.getBookInstanceUUID()));
    }

    private Patron find(PatronId patronId) {
        return findPatron
                .findBy(patronId)
                .getOrElseThrow(() -> new IllegalArgumentException("Patron with given Id does not exists: " + patronId.getPatronId()));
    }

    private Either<BookHoldCancelingFailed, BookHoldCanceled> cancelHold(Patron patron, BookOnHold book) {
        if (patron.getPatronHolds().a(book)) {
            return success(holdCanceledNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId()));
        }
        return failure(holdCancelingFailedNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronInformation().getPatronId()));
    }

    @Value
    public static class CancelHoldCommand {
        @NonNull Instant timestamp;
        @NonNull PatronId patronId;
        @NonNull CatalogueBookInstanceUUID catalogueBookInstanceUUID;
    }
}

