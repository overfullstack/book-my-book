package com.gakshintala.bookmybook.adapters.rest.library.request;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.library.Version;
import lombok.Value;

import javax.validation.constraints.NotBlank;

import static com.gakshintala.bookmybook.core.usecases.library.AddBookToLibrary.AddBookToLibraryCommand;

@Value
public class AddBookToLibraryRequest {
    @NotBlank
    private final String bookInstanceId;
    @NotBlank
    private final String bookType;

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
