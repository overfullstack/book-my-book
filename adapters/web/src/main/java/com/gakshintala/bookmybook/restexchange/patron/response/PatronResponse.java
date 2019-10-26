package com.gakshintala.bookmybook.restexchange.patron.response;

import com.gakshintala.bookmybook.domain.patron.Patron;
import io.vavr.control.Try;
import lombok.Value;

@Value
public class PatronResponse {
    Boolean success;
    String message;
    Patron patron;

    public static PatronResponse fromResult(Try<Patron> result) {
        return result.map(patron -> new PatronResponse(true, "Success!", patron))
                .getOrElseGet(cause -> new PatronResponse(false, cause.getMessage(), null));
    }
}
