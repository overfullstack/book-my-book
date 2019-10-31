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
import com.gakshintala.bookmybook.repositories.library.BookEntity;
import com.gakshintala.bookmybook.repositories.library.BookEntity.BookState;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.experimental.UtilityClass;

import static com.gakshintala.bookmybook.repositories.library.BookEntity.BookState.AVAILABLE;
import static com.gakshintala.bookmybook.repositories.library.BookEntity.BookState.COLLECTED;
import static com.gakshintala.bookmybook.repositories.library.BookEntity.BookState.ON_HOLD;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@UtilityClass
public class BookDomainMapper {
    public static Tuple2<LibraryBookId, Book> toDomainModel(BookEntity bookEntity) {
        return create(bookEntity, bookEntity.getBook_state());
    }

    private static Tuple2<LibraryBookId, Book> create(BookEntity bookEntity, BookState bookState) {
        return Tuple.of(new LibraryBookId(bookEntity.getId()), Match(bookState).of(
                Case(API.$(AVAILABLE), () -> toAvailableBook(bookEntity)),
                Case(API.$(ON_HOLD), () -> toBookOnHold(bookEntity)),
                Case(API.$(COLLECTED), () -> toCollectedBook(bookEntity))
        ));
    }

    private static AvailableBook toAvailableBook(BookEntity bookEntity) {
        return new AvailableBook(new CatalogueBookInstanceId(bookEntity.getBook_id()), bookEntity.getBook_type(),
                new LibraryBranchId(bookEntity.getAvailable_at_branch()), new Version(bookEntity.getVersion()));
    }

    private static BookOnHold toBookOnHold(BookEntity bookEntity) {
        return new BookOnHold(new CatalogueBookInstanceId(bookEntity.getBook_id()), bookEntity.getBook_type(),
                new LibraryBranchId(bookEntity.getOn_hold_at_branch()), new PatronId(bookEntity.getOn_hold_by_patron()),
                bookEntity.getOn_hold_till(), new Version(bookEntity.getVersion()));
    }

    private static CollectedBook toCollectedBook(BookEntity bookEntity) {
        return new CollectedBook(new CatalogueBookInstanceId(bookEntity.getBook_id()), bookEntity.getBook_type(),
                new LibraryBranchId(bookEntity.getCollected_at_branch()), new PatronId(bookEntity.getCollected_by_patron()),
                new Version(bookEntity.getVersion()));
    }
}
