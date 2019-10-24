package com.gakshintala.bookmybook.core.usecases.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.ports.UseCase;
import com.gakshintala.bookmybook.core.ports.persistence.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.core.ports.persistence.catalogue.PersistBookInstance;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookInstanceToCatalogue implements UseCase<AddBookInstanceToCatalogue.AddBookInstanceToCatalogueCommand, Try<CatalogueBookInstanceId>> {
    private final FindCatalogueBook findCatalogueBook;
    private final PersistBookInstance persistBookInstance;

    @Override
    public Try<CatalogueBookInstanceId> execute(AddBookInstanceToCatalogueCommand command) {
        return findCatalogueBook.findBy(command.getIsbn())
                        .flatMap(this::persistBookInstance);
    }

    public Try<CatalogueBookInstanceId> persistBookInstance(CatalogueBook catalogueBook) {
        return persistBookInstance.persist(CatalogueBookInstance.instanceOf(catalogueBook));
    }

    @Value
    public static class AddBookInstanceToCatalogueCommand {
        private final ISBN isbn;
    }
}
