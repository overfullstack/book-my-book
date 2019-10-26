package com.gakshintala.bookmybook.ports.persistence.catalogue;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistBookInstance {
    Try<CatalogueBookInstanceId> persist(CatalogueBookInstance book);
}
