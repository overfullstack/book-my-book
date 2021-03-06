package com.gakshintala.bookmybook.repositories.catalogue;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookId;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstance;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.catalogue.ISBN;
import com.gakshintala.bookmybook.mappers.CatalogueBookDomainMapper;
import com.gakshintala.bookmybook.ports.persistence.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.ports.persistence.catalogue.PersistBookInstance;
import com.gakshintala.bookmybook.ports.persistence.catalogue.PersistCatalogueBook;
import com.gakshintala.bookmybook.repositories.DBUtils;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.function.UnaryOperator;

/**
 * This repository is implemented using plain SQL queries and JdbcTemplate.
 */
@Repository
@RequiredArgsConstructor
class CatalogueRepository implements PersistCatalogueBook, PersistBookInstance, FindCatalogueBook {

    private static final String INSERT_CATALOGUE_BOOK_SQL = "INSERT INTO catalogue_book (id, isbn, title, author) VALUES (catalogue_book_seq.nextval, ?, ?, ?)";
    private static final String INSERT_CATALOGUE_BOOK_INSTANCE_SQL = "INSERT INTO catalogue_book_instance (id, isbn, book_id) VALUES (catalogue_book_instance_seq.nextval, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Try<Tuple2<CatalogueBookId, CatalogueBook>> persist(CatalogueBook catalogueBook) {
        UnaryOperator<KeyHolder> persistBook = keyHolder -> {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATALOGUE_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, catalogueBook.getIsbn().getIsbn());
                preparedStatement.setString(2, catalogueBook.getTitle().getTitle());
                preparedStatement.setString(3, catalogueBook.getAuthor().getName());
                return preparedStatement;
            }, keyHolder);
            return keyHolder;
        };
        return DBUtils.insertAndGetGeneratedKey(persistBook)
                .map(catalogueBookId -> Tuple.of(new CatalogueBookId(catalogueBookId), catalogueBook));
    }

    @Override
    public Try<CatalogueBookInstanceId> persist(CatalogueBookInstance catalogueBookInstance) {
        UnaryOperator<KeyHolder> persistBookInstance = keyHolder -> {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATALOGUE_BOOK_INSTANCE_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, catalogueBookInstance.getBookIsbn().getIsbn());
                preparedStatement.setObject(2, catalogueBookInstance.getCatalogueBookInstanceId().getBookInstanceUUID().toString());
                return preparedStatement;
            }, keyHolder);
            return keyHolder;
        };
        return DBUtils.insertAndGetGeneratedKey(persistBookInstance)
                .map(ignore -> catalogueBookInstance.getCatalogueBookInstanceId());
    }

    @Override
    public Try<CatalogueBook> findBy(ISBN isbn) {
        return Try.of(() -> Option.of(
                jdbcTemplate.queryForObject(
                        "SELECT b.* FROM catalogue_book b WHERE b.isbn = ?",
                        new BeanPropertyRowMapper<>(CatalogueBookEntity.class),
                        isbn.getIsbn()))
                .map(CatalogueBookDomainMapper::toDomainModel)
                .getOrElseThrow(() -> new IllegalArgumentException("No Book found with ISBN: " + isbn.getIsbn())));
    }
}

