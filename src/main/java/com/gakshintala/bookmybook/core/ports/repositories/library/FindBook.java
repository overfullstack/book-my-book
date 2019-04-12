package com.gakshintala.bookmybook.core.ports.repositories.library;

import com.gakshintala.bookmybook.core.domain.library.Book;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindBook {
    Option<Book> findBy(CatalogueBookInstanceUUID catalogueBookId);
}
