package com.gakshintala.bookmybook.core.domain.book;


import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.common.Version;
import com.gakshintala.bookmybook.core.domain.patron.PatronBooksEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = "bookInformation")
public class AvailableBook implements Book {

    @NonNull
    BookInformation bookInformation;

    @NonNull
    LibraryBranchId libraryBranch;

    @NonNull
    Version version;

    public AvailableBook(BookId bookId, BookType type, LibraryBranchId libraryBranchId, Version version) {
        this(new BookInformation(bookId, type), libraryBranchId, version);
    }

    public boolean isRestricted() {
        return bookInformation.getBookType().equals(BookType.Restricted);
    }

    public BookId getBookId() {
        return bookInformation.getBookId();
    }

    public BookOnHold handle(PatronBooksEvent.BookPlacedOnHold bookPlacedOnHold) {
        return new BookOnHold(
                bookInformation,
                new LibraryBranchId(bookPlacedOnHold.getLibraryBranchId()),
                new PatronId(bookPlacedOnHold.getPatronId()),
                bookPlacedOnHold.getHoldTill(),
                version);
    }
}

