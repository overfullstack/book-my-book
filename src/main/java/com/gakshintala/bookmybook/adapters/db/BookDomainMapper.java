package com.gakshintala.bookmybook.adapters.db;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.common.Version;
import com.gakshintala.bookmybook.core.domain.library.*;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity;
import com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity.BookState;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.experimental.UtilityClass;

import static com.gakshintala.bookmybook.infrastructure.repositories.library.BookDatabaseEntity.BookState.*;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@UtilityClass
public class BookDomainMapper {
    public static Tuple2<Integer, Book> toDomainModel(BookDatabaseEntity bookDatabaseEntity) {
        return toDomainModelWithState(bookDatabaseEntity, bookDatabaseEntity.getBook_state());
    }

    private static Tuple2<Integer, Book> toDomainModelWithState(BookDatabaseEntity bookDatabaseEntity, BookState bookState) {
        return Tuple.of(bookDatabaseEntity.getId(), Match(bookState).of(
                Case(API.$(Available), () -> toAvailableBook(bookDatabaseEntity)),
                Case(API.$(OnHold), () -> toBookOnHold(bookDatabaseEntity)),
                Case(API.$(Collected), () -> toCollectedBook(bookDatabaseEntity))
        ));
    }

    private static AvailableBook toAvailableBook(BookDatabaseEntity bookDatabaseEntity) {
        return new AvailableBook(new CatalogueBookInstanceUUID(bookDatabaseEntity.getBook_id()), bookDatabaseEntity.getBook_type(),
                new LibraryBranchId(bookDatabaseEntity.getAvailable_at_branch()), new Version(bookDatabaseEntity.getVersion()));
    }

    private static BookOnHold toBookOnHold(BookDatabaseEntity bookDatabaseEntity) {
        return new BookOnHold(new CatalogueBookInstanceUUID(bookDatabaseEntity.getBook_id()), bookDatabaseEntity.getBook_type(),
                new LibraryBranchId(bookDatabaseEntity.getOn_hold_at_branch()), new PatronId(bookDatabaseEntity.getOn_hold_by_patron()),
                bookDatabaseEntity.getOn_hold_till(), new Version(bookDatabaseEntity.getVersion()));
    }

    private static CollectedBook toCollectedBook(BookDatabaseEntity bookDatabaseEntity) {
        return new CollectedBook(new CatalogueBookInstanceUUID(bookDatabaseEntity.getBook_id()), bookDatabaseEntity.getBook_type(),
                new LibraryBranchId(bookDatabaseEntity.getCollected_at_branch()), new PatronId(bookDatabaseEntity.getCollected_by_patron()),
                new Version(bookDatabaseEntity.getVersion()));
    }
}
