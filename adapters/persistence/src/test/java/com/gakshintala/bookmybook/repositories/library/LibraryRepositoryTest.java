package com.gakshintala.bookmybook.repositories.library;

import com.gakshintala.bookmybook.domain.library.Book;
import com.gakshintala.bookmybook.ports.persistence.library.FindAvailableBook;
import com.gakshintala.bookmybook.ports.persistence.library.PersistBookInLibrary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import static com.gakshintala.bookmybook.fixtures.BookFixture.circulatingAvailableBook;

@JdbcTest
@ComponentScan
@ContextConfiguration(classes = LibraryRepository.class)
class LibraryRepositoryTest {

    @Autowired
    PersistBookInLibrary persistBookInLibrary;

    @Autowired
    FindAvailableBook findAvailableBook;

    @Test
    void persist() {
        Book book = circulatingAvailableBook();
        persistBookInLibrary.persist(book);
        final var availableBook = findAvailableBook.findAvailableBook(book.getBookInformation().getCatalogueBookInstanceId()).get();
        Assertions.assertEquals(book.getBookInformation().getCatalogueBookInstanceId(), availableBook.getBookInformation().getCatalogueBookInstanceId());
    }
}
