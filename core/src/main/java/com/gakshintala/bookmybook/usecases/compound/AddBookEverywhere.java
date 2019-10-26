package com.gakshintala.bookmybook.usecases.compound;


import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.domain.library.Version;
import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookToCatalogue;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookToCatalogue.AddBookToCatalogueCommand;
import com.gakshintala.bookmybook.usecases.library.AddBookToLibrary;
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
                .flatMap(catalogueBookInstanceId -> addBookToLibrary.execute(
                        new AddBookToLibrary.AddBookToLibraryCommand(new AvailableBook(catalogueBookInstanceId, command.getBookType(),
                                LibraryBranchId.randomLibraryId(), Version.zero()))));
    }

    @Value
    public static class AddBookEverywhereCommand {
        private final CatalogueBook catalogueBook;
        private final BookType bookType;
    }

}
