package com.gakshintala.bookmybook.core.ports.persistence.library;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import io.vavr.control.Try;

// TODO: 2019-04-13 Rename event to something else, also for Patron 
@FunctionalInterface
public interface HandlePatronEventInLibrary {
    Try<CatalogueBookInstanceId> handle(PatronEvent patronEvent);
}
