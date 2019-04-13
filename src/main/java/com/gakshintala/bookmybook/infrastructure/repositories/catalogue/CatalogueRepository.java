package com.gakshintala.bookmybook.infrastructure.repositories.catalogue;

import com.gakshintala.bookmybook.adapters.db.CatalogueBookDomainMapper;
import com.gakshintala.bookmybook.core.domain.catalogue.*;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.PersistCatalogueBook;
import com.gakshintala.bookmybook.core.ports.repositories.catalogue.PersistBookInstance;
import com.gakshintala.bookmybook.infrastructure.repositories.DBUtils;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Repository
@RequiredArgsConstructor
class CatalogueRepository implements PersistCatalogueBook, PersistBookInstance, FindCatalogueBook {

    private static final String INSERT_CATALOGUE_BOOK_SQL = "INSERT INTO catalogue_book (id, isbn, title, author) VALUES (catalogue_book_seq.nextval, ?, ?, ?)";
    private static final String INSERT_CATALOGUE_BOOK_INSTANCE_SQL = "INSERT INTO catalogue_book_instance (id, isbn, book_id) VALUES (catalogue_book_instance_seq.nextval, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public CatalogueBookId persist(CatalogueBook book) {
        Function<JdbcTemplate, UnaryOperator<KeyHolder>> persistBook = jdbc -> keyHolder -> {
            jdbc.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATALOGUE_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, book.getBookIsbn().getIsbn());
                preparedStatement.setString(2, book.getTitle().getTitle());
                preparedStatement.setString(3, book.getAuthor().getName());
                return preparedStatement;
            }, keyHolder);
            return keyHolder;
        };
        return DBUtils.insertAndGetGeneratedKey(persistBook, jdbcTemplate)
                .map(CatalogueBookId::new)
                .get();
    }

    @Override
    public CatalogueBookInstanceUUID persist(CatalogueBookInstance catalogueBookInstance) {
        Function<JdbcTemplate, UnaryOperator<KeyHolder>> persistBookInstance = jdbc -> keyHolder -> {
            jdbc.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATALOGUE_BOOK_INSTANCE_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, catalogueBookInstance.getBookIsbn().getIsbn());
                preparedStatement.setObject(2, catalogueBookInstance.getCatalogueBookInstanceUUID().getBookInstanceUUID().toString());
                return preparedStatement;
            }, keyHolder);
            return keyHolder;
        };
        DBUtils.insertAndGetGeneratedKey(persistBookInstance, jdbcTemplate);
        return catalogueBookInstance.getCatalogueBookInstanceUUID();
    }

    @Override
    public Option<CatalogueBook> findBy(ISBN isbn) {
        try {
            return Option.of(
                    jdbcTemplate.queryForObject(
                            "SELECT b.* FROM catalogue_book b WHERE b.isbn = ?",
                            new BeanPropertyRowMapper<>(CatalogueBookDatabaseEntity.class),
                            isbn.getIsbn())
            ).map(CatalogueBookDomainMapper::toDomainModel);
        } catch (EmptyResultDataAccessException e) {
            return Option.none();
        }
    }
}

