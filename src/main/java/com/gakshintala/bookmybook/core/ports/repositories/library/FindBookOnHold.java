package com.gakshintala.bookmybook.core.ports.repositories.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindBookOnHold {

    Option<BookOnHold> findBy(CatalogueBookInstanceUUID catalogueBookId, PatronId patronId);
}
