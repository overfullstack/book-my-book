package com.gakshintala.bookmybook.core.ports;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;

@FunctionalInterface
public interface PersistCatalogBook {
    void persist(CatalogueBook book);
}
