package com.gakshintala.bookmybook.core.ports;

import com.gakshintala.bookmybook.core.domain.book.Book;
import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindBook {
    Option<Book> findBy(BookId bookId);
}
