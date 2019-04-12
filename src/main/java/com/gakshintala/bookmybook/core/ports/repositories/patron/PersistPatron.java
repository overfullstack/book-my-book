package com.gakshintala.bookmybook.core.ports.repositories.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;

@FunctionalInterface
public interface PersistPatron {
    Patron persist(PatronInformation patronInformation);
}
