package com.gakshintala.bookmybook.core.domain.library;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookReturned;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = "bookInformation")
public class CollectedBook implements Book {

    @NonNull
    BookInformation bookInformation;

    @NonNull
    LibraryBranchId collectedAt;

    @NonNull
    PatronId byPatron;

    @NonNull
    Version version;

    public CollectedBook(CatalogueBookInstanceId catalogueBookInstanceId, BookType type, LibraryBranchId libraryBranchId, PatronId patronId, Version version) {
        this(new BookInformation(catalogueBookInstanceId, type), libraryBranchId, patronId, version);
    }

    public CatalogueBookInstanceId getBookId() {
        return bookInformation.getCatalogueBookInstanceId();
    }

    public AvailableBook handle(BookReturned bookReturnedByPatron) {
        return new AvailableBook(
                bookInformation,
                new LibraryBranchId(bookReturnedByPatron.getLibraryBranchId()),
                version);
    }



}

