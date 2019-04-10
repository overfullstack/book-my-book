package com.gakshintala.bookmybook.core.ports.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;

@FunctionalInterface
public interface PersistCatalogueBookInstance {
    CatalogueBookInstance persistBookInstance(CatalogueBookInstance book);
}
