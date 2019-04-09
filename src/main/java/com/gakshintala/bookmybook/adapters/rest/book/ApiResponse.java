package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.core.domain.common.Result;
import io.vavr.control.Try;
import lombok.Value;
import org.springframework.http.ResponseEntity;

@Value
public class ApiResponse {
    private final Boolean success;
    private final String message;

    public static ResponseEntity<ApiResponse> fromResult(Try<Result> result) {
        return ResponseEntity.ok(
                result.map(ignore -> new ApiResponse(true, "Success!"))
                        .getOrElseGet(cause -> new ApiResponse(false, cause.getMessage()))
        );
    }
}
