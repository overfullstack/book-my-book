package com.gakshintala.bookmybook.core.ports.repositories.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindPatron {
    Try<Patron> findBy(PatronId patronId);
}
