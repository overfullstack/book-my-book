package com.gakshintala.bookmybook.restexchange.catalogue.request;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookToCatalogue.AddBookToCatalogueCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookToCatalogueRequest {
    @NotBlank
    String bookIsbn;
    @NotBlank
    String title;
    @NotBlank
    String author;

    public AddBookToCatalogueCommand toCommand() {
        return new AddBookToCatalogueCommand(
                new CatalogueBook(
                        this.getBookIsbn(),
                        this.getTitle(),
                        this.getAuthor()
                ));
    }
}
