package com.gakshintala.bookmybook.core.ports.repositories.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookId;

@FunctionalInterface
public interface PersistCatalogueBook {
    CatalogueBookId persistBook(CatalogueBook book);
}
