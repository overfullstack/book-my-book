package com.gakshintala.bookmybook.adapters.rest.catalogue.request;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookInstanceToCatalogueRequest {
    @NotBlank
    private final String isbn;
    @NotBlank
    private final String bookType;

    public AddBookInstanceToCatalogueCommand toCommand() {
        return new AddBookInstanceToCatalogueCommand(
                new ISBN(isbn), 
                BookType.fromString(bookType)
        );
    }
}
