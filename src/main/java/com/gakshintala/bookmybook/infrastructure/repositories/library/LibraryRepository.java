package com.gakshintala.bookmybook.infrastructure.repositories.library;

import com.gakshintala.bookmybook.adapters.db.BookDomainMapper;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.*;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.AggregateRootIsStale;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindAvailableBook;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindBook;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindBookOnHold;
import com.gakshintala.bookmybook.core.ports.repositories.library.PersistBookInLibrary;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.gakshintala.bookmybook.infrastructure.repositories.DBUtils.operateAndGetGeneratedKey;
import static io.vavr.API.*;
import static io.vavr.Patterns.$Some;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@Repository
@RequiredArgsConstructor
class LibraryRepository implements PersistBookInLibrary, FindAvailableBook, FindBookOnHold, FindBook {

    private static final String INSERT_BOOK_SQL = "INSERT INTO book_database_entity (id, book_id, book_type, book_state, available_at_branch,on_hold_at_branch, on_hold_by_patron, collected_at_branch, collected_by_patron, on_hold_till, version) VALUES (book_database_entity_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
    private static final String UPDATE_AVAILABLE_BOOK_SQL = "UPDATE book_database_entity b SET b.book_state = ?, b.available_at_branch = ?, b.version = ? WHERE book_id = ? AND version = ?";
    private static final String UPDATE_BOOK_ON_HOLD_SQL = "UPDATE book_database_entity b SET b.book_state = ?, b.on_hold_at_branch = ?, b.on_hold_by_patron = ?, b.on_hold_till = ?, b.version = ? WHERE book_id = ? AND version = ?";
    private static final String UPDATE_COLLECTED_BOOK_SQL = "UPDATE book_database_entity b SET b.book_state = ?, b.collected_at_branch = ?, b.collected_by_patron = ?, b.version = ? WHERE book_id = ? AND version = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Option<AvailableBook> findAvailableBookBy(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return Match(findBy(catalogueBookInstanceUUID)).of(
                Case($Some($(instanceOf(AvailableBook.class))), Option::of),
                Case($(), Option::none)
        );
    }

    @Override
    public Option<BookOnHold> findBookOnHold(CatalogueBookInstanceUUID catalogueBookInstanceUUID, PatronId patronId) {
        return Match(findBy(catalogueBookInstanceUUID)).of(
                Case($Some($(instanceOf(BookOnHold.class))), Option::of),
                Case($(), Option::none)
        );
    }
    
    @Override
    public Option<Book> findBy(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return findBookById(catalogueBookInstanceUUID)
                .map(BookDomainMapper::toDomainModel);
    }

    private Option<BookDatabaseEntity> findBookById(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return Try.ofSupplier(() -> of(jdbcTemplate.queryForObject("SELECT b.* FROM book_database_entity b WHERE b.book_id = ?", new BeanPropertyRowMapper<>(BookDatabaseEntity.class), catalogueBookInstanceUUID.getBookInstanceUUID())))
                .getOrElse(none());
    }

    @Override
    public LibraryBookId persist(Book book) {
        return findBy(book.bookId())
                .map(entity -> updateOptimistically(book))
                .getOrElse(() -> insertNew(book));
    }

    private LibraryBookId updateOptimistically(Book book) {
        return operateAndGetGeneratedKey(
                Match(book).of(
                        Case($(instanceOf(AvailableBook.class)), updateAvailableBook),
                        Case($(instanceOf(BookOnHold.class)), updateBookOnHold),
                        Case($(instanceOf(CollectedBook.class)), updateCollectedBook)
                ), jdbcTemplate)
                .map(LibraryBookId::new)
                .getOrElseThrow(() -> new AggregateRootIsStale("Someone has updated library in the meantime, library: " + book));

    }

    private final Function<AvailableBook, Function<JdbcTemplate, UnaryOperator<KeyHolder>>> updateAvailableBook = availableBook -> jdbc -> keyHolder -> {
        jdbc.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_AVAILABLE_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, BookDatabaseEntity.BookState.Available.toString());
            preparedStatement.setString(2, availableBook.getLibraryBranch().getLibraryBranchId().toString());
            preparedStatement.setString(3, String.valueOf(availableBook.getVersion().getVersion() + 1));
            preparedStatement.setString(4, availableBook.getBookInstanceId().getBookInstanceUUID().toString());
            preparedStatement.setString(5, String.valueOf(availableBook.getVersion().getVersion()));
            return preparedStatement;
        }, keyHolder);
        return keyHolder;
    };

    private final Function<BookOnHold, Function<JdbcTemplate, UnaryOperator<KeyHolder>>> updateBookOnHold = bookOnHold -> jdbc -> keyHolder -> {
        jdbc.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_ON_HOLD_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, bookOnHold.getHoldPlacedAt().getLibraryBranchId().toString());
            preparedStatement.setString(2, bookOnHold.getByPatron().getPatronId().toString());
            preparedStatement.setString(3, bookOnHold.getHoldTill().toString());
            preparedStatement.setString(4, String.valueOf(bookOnHold.getVersion().getVersion() + 1));
            preparedStatement.setString(5, bookOnHold.getBookId().getBookInstanceUUID().toString());
            preparedStatement.setString(6, String.valueOf(bookOnHold.getVersion().getVersion()));
            return preparedStatement;
        }, keyHolder);
        return keyHolder;
    };

    private final Function<CollectedBook, Function<JdbcTemplate, UnaryOperator<KeyHolder>>> updateCollectedBook = collectedBook -> jdbc -> keyHolder -> {
        jdbc.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COLLECTED_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, BookDatabaseEntity.BookState.Collected.toString());
            preparedStatement.setString(2, collectedBook.getCollectedAt().getLibraryBranchId().toString());
            preparedStatement.setString(3, String.valueOf(collectedBook.getVersion().getVersion() + 1));
            preparedStatement.setString(4, collectedBook.getBookId().getBookInstanceUUID().toString());
            preparedStatement.setString(5, String.valueOf(collectedBook.getVersion().getVersion()));
            return preparedStatement;
        }, keyHolder);
        return keyHolder;
    };

    private LibraryBookId insertNew(Book book) {
        return operateAndGetGeneratedKey(
                Match(book).of(
                        Case($(instanceOf(AvailableBook.class)), this::insert),
                        Case($(instanceOf(BookOnHold.class)), this::insert),
                        Case($(instanceOf(CollectedBook.class)), this::insert)
                ), jdbcTemplate)
                .map(LibraryBookId::new)
                .get();
    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(AvailableBook availableBook) {
        return insert(availableBook.getBookInstanceId(), availableBook.type(), BookDatabaseEntity.BookState.Available, availableBook.getLibraryBranch().getLibraryBranchId(), null, null, null, null, null);
    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(BookOnHold bookOnHold) {
        return insert(bookOnHold.getBookId(), bookOnHold.type(), BookDatabaseEntity.BookState.OnHold, null, bookOnHold.getHoldPlacedAt().getLibraryBranchId(), bookOnHold.getByPatron().getPatronId(), bookOnHold.getHoldTill(), null, null);

    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(CollectedBook collectedBook) {
        return insert(collectedBook.getBookId(), collectedBook.type(), BookDatabaseEntity.BookState.Collected, null, null, null, null, collectedBook.getCollectedAt().getLibraryBranchId(), collectedBook.getByPatron().getPatronId());

    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(CatalogueBookInstanceUUID catalogueBookInstanceUUID, BookType bookType, BookDatabaseEntity.BookState state, UUID availableAt, UUID onHoldAt, UUID onHoldBy, Instant onHoldTill, UUID collectedAt, UUID collectedBy) {
        return jdbc -> keyHolder -> {
            jdbc.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setObject(1, catalogueBookInstanceUUID.getBookInstanceUUID(), Types.OTHER);
                preparedStatement.setString(2, bookType.toString());
                preparedStatement.setString(3, state.toString());
                preparedStatement.setObject(4, availableAt, Types.OTHER);
                preparedStatement.setObject(5, onHoldAt, Types.OTHER);
                preparedStatement.setObject(6, onHoldBy, Types.OTHER);
                preparedStatement.setObject(7, collectedAt, Types.OTHER);
                preparedStatement.setObject(8, collectedBy, Types.OTHER);
                preparedStatement.setObject(9, onHoldTill, Types.OTHER);
                return preparedStatement;
            }, keyHolder);
            return keyHolder;
        };
    }

    

}

