package com.gakshintala.bookmybook.core.ports.repositories.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindPatron {
    Option<Patron> findBy(PatronId patronId);
}
