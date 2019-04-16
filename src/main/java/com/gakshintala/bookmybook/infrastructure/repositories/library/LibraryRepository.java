package com.gakshintala.bookmybook.infrastructure.repositories.library;

import com.gakshintala.bookmybook.adapters.db.BookDomainMapper;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.Book;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.library.CollectedBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldExpired;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHoldEvents;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookReturned;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindAvailableBook;
import com.gakshintala.bookmybook.core.ports.repositories.library.FindBookOnHold;
import com.gakshintala.bookmybook.core.ports.repositories.library.HandlePatronEventInLibrary;
import com.gakshintala.bookmybook.core.ports.repositories.library.PersistBookInLibrary;
import com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity.BookState;
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
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.gakshintala.bookmybook.infrastructure.repositories.DBUtils.insertAndGetGeneratedKey;
import static com.gakshintala.bookmybook.infrastructure.repositories.DBUtils.performUpdateOperation;
import static com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity.BookState.Available;
import static com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity.BookState.Collected;
import static com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity.BookState.OnHold;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.Option;
import static io.vavr.Predicates.instanceOf;

@Repository
@RequiredArgsConstructor
class LibraryRepository implements PersistBookInLibrary, FindAvailableBook, FindBookOnHold, HandlePatronEventInLibrary {

    private static final String INSERT_BOOK_IN_LIBRARY_SQL = "INSERT INTO book_database_entity (id, book_id, book_type, book_state, available_at_branch,on_hold_at_branch, on_hold_by_patron, collected_at_branch, collected_by_patron, on_hold_till, version) VALUES (book_database_entity_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
    private static final String UPDATE_AVAILABLE_BOOK_SQL = "UPDATE book_database_entity b SET b.book_state = ?, b.available_at_branch = ?, b.version = ? WHERE book_id = ? AND version = ?";
    private static final String UPDATE_BOOK_ON_HOLD_SQL = "UPDATE book_database_entity b SET b.book_state = ?, b.on_hold_at_branch = ?, b.on_hold_by_patron = ?, b.on_hold_till = ?, b.version = ? WHERE book_id = ? AND version = ?";
    private static final String UPDATE_COLLECTED_BOOK_SQL = "UPDATE book_database_entity b SET b.book_state = ?, b.collected_at_branch = ?, b.collected_by_patron = ?, b.version = ? WHERE book_id = ? AND version = ?";
    private final JdbcTemplate jdbcTemplate;
    private final Function<AvailableBook, Function<JdbcTemplate, Integer>> updateAvailableBook = availableBook -> jdbc ->
            jdbc.update(UPDATE_AVAILABLE_BOOK_SQL,
                    Available.toString(),
                    availableBook.getLibraryBranchId().getLibraryBranchUUID(),
                    availableBook.getVersion().getVersion() + 1,
                    availableBook.getBookInstanceId().getBookInstanceUUID(),
                    availableBook.getVersion().getVersion());
    private final Function<BookOnHold, Function<JdbcTemplate, Integer>> updateBookOnHold = bookOnHold -> jdbc ->
            jdbc.update(UPDATE_BOOK_ON_HOLD_SQL,
                    OnHold.toString(),
                    bookOnHold.getHoldPlacedAt().getLibraryBranchUUID(),
                    bookOnHold.getByPatron().getPatronId(),
                    bookOnHold.getHoldTill(),
                    bookOnHold.getVersion().getVersion() + 1,
                    bookOnHold.getBookId().getBookInstanceUUID(),
                    bookOnHold.getVersion().getVersion());
    private final Function<CollectedBook, Function<JdbcTemplate, Integer>> updateCollectedBook = collectedBook -> jdbc ->
            jdbc.update(UPDATE_COLLECTED_BOOK_SQL,
                    Collected.toString(),
                    collectedBook.getCollectedAt().getLibraryBranchUUID(),
                    collectedBook.getByPatron().getPatronId(),
                    collectedBook.getVersion().getVersion() + 1,
                    collectedBook.getBookId().getBookInstanceUUID(),
                    collectedBook.getVersion().getVersion());

    @Override
    public Try<LibraryBookId> persist(Book book) {
        return queryBookBy(book.bookId())
                .map(tuple2 -> {
                    updateOptimistically(tuple2._2);
                    return tuple2._1;
                })
                .orElse(() -> insertNew(book));
    }

