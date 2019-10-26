package com.gakshintala.bookmybook.ports.persistence.library;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.patron.PatronEvent;
import io.vavr.control.Try;

// TODO: 2019-04-13 Rename event to something else, also for Patron 
@FunctionalInterface
public interface HandlePatronEventInLibrary {
    Try<CatalogueBookInstanceId> handle(PatronEvent patronEvent);
}
