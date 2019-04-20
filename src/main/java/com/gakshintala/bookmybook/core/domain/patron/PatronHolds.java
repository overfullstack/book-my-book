package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import lombok.NonNull;
import lombok.Value;

import io.vavr.collection.Set;

@Value
public class PatronHolds {

    public static final int MAX_NUMBER_OF_HOLDS = 5;

    Set<Hold> resourcesOnHold;

    public boolean a(@NonNull BookOnHold bookOnHold) {
        Hold hold = new Hold(bookOnHold.getBookId(), bookOnHold.getHoldPlacedAt());
        return resourcesOnHold.contains(hold);
    }

    int count() {
        return resourcesOnHold.size();
    }

    public boolean maximumHoldsAfterHolding() {
        return count() + 1 == MAX_NUMBER_OF_HOLDS;
    }
}
