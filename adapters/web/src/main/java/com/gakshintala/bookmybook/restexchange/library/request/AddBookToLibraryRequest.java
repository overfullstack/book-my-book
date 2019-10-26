package com.gakshintala.bookmybook.restexchange.library.request;


import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.domain.library.Version;
import com.gakshintala.bookmybook.usecases.library.AddBookToLibrary.AddBookToLibraryCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;


@Value
public class AddBookToLibraryRequest {
    @NotBlank
    String bookInstanceId;
    @NotBlank
    String bookType;

    public AddBookToLibraryCommand toCommand() {
        return new AddBookToLibraryCommand(
                new AvailableBook(
                        CatalogueBookInstanceId.fromString(this.getBookInstanceId()),
                        BookType.fromString(this.getBookType()),
                        LibraryBranchId.randomLibraryId(),
                        Version.zero()
                ));
    }
}
