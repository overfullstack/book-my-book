package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.catalogue.*;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.PersistBookInstance;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookInstanceToCatalogue implements UseCase<AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand, Try<CatalogueBookInstanceUUID>> {
    private final FindCatalogueBook findCatalogueBook;
    private final PersistBookInstance persistBookInstance;

    @Override
    public Try<CatalogueBookInstanceUUID> execute(AddBookInstanceToCatalogueCommand command) {
        return Try.of(() -> findCatalogueBook.findBy(command.getIsbn())
                .map(book -> BookInstance.instanceOf(book, command.getBookType()))
                .map(persistBookInstance::persist)
                .get()); // TODO: 2019-04-10 Someway to represent different errors for findCatalogBook not found and db exception with persistBook 

    }

    @Value
    public static class AddBookInstanceToCatalogueCommand {
        private final ISBN isbn;
        private final BookType bookType;
    }
}
