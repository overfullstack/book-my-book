package com.gakshintala.bookmybook.adapters.rest.book.request;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.usecases.AddBookEverywhere;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class AddBookEverywhereRequest {
    @NotNull
    AddBookToCatalogueRequest bookInfo;
    @NotBlank
    String bookType;

    public AddBookEverywhere.InputValues toInput() {
        return new AddBookEverywhere.InputValues(
                new CatalogueBook(
                        bookInfo.getBookIsbn(),
                        bookInfo.getTitle(),
                        bookInfo.getAuthor()
                ), BookType.fromString(bookType)
        );
    }

}
