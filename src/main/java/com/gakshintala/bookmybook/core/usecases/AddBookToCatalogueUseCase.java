package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.common.Result;
import com.gakshintala.bookmybook.core.ports.PersistCatalogBook;
import io.vavr.control.Try;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gakshintala.bookmybook.core.domain.common.Result.Success;

@Service
public class AddBookToCatalogueUseCase implements UseCase<AddBookToCatalogueUseCase.InputValues, AddBookToCatalogueUseCase.OutputValues> {
    private PersistCatalogBook persistCatalogBook;

    @Autowired
    public AddBookToCatalogueUseCase(PersistCatalogBook persistBook) {
        this.persistCatalogBook = persistBook;
    }

    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(
                Try.of(() -> {
                    persistCatalogBook.persist(input.getCatalogueBook());
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
