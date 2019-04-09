package com.gakshintala.bookmybook.core.domain.patron;



import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.book.BookOnHold;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import static com.gakshintala.bookmybook.core.domain.common.EitherResult.announceFailure;
import static com.gakshintala.bookmybook.core.domain.common.EitherResult.announceSuccess;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookCollected.bookCollectedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookCollectingFailed.bookCollectingFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookHoldCanceled.holdCanceledNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookHoldCancelingFailed.holdCancelingFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookHoldFailed.bookHoldFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookPlacedOnHold.bookPlacedOnHoldNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent.BookPlacedOnHoldEvents.events;
import static com.gakshintala.bookmybook.core.domain.patron.PatronHolds.MAX_NUMBER_OF_HOLDS;
import static com.gakshintala.bookmybook.core.domain.patron.Rejection.withReason;


//TODO - rename to patron?
@AllArgsConstructor
@EqualsAndHashCode(of = "patron")
public class PatronBooks {

    @NonNull
    private final PatronInformation patron;

    @NonNull
    private final List<PlacingOnHoldPolicy> placingOnHoldPolicies;

    @NonNull
    private final OverdueCheckouts overdueCheckouts;

    @NonNull
    private final PatronHolds patronHolds;

    public Either<PatronBooksEvent.BookHoldFailed, PatronBooksEvent.BookPlacedOnHoldEvents> placeOnHold(AvailableBook book) {
        return placeOnHold(book, HoldDuration.openEnded());
    }

    public Either<PatronBooksEvent.BookHoldFailed, PatronBooksEvent.BookPlacedOnHoldEvents> placeOnHold(AvailableBook aBook, HoldDuration duration) {
        Option<Rejection> rejection = patronCanHold(aBook, duration);
        if (rejection.isEmpty()) {
            PatronBooksEvent.BookPlacedOnHold bookPlacedOnHold = bookPlacedOnHoldNow(aBook.getBookId(), aBook.type(), aBook.getLibraryBranch(), patron.getPatronId(), duration);
            if (patronHolds.maximumHoldsAfterHolding(aBook)) {
                return announceSuccess(events(bookPlacedOnHold, PatronBooksEvent.MaximumNumberOhHoldsReached.now(patron, MAX_NUMBER_OF_HOLDS)));
            }
            return announceSuccess(events(bookPlacedOnHold));
        }
        return announceFailure(bookHoldFailedNow(rejection.get(), aBook.getBookId(), aBook.getLibraryBranch(), patron));
    }

    public Either<PatronBooksEvent.BookHoldCancelingFailed, PatronBooksEvent.BookHoldCanceled> cancelHold(BookOnHold book) {
        if (patronHolds.a(book)) {
            return announceSuccess(holdCanceledNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronId()));
        }
        return announceFailure(holdCancelingFailedNow(book.getBookId(), book.getHoldPlacedAt(), patron.getPatronId()));
    }

    public Either<PatronBooksEvent.BookCollectingFailed, PatronBooksEvent.BookCollected> collect(BookOnHold book, CheckoutDuration duration) {
        if (patronHolds.a(book)) {
            return announceSuccess(bookCollectedNow(book.getBookId(), book.type(), book.getHoldPlacedAt(), patron.getPatronId(), duration));
        }
        return announceFailure(bookCollectingFailedNow(withReason("book is not on hold by patron"), book.getBookId(), book.getHoldPlacedAt(), patron));
    }

    private Option<Rejection> patronCanHold(AvailableBook aBook, HoldDuration forDuration) {
        return placingOnHoldPolicies
                .toStream()
                .map(policy -> policy.apply(aBook, this, forDuration))
                .find(Either::isLeft)
                .map(Either::getLeft);
    }

    boolean isRegular() {
        return patron.isRegular();
    }

    int overdueCheckoutsAt(LibraryBranchId libraryBranch) {
        return overdueCheckouts.countAt(libraryBranch);
    }

    public int numberOfHolds() {
        return patronHolds.count();
    }



}


