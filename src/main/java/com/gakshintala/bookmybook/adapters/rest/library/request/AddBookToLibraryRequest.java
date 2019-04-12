package com.gakshintala.bookmybook.adapters.rest.library.request;

import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.common.Version;
import lombok.Value;

import javax.validation.constraints.NotBlank;

import static com.gakshintala.bookmybook.core.usecases.AddBookToLibrary.AddBookToLibraryCommand;

@Value
public class AddBookToLibraryRequest {
    @NotBlank
    private final String bookInstanceId;
    @NotBlank
    private final String bookType;

    public AddBookToLibraryCommand toCommand() {
        return new AddBookToLibraryCommand(
                new AvailableBook(
                        CatalogueBookInstanceUUID.fromString(this.getBookInstanceId()),
                        BookType.fromString(this.getBookType()),
                        LibraryBranchId.randomLibraryId(),
                        Version.zero()
                ));
    }
}
