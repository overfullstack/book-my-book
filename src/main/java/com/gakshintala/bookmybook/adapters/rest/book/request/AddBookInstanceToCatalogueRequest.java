package com.gakshintala.bookmybook.adapters.rest.book.request;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogue;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookInstanceToCatalogueRequest {
    @NotBlank
    private final String isbn;
    @NotBlank
    private final String bookType;

    public AddBookInstanceToCatalogue.InputValues toInput() {
        return new AddBookInstanceToCatalogue.InputValues(
                new ISBN(isbn), BookType.fromString(bookType)
        );
    }
}
