package com.gakshintala.bookmybook.core.domain.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
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
    LibraryBranchId libraryBranchId;

    @NonNull
    Version version;

    public AvailableBook(CatalogueBookInstanceUUID catalogueBookInstanceUUID, BookType type, LibraryBranchId libraryBranchId, Version version) {
        this(new BookInformation(catalogueBookInstanceUUID, type), libraryBranchId, version);
    }
    
    public boolean isRestricted() {
        return bookInformation.getBookType().equals(BookType.Restricted);
    }

    public CatalogueBookInstanceUUID getBookInstanceId() {
        return bookInformation.getCatalogueBookInstanceUUID();
    }

    public BookOnHold handle(PatronEvent.BookPlacedOnHoldNow bookPlacedOnHoldNow) {
        return new BookOnHold(
                bookInformation,
                new LibraryBranchId(bookPlacedOnHoldNow.getLibraryBranchId()),
                new PatronId(bookPlacedOnHoldNow.getPatronId()),
                bookPlacedOnHoldNow.getHoldTill(),
                version);
    }
}

