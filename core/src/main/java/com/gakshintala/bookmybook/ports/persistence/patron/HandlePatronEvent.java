package com.gakshintala.bookmybook.ports.persistence.patron;

import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronEvent;
import io.vavr.control.Try;

@FunctionalInterface
public interface HandlePatronEvent {
    Try<Patron> handle(PatronEvent patronEvent);
}
