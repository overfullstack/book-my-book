package com.gakshintala.bookmybook.ports.persistence.catalogue;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookId;
import io.vavr.Tuple2;
import io.vavr.control.Try;

@FunctionalInterface
public interface PersistCatalogueBook {
    Try<Tuple2<CatalogueBookId, CatalogueBook>> persist(CatalogueBook book);
}
