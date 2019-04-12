package com.gakshintala.bookmybook.core.ports.repositories.library;

import com.gakshintala.bookmybook.core.domain.library.Book;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;

@FunctionalInterface
public interface PersistBookInLibrary {
    // TODO: 2019-04-10 Some Monad like Try to indicate db operation is successful.
    LibraryBookId persist(Book book);
}
