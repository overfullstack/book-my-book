package com.gakshintala.bookmybook.core.ports.repositories.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindBookOnHold {

    Try<BookOnHold> findBookOnHold(CatalogueBookInstanceId catalogueBookId);
}
