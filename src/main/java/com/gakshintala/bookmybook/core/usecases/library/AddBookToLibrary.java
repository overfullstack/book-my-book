package com.gakshintala.bookmybook.core.usecases.library;

import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.ports.repositories.library.PersistBookInLibrary;
import com.gakshintala.bookmybook.core.usecases.UseCase;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddBookToLibrary implements UseCase<AddBookToLibrary.AddBookToLibraryCommand, Try<Tuple2<LibraryBookId, LibraryBranchId>>> {
    private final PersistBookInLibrary persistBookInLibrary;

    @Override
    public Try<Tuple2<LibraryBookId, LibraryBranchId>> execute(AddBookToLibraryCommand command) {
        return Try.of(() -> Tuple.of(persistBookInLibrary.persist(command.getAvailableBook()), command.getAvailableBook().getLibraryBranchId()));
    }

    @Value
    public static class AddBookToLibraryCommand {
        private final AvailableBook availableBook;
    }
    
}
