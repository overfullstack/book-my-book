package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.common.Version;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand;
import com.gakshintala.bookmybook.core.usecases.AddBookToCatalogue.AddBookToCatalogueCommand;
import com.gakshintala.bookmybook.core.usecases.AddBookToLibrary.AddBookToLibraryCommand;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookEverywhere implements UseCase<AddBookEverywhere.AddBookEverywhereCommand, Try<LibraryBookId>> {
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;
    private final AddBookToLibrary addBookToLibrary;

    public final Try<LibraryBookId> execute(AddBookEverywhereCommand command) {
        return addBookToCatalogue.execute(
                new AddBookToCatalogueCommand(command.getCatalogueBook()))
                .flatMap(ignore ->
                        addBookInstanceToCatalogue.execute(
                                new AddBookInstanceToCatalogueCommand(
                                        command.getCatalogueBook().getBookIsbn(),
                                        command.getBookType())))
                .flatMap(catalogueBookInstanceUUID ->
                        addBookToLibrary.execute(
                                new AddBookToLibraryCommand(
                                        new AvailableBook(
                                                catalogueBookInstanceUUID,
                                                command.getBookType(),
                                                LibraryBranchId.randomLibraryId(),
                                                Version.zero()
                                        ))));
    }

    @Value
    public static class AddBookEverywhereCommand {
        private final CatalogueBook catalogueBook;
        private final BookType bookType;
    }

}
