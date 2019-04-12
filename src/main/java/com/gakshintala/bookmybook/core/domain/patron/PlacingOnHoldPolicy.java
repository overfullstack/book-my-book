package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import io.vavr.Function3;
import io.vavr.control.Either;
import lombok.NonNull;
import lombok.Value;

interface PlacingOnHoldPolicy extends Function3<AvailableBook, Patron, HoldDuration, Either<Rejection, Allowance>> {
}

@Value
class Allowance {
}

@Value
class Rejection {

    @Value
    static class Reason {
        @NonNull
        String reason;
    }

    @NonNull
    Reason reason;

    static Rejection withReason(String reason) {
        return new Rejection(new Reason(reason));
    }
}

