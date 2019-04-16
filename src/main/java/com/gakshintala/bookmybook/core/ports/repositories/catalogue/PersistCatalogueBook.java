package com.gakshintala.bookmybook.core.ports.repositories.catalogue;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookId;
import io.vavr.Tuple2;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistCatalogueBook {
    Try<Tuple2<CatalogueBookId, CatalogueBook>> persist(CatalogueBook book);
}
