package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogueUseCase;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AddBookInstanceRequest {
    @NotBlank
    private final String isbn;
    @NotBlank
    private final String bookType;

    public AddBookInstanceToCatalogueUseCase.InputValues toInput() {
        return new AddBookInstanceToCatalogueUseCase.InputValues(
                new ISBN(isbn), BookType.fromString(bookType)
        );
    }
}
