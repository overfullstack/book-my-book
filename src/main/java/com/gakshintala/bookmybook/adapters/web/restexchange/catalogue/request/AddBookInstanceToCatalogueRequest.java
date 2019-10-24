package com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.request;

import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookInstanceToCatalogueRequest {
    @NotBlank
    private final String isbn;

    public AddBookInstanceToCatalogueCommand toCommand() {
        return new AddBookInstanceToCatalogueCommand(
                new ISBN(isbn)
        );
    }
}
