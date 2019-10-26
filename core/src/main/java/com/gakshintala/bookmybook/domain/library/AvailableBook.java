package com.gakshintala.bookmybook.domain.library;


import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.patron.PatronEvent.BookPlacedOnHoldNow;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import static com.gakshintala.bookmybook.domain.catalogue.BookType.RESTRICTED;


@Value
@AllArgsConstructor
@EqualsAndHashCode(of = "bookInformation")
public class AvailableBook implements Book {

    @NonNull
    BookInformation bookInformation;

    @NonNull
    LibraryBranchId libraryBranchId;

    @NonNull
    Version version;

    public AvailableBook(CatalogueBookInstanceId catalogueBookInstanceId, BookType type, LibraryBranchId libraryBranchId, Version version) {
        this(new BookInformation(catalogueBookInstanceId, type), libraryBranchId, version);
    }

    public boolean isRestricted() {
        return RESTRICTED.equals(bookInformation.getBookType());
    }

    public CatalogueBookInstanceId getBookInstanceId() {
        return bookInformation.getCatalogueBookInstanceId();
    }

    public BookOnHold handle(BookPlacedOnHoldNow bookPlacedOnHoldNow) {
        return new BookOnHold(
                bookInformation,
                new LibraryBranchId(bookPlacedOnHoldNow.getLibraryBranchId()),
                new PatronId(bookPlacedOnHoldNow.getPatronId()),
                bookPlacedOnHoldNow.getHoldTill(),
                version);
    }
}

