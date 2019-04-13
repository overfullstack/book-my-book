package com.gakshintala.bookmybook.adapters.rest.library.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddBookToLibraryResponse {
    private final Boolean success;
    private final String message;
    private final Tuple2<LibraryBookId, LibraryBranchId> addedBookInfo;

    public static AddBookToLibraryResponse fromResult(Try<Tuple2<LibraryBookId, LibraryBranchId>> result) {
        return result.map(addedBookInfo -> new AddBookToLibraryResponse(true, "Success!", addedBookInfo))
                .getOrElseGet(cause -> new AddBookToLibraryResponse(false, cause.getMessage(), null));
    }
}
