package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookId;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.PersistCatalogueBook;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookToCatalogue implements UseCase<AddBookToCatalogue.AddBookToCatalogueCommand, Try<CatalogueBookId>> {
    private final PersistCatalogueBook persistCatalogueBook;

    @Override
    public Try<CatalogueBookId> execute(AddBookToCatalogueCommand command) {
        return Try.of(() -> persistCatalogueBook.persistBook(command.getCatalogueBook()));
    }

    @Value
    public static class AddBookToCatalogueCommand {
        private final CatalogueBook catalogueBook;
    }
    
}
