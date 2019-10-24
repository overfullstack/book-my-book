package com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.request;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookToCatalogue.AddBookToCatalogueCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookToCatalogueRequest {
    @NotBlank
    private final String bookIsbn;
    @NotBlank
    private final String title;
    @NotBlank
    private final String author;

    public AddBookToCatalogueCommand toCommand() {
        return new AddBookToCatalogueCommand(
                new CatalogueBook(
                        this.getBookIsbn(),
                        this.getTitle(),
                        this.getAuthor()
                ));
    }
}
