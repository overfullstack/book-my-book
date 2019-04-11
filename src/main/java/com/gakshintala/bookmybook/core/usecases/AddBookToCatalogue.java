package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.common.Result;
import com.gakshintala.bookmybook.core.ports.catalogue.PersistCatalogueBook;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import static com.gakshintala.bookmybook.core.domain.common.Result.Success;

@Service
@RequiredArgsConstructor
public class AddBookToCatalogue implements UseCase<AddBookToCatalogue.InputValues, Try<Result>> {
    private final PersistCatalogueBook persistCatalogueBook;

    @Override
    public Try<Result> execute(InputValues input) {
        return Try.of(() -> {
            persistCatalogueBook.persistBook(input.getCatalogueBook());
            return Success;
        });
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final CatalogueBook catalogueBook;
    }
    
}
