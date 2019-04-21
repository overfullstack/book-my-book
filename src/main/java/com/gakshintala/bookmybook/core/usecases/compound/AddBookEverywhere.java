package com.gakshintala.bookmybook.core.usecases.compound;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.library.Version;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.usecases.UseCase;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookToCatalogue;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookToCatalogue.AddBookToCatalogueCommand;
import com.gakshintala.bookmybook.core.usecases.library.AddBookToLibrary;
import com.gakshintala.bookmybook.core.usecases.library.AddBookToLibrary.AddBookToLibraryCommand;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookEverywhere implements UseCase<AddBookEverywhere.AddBookEverywhereCommand, Try<Tuple2<LibraryBookId, AvailableBook>>> {
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;
    private final AddBookToLibrary addBookToLibrary;

    public final Try<Tuple2<LibraryBookId, AvailableBook>> execute(AddBookEverywhereCommand command) {
        return addBookToCatalogue.execute(new AddBookToCatalogueCommand(command.getCatalogueBook()))
                .flatMap(tuple2 -> addBookInstanceToCatalogue.persistBookInstance(tuple2._2))
                .flatMap(catalogueBookInstanceUUID -> addBookToLibrary.execute(
                        new AddBookToLibraryCommand(new AvailableBook(catalogueBookInstanceUUID, command.getBookType(),
                                LibraryBranchId.randomLibraryId(), Version.zero()))));
    }

    @Value
    public static class AddBookEverywhereCommand {
        private final CatalogueBook catalogueBook;
        private final BookType bookType;
    }

}
