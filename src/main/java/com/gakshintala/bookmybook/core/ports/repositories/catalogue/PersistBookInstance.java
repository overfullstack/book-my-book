package com.gakshintala.bookmybook.core.ports.repositories.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistBookInstance {
    Try<CatalogueBookInstanceId> persist(CatalogueBookInstance book);
}
