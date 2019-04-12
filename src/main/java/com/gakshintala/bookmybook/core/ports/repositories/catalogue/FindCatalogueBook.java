package com.gakshintala.bookmybook.core.ports.repositories.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import io.vavr.control.Option;

@FunctionalInterface
public interface FindCatalogueBook {
    Option<CatalogueBook> findBy(ISBN isbn);
    
}
