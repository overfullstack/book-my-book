package com.gakshintala.bookmybook.core.domain.patron;

import lombok.NonNull;
import lombok.Value;

@Value
public class Rejection {

    @Value
    static class Reason {
        @NonNull
        String reason;
    }

    @NonNull
    Reason reason;

    public static Rejection withReason(String reason) {
        return new Rejection(new Reason(reason));
    }
}
