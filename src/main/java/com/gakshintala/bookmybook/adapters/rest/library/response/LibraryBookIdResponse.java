package com.gakshintala.bookmybook.adapters.rest.library.response;

import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import io.vavr.control.Try;
import lombok.Value;

@Value
public class LibraryBookIdResponse {
    private final Boolean success;
    private final String message;
    private final LibraryBookId libraryBookId;

    public static LibraryBookIdResponse fromResult(Try<LibraryBookId> libraryBookId) {
        return libraryBookId.map(bookResponse -> new LibraryBookIdResponse(true, "Success!", bookResponse))
                .getOrElseGet(cause -> new LibraryBookIdResponse(false, cause.getMessage(), null));
    }
}
