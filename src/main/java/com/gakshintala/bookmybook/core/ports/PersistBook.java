package com.gakshintala.bookmybook.core.ports;

import com.gakshintala.bookmybook.core.domain.book.Book;

@FunctionalInterface
public interface PersistBook {
    void persist(Book book);
}
