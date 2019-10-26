package com.gakshintala.bookmybook.usecases.catalogue;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookId;
import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.ports.persistence.catalogue.PersistCatalogueBook;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookToCatalogue implements UseCase<AddBookToCatalogue.AddBookToCatalogueCommand, Try<Tuple2<CatalogueBookId, CatalogueBook>>> {
    private final PersistCatalogueBook persistCatalogueBook;

    @Override
    public Try<Tuple2<CatalogueBookId, CatalogueBook>> execute(AddBookToCatalogueCommand command) {
        return persistCatalogueBook.persist(command.getCatalogueBook());
    }

    @Value
    public static class AddBookToCatalogueCommand {
        private final CatalogueBook catalogueBook;
    }

}