    private Try<Tuple2<LibraryBookId, Book>> queryBookBy(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return queryBy(catalogueBookInstanceUUID)
                .map(BookDomainMapper::toDomainModel);
    }

    private Try<BookDatabaseEntity> queryBy(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return Try.of(() ->
                Option(jdbcTemplate.queryForObject("SELECT b.* FROM book_database_entity b WHERE b.book_id = ?",
                        new BeanPropertyRowMapper<>(BookDatabaseEntity.class), catalogueBookInstanceUUID.getBookInstanceUUID()))
                        .getOrElseThrow(() -> new IllegalArgumentException("No Book found with Id: " + catalogueBookInstanceUUID))
        );
    }

    private Try<Integer> updateOptimistically(Book book) {
        return performUpdateOperation(
                Match(book).of(
                        Case($(instanceOf(AvailableBook.class)), updateAvailableBook),
                        Case($(instanceOf(BookOnHold.class)), updateBookOnHold),
                        Case($(instanceOf(CollectedBook.class)), updateCollectedBook)
                ), jdbcTemplate);
    }

    private Try<LibraryBookId> insertNew(Book book) {
        return insertAndGetGeneratedKey(
                Match(book).of(
                        Case($(instanceOf(AvailableBook.class)), this::insert),
                        Case($(instanceOf(BookOnHold.class)), this::insert),
                        Case($(instanceOf(CollectedBook.class)), this::insert)
                ), jdbcTemplate)
                .map(LibraryBookId::new);
    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(AvailableBook availableBook) {
        return insert(availableBook.getBookInstanceId(), availableBook.type(), Available,
                availableBook.getLibraryBranchId().getLibraryBranchUUID(), null, null, null, null, null);
    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(CatalogueBookInstanceUUID catalogueBookInstanceUUID,
                                                                    BookType bookType, BookState state,
                                                                    UUID availableAt, UUID onHoldAt, UUID onHoldBy,
                                                                    Instant onHoldTill, UUID collectedAt, UUID collectedBy) {
        return jdbc -> keyHolder -> {
            jdbc.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_IN_LIBRARY_SQL, Statement.RETURN_GENERATED_KEYS);
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

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(BookOnHold bookOnHold) {
        return insert(bookOnHold.getBookId(), bookOnHold.type(), OnHold, null,
                bookOnHold.getHoldPlacedAt().getLibraryBranchUUID(), bookOnHold.getByPatron().getPatronId(), bookOnHold.getHoldTill(), null, null);
    }

    private Function<JdbcTemplate, UnaryOperator<KeyHolder>> insert(CollectedBook collectedBook) {
        return insert(collectedBook.getBookId(), collectedBook.type(), Collected, null, null,
                null, null, collectedBook.getCollectedAt().getLibraryBranchUUID(), collectedBook.getByPatron().getPatronId());
    }

    @Override
    public Try<AvailableBook> findAvailableBook(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return findBookWithType(catalogueBookInstanceUUID, AvailableBook.class);
    }

    private <T extends Book> Try<T> findBookWithType(CatalogueBookInstanceUUID catalogueBookInstanceUUID, Class<T> bookType) {
        return queryBookBy(catalogueBookInstanceUUID)
                        .map(Tuple2::_2)
                        .map(book -> Match(book).of(
                                Case($(instanceOf(bookType)), Function.identity())
                        ));
    }

    @Override
    public Try<BookOnHold> findBookOnHold(CatalogueBookInstanceUUID catalogueBookInstanceUUID) {
        return findBookWithType(catalogueBookInstanceUUID, BookOnHold.class);
    }

    @Override
    public Try<CatalogueBookInstanceUUID> handle(PatronEvent event) {
        return Match(event).of(
                Case($(instanceOf(BookPlacedOnHoldEvents.class)), this::handle),
                Case($(instanceOf(BookPlacedOnHold.class)), this::handle),
                Case($(instanceOf(BookCollected.class)), this::handle),
                Case($(instanceOf(BookHoldCanceled.class)), this::handle),
                Case($(instanceOf(BookHoldExpired.class)), this::handle),
                Case($(instanceOf(BookReturned.class)), this::handle)
        );
    }

    private Try<CatalogueBookInstanceUUID> handle(BookPlacedOnHoldEvents bookPlacedOnHoldEvents) {
        return this.handle(bookPlacedOnHoldEvents.getBookPlacedOnHold());
    }

    private Try<CatalogueBookInstanceUUID> handle(BookPlacedOnHold bookPlacedOnHold) {
        return queryBookBy(new CatalogueBookInstanceUUID(bookPlacedOnHold.getBookId()))
                .map(book -> handleBookPlacedOnHold(book._2, bookPlacedOnHold)
                        .map(this::updateOptimistically))
                .map(ignore -> new CatalogueBookInstanceUUID(bookPlacedOnHold.getBookId()));
    }

    private Try<CatalogueBookInstanceUUID> handle(BookCollected bookCollected) {
        return queryBookBy(new CatalogueBookInstanceUUID(bookCollected.getBookId()))
                .map(book -> handleBookCollected(book._2, bookCollected))
                .map(this::updateOptimistically)
                .map(ignore -> new CatalogueBookInstanceUUID(bookCollected.getBookId()));
    }

    private Try<CatalogueBookInstanceUUID> handle(BookHoldExpired holdExpired) {
        return queryBookBy(new CatalogueBookInstanceUUID(holdExpired.getBookId()))
                .map(book -> handleBookHoldExpired(book._2, holdExpired))
                .map(this::updateOptimistically)
                .map(ignore -> new CatalogueBookInstanceUUID(holdExpired.getBookId()));
    }

    private Try<CatalogueBookInstanceUUID> handle(BookHoldCanceled holdCanceled) {
        return queryBookBy(new CatalogueBookInstanceUUID(holdCanceled.getBookId()))
                .map(book -> handleBookHoldCanceled(book._2, holdCanceled))
                .map(this::updateOptimistically)
                .map(ignore -> new CatalogueBookInstanceUUID(holdCanceled.getBookId()));
    }

    private Try<CatalogueBookInstanceUUID> handle(BookReturned bookReturned) {
        return queryBookBy(new CatalogueBookInstanceUUID(bookReturned.getBookId()))
                .map(book -> handleBookReturned(book._2, bookReturned))
                .map(this::updateOptimistically)
                .map(ignore -> new CatalogueBookInstanceUUID(bookReturned.getBookId()));
    }

    private Option<? extends Book> handleBookPlacedOnHold(Book book, BookPlacedOnHold bookPlacedOnHold) {
        return Match(book).of(
                Case($(instanceOf(AvailableBook.class)), availableBook -> Option(availableBook.handle(bookPlacedOnHold))),
                Case($(instanceOf(BookOnHold.class)), bookOnHold -> bookNotAlreadyOnHoldByPatron(bookOnHold, bookPlacedOnHold)),
                Case($(), () -> Option(book))
        );
    }

    private Option<BookOnHold> bookNotAlreadyOnHoldByPatron(BookOnHold onHold, BookPlacedOnHold bookPlacedOnHold) {
        return Option(onHold)
                .filter(onHoldBook -> !onHoldBook.by(new PatronId(bookPlacedOnHold.getPatronId())));
    }

    private Book handleBookHoldExpired(Book book, BookHoldExpired holdExpired) {
        return Match(book).of(
                Case($(instanceOf(BookOnHold.class)), onHold -> onHold.mapFrom(holdExpired)),
                Case($(), () -> book)
        );
    }

    private Book handleBookHoldCanceled(Book book, BookHoldCanceled holdCanceled) {
        return Match(book).of(
                Case($(instanceOf(BookOnHold.class)), onHold -> onHold.mapFrom(holdCanceled)),
                Case($(), () -> book)
        );
    }

    private Book handleBookCollected(Book book, BookCollected bookCollected) {
        return Match(book).of(
                Case($(instanceOf(BookOnHold.class)), onHold -> onHold.mapFrom(bookCollected)),
                Case($(), () -> book)
        );
    }

    private Book handleBookReturned(Book book, BookReturned bookReturned) {
        return Match(book).of(
                Case($(instanceOf(CollectedBook.class)), collected -> collected.handle(bookReturned)),
                Case($(), () -> book)
        );
    }
}

