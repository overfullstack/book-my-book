package com.gakshintala.bookmybook.core.ports.repositories.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindAvailableBook {

    Option<AvailableBook> findBy(CatalogueBookInstanceUUID catalogueBookId);
}
