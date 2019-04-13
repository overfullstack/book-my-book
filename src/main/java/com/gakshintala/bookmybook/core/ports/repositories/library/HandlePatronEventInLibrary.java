package com.gakshintala.bookmybook.core.ports.repositories.library;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;

// TODO: 2019-04-13 Rename event to something else, also for Patron 
@FunctionalInterface
public interface HandlePatronEventInLibrary {
    CatalogueBookInstanceUUID handle(PatronEvent patronEvent);
}
