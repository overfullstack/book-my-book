package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.ports.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.core.ports.catalogue.PersistCatalogueBookInstance;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookInstanceToCatalogueUseCase implements UseCase<AddBookInstanceToCatalogueUseCase.InputValues, AddBookInstanceToCatalogueUseCase.OutputValues> {
    private final FindCatalogueBook findCatalogueBook;
    private final PersistCatalogueBookInstance persistCatalogueBookInstance;

    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(
                Try.of(() -> findCatalogueBook
                        .findBy(new ISBN(input.getIsbn().getIsbn()))
                        .map(book -> CatalogueBookInstance.instanceOf(book, input.getBookType()))
                        .map(persistCatalogueBookInstance::persistBookInstance)
                        .get()) // TODO: 2019-04-10 Someway to represent different errors for findCatalogBook not found and db exception with persistBook 
        );
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final ISBN isbn;
        private final BookType bookType;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Try<CatalogueBookInstance> catalogueBookInstance;
    }
}
