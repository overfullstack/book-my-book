package com.gakshintala.bookmybook.adapters.rest.book.request;

import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.common.Version;
import com.gakshintala.bookmybook.core.usecases.AddBookToLibraryUseCase;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Value
public class AddBookToLibraryRequest {
    @NotBlank
    private final String bookId;
    @NotBlank
    private final String bookType;

    public AddBookToLibraryUseCase.InputValues toInput() {
        return new AddBookToLibraryUseCase.InputValues(
                new AvailableBook(
                        new BookId(UUID.fromString(this.getBookId())),
                        BookType.fromString(this.getBookType()),
                        ourLibraryBranch(),
                        Version.zero()
                ));
    }

    private LibraryBranchId ourLibraryBranch() {
        return new LibraryBranchId(UUID.randomUUID());
    }
}
