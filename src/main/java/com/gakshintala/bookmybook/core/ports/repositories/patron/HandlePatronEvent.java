package com.gakshintala.bookmybook.core.ports.repositories.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;

@FunctionalInterface
public interface HandlePatronEvent {
    Patron handle(PatronEvent patronEvent);
}
