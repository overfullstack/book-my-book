package com.gakshintala.bookmybook.core.usecases;

import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.ports.repositories.library.PersistBookInLibrary;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookToLibrary implements UseCase<AddBookToLibrary.AddBookToLibraryCommand, Try<LibraryBookId>> {
    private final PersistBookInLibrary persistBookInLibrary;

    @Override
    public Try<LibraryBookId> execute(AddBookToLibraryCommand command) {
        return Try.of(() -> persistBookInLibrary.persist(command.getAvailableBook()));
    }

    @Value
    public static class AddBookToLibraryCommand {
        private final AvailableBook availableBook;
    }
    
}
