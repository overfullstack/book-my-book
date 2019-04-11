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
public class AddBookEverywhere implements UseCase<AddBookEverywhere.InputValues, AddBookEverywhere.OutputValues> {
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;
    private final AddBookToLibrary addBookToLibrary;

    public final OutputValues execute(InputValues inputValues) {
        return new AddBookEverywhere.OutputValues(
                addBookToCatalogue.execute(new AddBookToCatalogue.InputValues(inputValues.catalogueBook))
                        .getResult()
                        .flatMap(result ->
                                addBookInstanceToCatalogue.execute(
                                        new AddBookInstanceToCatalogue.InputValues(
                                                inputValues.catalogueBook.getBookIsbn(),
                                                inputValues.bookType)
                                ).getCatalogueBookInstance())
                        .flatMap(catalogueBookInstance ->
                                addBookToLibrary.execute(
                                        new AddBookToLibrary.InputValues(
                                                new AvailableBook(
                                                        catalogueBookInstance.getBookId(),
                                                        catalogueBookInstance.getBookType(),
                                                        LibraryBranchId.randomLibraryId(),
                                                        Version.zero()
                                                ))
                                ).getBook())
        );
    }


    @Value
    public static class InputValues implements UseCase.InputValues {
        private final CatalogueBook catalogueBook;
        private final BookType bookType;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Try<Book> book;
    }
}
