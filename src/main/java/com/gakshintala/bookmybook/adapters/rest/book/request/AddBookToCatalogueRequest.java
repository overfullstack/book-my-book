package com.gakshintala.bookmybook.adapters.rest.book.request;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.usecases.AddBookToCatalogueUseCase;
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

    public AddBookToCatalogueUseCase.InputValues toInput() {
        return new AddBookToCatalogueUseCase.InputValues(
                new CatalogueBook(
                        this.getBookIsbn(),
                        this.getTitle(),
                        this.getAuthor()
                ));
    }
}
