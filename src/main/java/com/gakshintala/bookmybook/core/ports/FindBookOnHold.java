package com.gakshintala.bookmybook.core.ports;


import com.gakshintala.bookmybook.core.domain.book.BookOnHold;
import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindBookOnHold {

    Option<BookOnHold> findBookOnHold(BookId bookId, PatronId patronId);
}
