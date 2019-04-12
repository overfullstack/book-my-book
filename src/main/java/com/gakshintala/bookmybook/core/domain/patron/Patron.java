package com.gakshintala.bookmybook.core.domain.patron;


import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import static com.gakshintala.bookmybook.core.domain.common.EitherResult.announceFailure;
import static com.gakshintala.bookmybook.core.domain.common.EitherResult.announceSuccess;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.*;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected.bookCollectedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollectingFailed.bookCollectingFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled.holdCanceledNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCancelingFailed.holdCancelingFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed.bookHoldFailedNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold.bookPlacedOnHoldNow;
import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldEvents.events;
import static com.gakshintala.bookmybook.core.domain.patron.PatronHolds.MAX_NUMBER_OF_HOLDS;
import static com.gakshintala.bookmybook.core.domain.patron.Rejection.withReason;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = "patronInformation")
public class Patron {

    @NonNull
    private final PatronInformation patronInformation;

    @NonNull
    private final List<PlacingOnHoldPolicy> placingOnHoldPolicies;

    @NonNull
    private final OverdueCheckouts overdueCheckouts;

    @NonNull
    private final PatronHolds patronHolds;

    public Either<BookHoldFailed, BookPlacedOnHoldEvents> placeOnHold(AvailableBook book) {
        return placeOnHold(book, HoldDuration.openEnded());
    }

    public Either<BookHoldFailed, BookPlacedOnHoldEvents> placeOnHold(AvailableBook aBook, HoldDuration duration) {
        Option<Rejection> rejection = patronCanHold(aBook, duration);
        if (rejection.isEmpty()) {
            BookPlacedOnHold bookPlacedOnHold = bookPlacedOnHoldNow(aBook.getBookInstanceId(), aBook.type(), aBook.getLibraryBranch(), patronInformation.getPatronId(), duration);
            if (patronHolds.maximumHoldsAfterHolding(aBook)) {
                return announceSuccess(events(bookPlacedOnHold, MaximumNumberOhHoldsReached.now(patronInformation, MAX_NUMBER_OF_HOLDS)));
            }
            return announceSuccess(events(bookPlacedOnHold));
        }
        return announceFailure(bookHoldFailedNow(rejection.get(), aBook.getBookInstanceId(), aBook.getLibraryBranch(), patronInformation));
    }

    public Either<BookHoldCancelingFailed, BookHoldCanceled> cancelHold(BookOnHold book) {
        if (patronHolds.a(book)) {
            return announceSuccess(holdCanceledNow(book.getBookId(), book.getHoldPlacedAt(), patronInformation.getPatronId()));
        }
        return announceFailure(holdCancelingFailedNow(book.getBookId(), book.getHoldPlacedAt(), patronInformation.getPatronId()));
    }

    public Either<BookCollectingFailed, BookCollected> collect(BookOnHold book, CheckoutDuration duration) {
        if (patronHolds.a(book)) {
            return announceSuccess(bookCollectedNow(book.getBookId(), book.type(), book.getHoldPlacedAt(), patronInformation.getPatronId(), duration));
        }
        return announceFailure(bookCollectingFailedNow(withReason("library is not on hold by patronInformation"), book.getBookId(), book.getHoldPlacedAt(), patronInformation));
    }

    private Option<Rejection> patronCanHold(AvailableBook aBook, HoldDuration forDuration) {
        return placingOnHoldPolicies
                .toStream()
                .map(policy -> policy.apply(aBook, this, forDuration))
                .find(Either::isLeft)
                .map(Either::getLeft);
    }

    boolean isRegular() {
        return patronInformation.isRegular();
    }

    int overdueCheckoutsAt(LibraryBranchId libraryBranch) {
        return overdueCheckouts.countAt(libraryBranch);
    }

    public int numberOfHolds() {
        return patronHolds.count();
    }



}


