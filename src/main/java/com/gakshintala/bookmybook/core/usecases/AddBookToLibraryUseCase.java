package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.common.Result;
import com.gakshintala.bookmybook.core.ports.PersistBook;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import static com.gakshintala.bookmybook.core.domain.common.Result.Success;

@Service
@RequiredArgsConstructor
public class AddBookToLibraryUseCase implements UseCase<AddBookToLibraryUseCase.InputValues, AddBookToLibraryUseCase.OutputValues> {
    private final PersistBook persistBook;

    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(
                Try.of(() -> {
                    persistBook.persist(input.getAvailableBook());
                    return Success;
                })
        );
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final AvailableBook availableBook;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Try<Result> result;
    }
}
