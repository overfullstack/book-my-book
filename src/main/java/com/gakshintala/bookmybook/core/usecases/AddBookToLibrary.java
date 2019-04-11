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
public class AddBookToLibrary implements UseCase<AddBookToLibrary.InputValues, Try<Book>> {
    private final PersistBookInLibrary persistBookInLibrary;

    @Override
    public Try<Book> execute(InputValues input) {
        return Try.of(() -> persistBookInLibrary.persist(input.getAvailableBook()));
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final AvailableBook availableBook;
    }
    
}
