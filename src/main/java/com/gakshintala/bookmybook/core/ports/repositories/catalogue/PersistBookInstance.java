package com.gakshintala.bookmybook.core.ports.repositories.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistBookInstance {
    Try<CatalogueBookInstanceUUID> persist(CatalogueBookInstance book);
}
