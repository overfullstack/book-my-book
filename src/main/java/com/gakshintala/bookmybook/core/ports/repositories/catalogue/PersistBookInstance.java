package com.gakshintala.bookmybook.core.ports.repositories.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;

@FunctionalInterface
public interface PersistBookInstance {
    CatalogueBookInstanceUUID persist(CatalogueBookInstance book);
}
