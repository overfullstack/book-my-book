package com.gakshintala.bookmybook.domain.patron;


import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldFailed;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHoldNow;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookHoldFailed.bookHoldFailedNow;
import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHold.with;
import static com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHoldNow.bookPlacedOnHoldNow;
import static com.gakshintala.bookmybook.domain.patron.PatronEvent.MaximumNumberOfHoldsReached.now;
import static com.gakshintala.bookmybook.domain.patron.PatronHolds.MAX_NUMBER_OF_HOLDS;

@Value
@EqualsAndHashCode(of = "patronInformation")
public class Patron {

    @NonNull
    PatronInformation patronInformation;

    @NonNull
    List<PlacingOnHoldPolicy> placingOnHoldPolicies;

    @NonNull
    PatronHolds patronHolds;

    public Either<BookHoldFailed, BookPlacedOnHold> canPatronPlaceOnHold(AvailableBook aBook, HoldDuration duration) {
        return validateCanPatronPlaceOnHold(aBook, duration)
                .map(rejection -> getBookHoldFailed(aBook, rejection))
                .toEither(getBookPlacedOnHold(aBook, duration))
                .swap();
    }

    private BookHoldFailed getBookHoldFailed(AvailableBook aBook, Rejection rejection) {
        return bookHoldFailedNow(rejection, aBook.getBookInstanceId(), aBook.getLibraryBranchId().getLibraryBranchUUID(),
                patronInformation.getPatronId());
    }

    /**
     * Two different events for same cause, just that one has extra info when MAX_NUMBER_OF_HOLDS is reached.
     *
     * @param aBook
     * @param duration
     * @return
     */
    private BookPlacedOnHold getBookPlacedOnHold(AvailableBook aBook, HoldDuration duration) {
        return patronHolds.maximumHoldsAfterHolding()
                ? with(getBookPlacedOnHoldNow(aBook, duration), now(patronInformation, MAX_NUMBER_OF_HOLDS))
                : with(getBookPlacedOnHoldNow(aBook, duration));
    }

    private BookPlacedOnHoldNow getBookPlacedOnHoldNow(AvailableBook aBook, HoldDuration duration) {
        return bookPlacedOnHoldNow(aBook.getBookInstanceId(), aBook.type(),
                aBook.getLibraryBranchId(), patronInformation.getPatronId(), duration);
    }

    private Option<Rejection> validateCanPatronPlaceOnHold(AvailableBook availableBook, HoldDuration forDuration) {
        return placingOnHoldPolicies
                .map(policy -> policy.apply(availableBook, this, forDuration))
                .find(Either::isLeft)
                .map(Either::getLeft);
    }

    boolean isRegular() {
        return patronInformation.isRegular();
    }

    int numberOfHolds() {
        return patronHolds.count();
    }

}


