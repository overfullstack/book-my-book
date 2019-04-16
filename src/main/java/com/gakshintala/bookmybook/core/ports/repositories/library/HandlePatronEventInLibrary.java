package com.gakshintala.bookmybook.core.ports.repositories.library;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import io.vavr.control.Try;

// TODO: 2019-04-13 Rename event to something else, also for Patron 
@FunctionalInterface
public interface HandlePatronEventInLibrary {
    Try<CatalogueBookInstanceUUID> handle(PatronEvent patronEvent);
}
