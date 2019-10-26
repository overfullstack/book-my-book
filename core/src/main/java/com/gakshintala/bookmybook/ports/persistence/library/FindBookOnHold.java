package com.gakshintala.bookmybook.ports.persistence.library;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.BookOnHold;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindBookOnHold {

    Try<BookOnHold> findBookOnHold(CatalogueBookInstanceId catalogueBookId);
}
