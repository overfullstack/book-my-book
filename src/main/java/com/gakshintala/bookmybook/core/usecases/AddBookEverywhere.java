package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.book.Book;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.common.Version;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookEverywhere implements UseCase<AddBookEverywhere.InputValues, Try<Book>> {
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;
    private final AddBookToLibrary addBookToLibrary;

    public final Try<Book> execute(InputValues inputValues) {
        return addBookToCatalogue.execute(new AddBookToCatalogue.InputValues(inputValues.catalogueBook))
                .flatMap(ignore ->
                        addBookInstanceToCatalogue.execute(
                                new AddBookInstanceToCatalogue.InputValues(
                                        inputValues.catalogueBook.getBookIsbn(),
                                        inputValues.bookType)))
                .flatMap(catalogueBookInstance ->
                        addBookToLibrary.execute(
                                new AddBookToLibrary.InputValues(
                                        new AvailableBook(
                                                catalogueBookInstance.getBookId(),
                                                catalogueBookInstance.getBookType(),
                                                LibraryBranchId.randomLibraryId(),
                                                Version.zero()
                                        ))));
    }
    
    @Value
    public static class InputValues implements UseCase.InputValues {
        private final CatalogueBook catalogueBook;
        private final BookType bookType;
    }

}
