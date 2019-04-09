package com.gakshintala.bookmybook.core.ports;


import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindAvailableBook {

    Option<AvailableBook> findAvailableBookBy(BookId bookId);
}
