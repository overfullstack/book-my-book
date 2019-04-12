package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;

@Value
public class PatronHolds {

    static final int MAX_NUMBER_OF_HOLDS = 5;

    Set<Hold> resourcesOnHold;

    boolean a(@NonNull BookOnHold bookOnHold) {
        Hold hold = new Hold(bookOnHold.getBookId(), bookOnHold.getHoldPlacedAt());
        return resourcesOnHold.contains(hold);
    }

    int count() {
        return resourcesOnHold.size();
    }

    boolean maximumHoldsAfterHolding(AvailableBook book) {
        return count() + 1 == MAX_NUMBER_OF_HOLDS;
    }
}
