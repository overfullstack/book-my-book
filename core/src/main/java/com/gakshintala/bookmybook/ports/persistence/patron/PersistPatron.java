package com.gakshintala.bookmybook.ports.persistence.patron;

import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistPatron {
    Try<Patron> persist(PatronInformation patronInformation);
}
