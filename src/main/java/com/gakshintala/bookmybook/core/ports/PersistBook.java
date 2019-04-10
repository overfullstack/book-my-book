package com.gakshintala.bookmybook.core.ports;

import com.gakshintala.bookmybook.core.domain.book.Book;

@FunctionalInterface
public interface PersistBook {
    // TODO: 2019-04-10 Some Monad like Try to indicate db operation is successful. 
    Book persist(Book book);
}
