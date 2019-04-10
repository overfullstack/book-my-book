package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.book.Book;
import com.gakshintala.bookmybook.core.ports.PersistBookInLibrary;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookToLibraryUseCase implements UseCase<AddBookToLibraryUseCase.InputValues, AddBookToLibraryUseCase.OutputValues> {
    private final PersistBookInLibrary persistBookInLibrary;

    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(
                Try.of(() -> persistBookInLibrary.persist(input.getAvailableBook()))
        );
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final AvailableBook availableBook;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Try<Book> book;
    }
}
