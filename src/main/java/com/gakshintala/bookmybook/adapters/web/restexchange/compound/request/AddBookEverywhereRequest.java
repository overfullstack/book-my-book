package com.gakshintala.bookmybook.adapters.web.restexchange.compound.request;

import com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere.AddBookEverywhereCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class AddBookEverywhereRequest {
    @NotNull
    AddBookToCatalogueRequest bookInfo;
    @NotBlank
    String bookType;

    public AddBookEverywhereCommand toCommand() {
        return new AddBookEverywhereCommand(
                new CatalogueBook(
                        bookInfo.getBookIsbn(),
                        bookInfo.getTitle(),
                        bookInfo.getAuthor()
                ), BookType.fromString(bookType)
        );
    }

}
