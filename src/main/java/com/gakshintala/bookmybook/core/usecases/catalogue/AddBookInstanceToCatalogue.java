package com.gakshintala.bookmybook.core.usecases.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.PersistBookInstance;
import com.gakshintala.bookmybook.core.usecases.UseCase;
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
        return findCatalogueBook.findBy(command.getIsbn())
                        .flatMap(this::persistBookInstance);
    }
    
    public Try<CatalogueBookInstanceUUID> persistBookInstance(CatalogueBook catalogueBook) {
        return persistBookInstance.persist(CatalogueBookInstance.instanceOf(catalogueBook));
    }

    @Value
    public static class AddBookInstanceToCatalogueCommand {
        private final ISBN isbn;
    }
}