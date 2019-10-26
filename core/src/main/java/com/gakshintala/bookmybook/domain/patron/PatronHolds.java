package com.gakshintala.bookmybook.domain.patron;

import com.gakshintala.bookmybook.domain.library.BookOnHold;
import io.vavr.collection.Set;
import lombok.NonNull;
import lombok.Value;

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

    boolean maximumHoldsAfterHolding() {
        return count() + 1 == MAX_NUMBER_OF_HOLDS;
    }
}
