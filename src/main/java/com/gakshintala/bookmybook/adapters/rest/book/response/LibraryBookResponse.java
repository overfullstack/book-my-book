package com.gakshintala.bookmybook.adapters.rest.book.response;

import com.gakshintala.bookmybook.core.domain.book.Book;
import io.vavr.control.Try;
import lombok.Value;

@Value
public class LibraryBookResponse {
    private final Boolean success;
    private final String message;
    private final Book book;

    public static LibraryBookResponse fromResult(Try<Book> book) {
        return book.map(bookResponse -> new LibraryBookResponse(true, "Success!", bookResponse))
                .getOrElseGet(cause -> new LibraryBookResponse(false, cause.getMessage(), null));
    }
}
