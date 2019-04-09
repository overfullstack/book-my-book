package com.gakshintala.bookmybook.core.ports;

import com.gakshintala.bookmybook.core.domain.patron.PatronBooks;
import com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import io.vavr.control.Option;

public interface PatronBooksRepository {

    Option<PatronBooks> findBy(PatronId patronId);

    PatronBooks publish(PatronBooksEvent event);
}
