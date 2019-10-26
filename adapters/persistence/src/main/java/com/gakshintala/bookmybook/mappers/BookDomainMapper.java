package com.gakshintala.bookmybook.mappers;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.library.Book;
import com.gakshintala.bookmybook.domain.library.BookOnHold;
import com.gakshintala.bookmybook.domain.library.CollectedBook;
import com.gakshintala.bookmybook.domain.library.LibraryBookId;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.domain.library.Version;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.repositories.library.BookDatabaseEntity;
import com.gakshintala.bookmybook.repositories.library.BookDatabaseEntity.BookState;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.experimental.UtilityClass;

import static com.gakshintala.bookmybook.repositories.library.BookDatabaseEntity.BookState.AVAILABLE;
import static com.gakshintala.bookmybook.repositories.library.BookDatabaseEntity.BookState.COLLECTED;
import static com.gakshintala.bookmybook.repositories.library.BookDatabaseEntity.BookState.ON_HOLD;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@UtilityClass
public class BookDomainMapper {
    public static Tuple2<LibraryBookId, Book> toDomainModel(BookDatabaseEntity bookDatabaseEntity) {
        return create(bookDatabaseEntity, bookDatabaseEntity.getBook_state());
    }

    private static Tuple2<LibraryBookId, Book> create(BookDatabaseEntity bookDatabaseEntity, BookState bookState) {
        return Tuple.of(new LibraryBookId(bookDatabaseEntity.getId()), Match(bookState).of(
                Case(API.$(AVAILABLE), () -> toAvailableBook(bookDatabaseEntity)),
                Case(API.$(ON_HOLD), () -> toBookOnHold(bookDatabaseEntity)),
                Case(API.$(COLLECTED), () -> toCollectedBook(bookDatabaseEntity))
        ));
    }

    private static AvailableBook toAvailableBook(BookDatabaseEntity bookDatabaseEntity) {
        return new AvailableBook(new CatalogueBookInstanceId(bookDatabaseEntity.getBook_id()), bookDatabaseEntity.getBook_type(),
                new LibraryBranchId(bookDatabaseEntity.getAvailable_at_branch()), new Version(bookDatabaseEntity.getVersion()));
    }

    private static BookOnHold toBookOnHold(BookDatabaseEntity bookDatabaseEntity) {
        return new BookOnHold(new CatalogueBookInstanceId(bookDatabaseEntity.getBook_id()), bookDatabaseEntity.getBook_type(),
                new LibraryBranchId(bookDatabaseEntity.getOn_hold_at_branch()), new PatronId(bookDatabaseEntity.getOn_hold_by_patron()),
                bookDatabaseEntity.getOn_hold_till(), new Version(bookDatabaseEntity.getVersion()));
    }

    private static CollectedBook toCollectedBook(BookDatabaseEntity bookDatabaseEntity) {
        return new CollectedBook(new CatalogueBookInstanceId(bookDatabaseEntity.getBook_id()), bookDatabaseEntity.getBook_type(),
                new LibraryBranchId(bookDatabaseEntity.getCollected_at_branch()), new PatronId(bookDatabaseEntity.getCollected_by_patron()),
                new Version(bookDatabaseEntity.getVersion()));
    }
}
