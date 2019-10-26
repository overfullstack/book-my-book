package com.gakshintala.bookmybook.ports.persistence.library;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.AvailableBook;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindAvailableBook {

    Try<AvailableBook> findAvailableBook(CatalogueBookInstanceId catalogueBookId);
}
