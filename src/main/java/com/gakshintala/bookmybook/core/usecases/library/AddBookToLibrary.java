package com.gakshintala.bookmybook.core.usecases.library;

import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.ports.persistence.library.PersistBookInLibrary;
import com.gakshintala.bookmybook.core.ports.UseCase;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookToLibrary implements UseCase<AddBookToLibrary.AddBookToLibraryCommand, Try<Tuple2<LibraryBookId, AvailableBook>>> {
    private final PersistBookInLibrary persistBookInLibrary;

    @Override
    public Try<Tuple2<LibraryBookId, AvailableBook>> execute(AddBookToLibraryCommand command) {
        return persistBookInLibrary.persist(command.getAvailableBook())
                .map(libraryBookId -> Tuple.of(libraryBookId, command.getAvailableBook()));
    }

    @Value
    public static class AddBookToLibraryCommand {
        private final AvailableBook availableBook;
    }

}
