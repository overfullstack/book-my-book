package com.gakshintala.bookmybook.usecases.library;

import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.ports.persistence.library.PersistBookInLibrary;
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
