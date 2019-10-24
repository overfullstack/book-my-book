package com.gakshintala.bookmybook.adapters.web.restexchange.patron.response;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import io.vavr.control.Try;
import lombok.Value;

@Value
public class PatronResponse {
    private final Boolean success;
    private final String message;
    private final Patron patron;
    
    public static PatronResponse fromResult(Try<Patron> result) {
        return result.map(patron -> new PatronResponse(true, "Success!", patron))
                .getOrElseGet(cause -> new PatronResponse(false, cause.getMessage(), null));
    }
}
