package com.gakshintala.bookmybook.core.ports.persistence.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistPatron {
    Try<Patron> persist(PatronInformation patronInformation);
}
