package com.gakshintala.bookmybook.restexchange.catalogue.request;

import com.gakshintala.bookmybook.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookInstanceToCatalogue;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookInstanceToCatalogueRequest {
    @NotBlank
    private final String isbn;

    public AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand toCommand() {
        return new AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand(
                new ISBN(isbn)
        );
    }
}
