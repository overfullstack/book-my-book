package com.gakshintala.bookmybook.domain.patron;

import com.gakshintala.bookmybook.domain.library.AvailableBook;
import io.vavr.collection.List;
import lombok.experimental.UtilityClass;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@UtilityClass
public class PlacingOnHoldPolicies {

    public static final PlacingOnHoldPolicy onlyResearcherPatronsCanHoldRestrictedBooksPolicy = (AvailableBook toHold, Patron patron, HoldDuration holdDuration) -> {
        if (toHold.isRestricted() && patron.isRegular()) {
            return left(Rejection.withReason("REGULAR patrons cannot hold restricted books"));
        }
        return right(new Allowance());
    };

    public static final PlacingOnHoldPolicy regularPatronMaximumNumberOfHoldsPolicy = (AvailableBook toHold, Patron patron, HoldDuration holdDuration) -> {
        if (patron.isRegular() && patron.numberOfHolds() >= PatronHolds.MAX_NUMBER_OF_HOLDS) {
            return left(Rejection.withReason("patron cannot hold more books"));
        }
        return right(new Allowance());
    };

    private static final PlacingOnHoldPolicy onlyResearcherPatronsCanPlaceOpenEndedHolds = (AvailableBook toHold, Patron patron, HoldDuration holdDuration) -> {
        if (patron.isRegular() && holdDuration.isOpenEnded()) {
            return left(Rejection.withReason("regular patron cannot place open ended holds"));
        }
        return right(new Allowance());
    };

    public static List<PlacingOnHoldPolicy> allCurrentPolicies() {
        return List.of(
                onlyResearcherPatronsCanHoldRestrictedBooksPolicy,
                regularPatronMaximumNumberOfHoldsPolicy,
                onlyResearcherPatronsCanPlaceOpenEndedHolds);
    }
}
