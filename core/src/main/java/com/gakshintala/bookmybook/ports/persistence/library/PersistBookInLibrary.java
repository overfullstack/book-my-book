package com.gakshintala.bookmybook.ports.persistence.library;

import com.gakshintala.bookmybook.domain.library.Book;
import com.gakshintala.bookmybook.domain.library.LibraryBookId;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistBookInLibrary {
    Try<LibraryBookId> persist(Book book);
}
