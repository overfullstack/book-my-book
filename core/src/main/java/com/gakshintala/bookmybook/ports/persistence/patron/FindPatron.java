package com.gakshintala.bookmybook.ports.persistence.patron;

import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindPatron {
    Try<Patron> findBy(PatronId patronId);
}
