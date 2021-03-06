package com.gakshintala.bookmybook.restexchange.library.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.library.LibraryBookId;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddBookToLibraryResponse {
    Boolean success;
    String message;
    Tuple2<LibraryBookId, AvailableBook> addedBookInfo;

    public static AddBookToLibraryResponse fromResult(Try<Tuple2<LibraryBookId, AvailableBook>> result) {
        return result.map(addedBookInfo -> new AddBookToLibraryResponse(true, "Success!", addedBookInfo))
                .getOrElseGet(cause -> new AddBookToLibraryResponse(false, cause.getMessage(), null));
    }
}
