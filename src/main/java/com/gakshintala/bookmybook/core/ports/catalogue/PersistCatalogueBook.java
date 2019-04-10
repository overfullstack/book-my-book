package com.gakshintala.bookmybook.core.ports.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;

@FunctionalInterface
public interface PersistCatalogueBook {
    CatalogueBook persistBook(CatalogueBook book);
}
