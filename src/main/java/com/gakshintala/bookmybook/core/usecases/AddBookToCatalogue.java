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
public class AddBookToCatalogue implements UseCase<AddBookToCatalogue.InputValues, AddBookToCatalogue.OutputValues> {
    private final PersistCatalogueBook persistCatalogueBook;
    
    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(
                Try.of(() -> {
                    persistCatalogueBook.persistBook(input.getCatalogueBook());
                    return Success;
                })
        );
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final CatalogueBook catalogueBook;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Try<Result> result;
    }
}
