package com.gakshintala.bookmybook.core.ports.repositories.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindBookOnHold {

    Try<BookOnHold> findBookOnHold(CatalogueBookInstanceUUID catalogueBookId);
}
