package com.gakshintala.bookmybook.infrastructure.book;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.core.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.core.ports.PersistCatalogBook;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
class CatalogueRepository implements PersistCatalogBook {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void persist(CatalogueBook book) {
        jdbcTemplate.update("" +
                        "INSERT INTO catalogue_book " +
                        "(id, isbn, title, author) VALUES " +
                        "(catalogue_book_seq.nextval, ?, ?, ?)",
                book.getBookIsbn().getIsbn(), book.getTitle().getTitle(), book.getAuthor().getName());
    }

    CatalogueBookInstance saveNew(CatalogueBookInstance bookInstance) {
        jdbcTemplate.update("" +
                        "INSERT INTO catalogue_book_instance " +
                        "(id, isbn, book_id) VALUES " +
                        "(catalogue_book_instance_seq.nextval, ?, ?)",
                bookInstance.getBookIsbn().getIsbn(), bookInstance.getBookId().getBookId());
        return bookInstance;
    }

    Option<CatalogueBook> findBy(ISBN isbn) {
        try {
            return Option.of(
                    jdbcTemplate.queryForObject(
                            "SELECT b.* FROM catalogue_book b WHERE b.isbn = ?",
                            new BeanPropertyRowMapper<>(BookDatabaseRow.class),
                            isbn.getIsbn())
                            .toBook());
        } catch (EmptyResultDataAccessException e) {
            return Option.none();

        }
    }

}

@Data
@NoArgsConstructor
class BookDatabaseRow {
    String isbn;
    String author;
    String title;

    CatalogueBook toBook() {
        return new CatalogueBook(isbn, author, title);
    }
}
