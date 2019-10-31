package com.gakshintala.bookmybook.repositories.catalogue;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.ports.persistence.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.ports.persistence.catalogue.PersistCatalogueBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

@JdbcTest
@ComponentScan
class CatalogueRepositoryTest {

    @Autowired
    PersistCatalogueBook persistCatalogueBook;

    @Autowired
    FindCatalogueBook findCatalogueBook;

    @Test
    void persist() {
        var catalogueBook = new CatalogueBook("0321125215", "title", "author");
        persistCatalogueBook.persist(catalogueBook);
        final var foundBook = findCatalogueBook.findBy(catalogueBook.getIsbn());
        Assertions.assertEquals(catalogueBook.getAuthor(), foundBook.get().getAuthor());
    }
}