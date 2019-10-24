package com.gakshintala.bookmybook.core.ports.persistence.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindAvailableBook {

    Try<AvailableBook> findAvailableBook(CatalogueBookInstanceId catalogueBookId);
}
