package com.gakshintala.bookmybook.infrastructure.book;


import com.gakshintala.bookmybook.core.domain.book.AvailableBook;
import com.gakshintala.bookmybook.core.domain.book.Book;
import com.gakshintala.bookmybook.core.domain.book.BookOnHold;
import com.gakshintala.bookmybook.core.domain.book.CollectedBook;
import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.common.Version;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import io.vavr.API;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static com.gakshintala.bookmybook.infrastructure.book.BookDatabaseEntity.BookState.*;
import static io.vavr.API.*;

@NoArgsConstructor
@Data
class BookDatabaseEntity {

    enum BookState {
        Available, OnHold, Collected
    }

    UUID book_id;
    BookType book_type;
    BookState book_state;
    UUID available_at_branch;
    UUID on_hold_at_branch;
    UUID on_hold_by_patron;
    Instant on_hold_till;
    UUID collected_at_branch;
    UUID collected_by_patron;
    int version;

    Book toDomainModel() {
        return Match(book_state).of(
                Case(API.$(Available), this::toAvailableBook),
                Case(API.$(OnHold), this::toBookOnHold),
                Case(API.$(Collected), this::toCollectedBook)
        );
    }

    private AvailableBook toAvailableBook() {
        return new AvailableBook(new BookId(book_id), book_type,  new LibraryBranchId(available_at_branch), new Version(version));
    }

    private BookOnHold toBookOnHold() {
        return new BookOnHold(new BookId(book_id), book_type, new LibraryBranchId(on_hold_at_branch), new PatronId(on_hold_by_patron), on_hold_till, new Version(version));
    }

    private CollectedBook toCollectedBook() {
        return new CollectedBook(new BookId(book_id), book_type,  new LibraryBranchId(collected_at_branch), new PatronId(collected_by_patron), new Version(version));
    }
}

