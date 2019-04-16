package com.gakshintala.bookmybook.core.ports.repositories.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import io.vavr.control.Try;

@FunctionalInterface
public interface HandlePatronEvent {
    Try<Patron> handle(PatronEvent patronEvent);
}
