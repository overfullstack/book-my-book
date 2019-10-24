package com.gakshintala.bookmybook.core.ports.persistence.library;

import com.gakshintala.bookmybook.core.domain.library.Book;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistBookInLibrary {
    Try<LibraryBookId> persist(Book book);
}
