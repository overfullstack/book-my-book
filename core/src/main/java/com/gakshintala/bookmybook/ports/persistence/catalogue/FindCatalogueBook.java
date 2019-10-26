package com.gakshintala.bookmybook.ports.persistence.catalogue;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.catalogue.ISBN;
import io.vavr.control.Try;

@FunctionalInterface
public interface FindCatalogueBook {
    Try<CatalogueBook> findBy(ISBN isbn);

}
