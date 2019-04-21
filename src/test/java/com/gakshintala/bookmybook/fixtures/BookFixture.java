package com.gakshintala.bookmybook.fixtures;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.Version;
import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import com.gakshintala.bookmybook.core.domain.library.BookInformation;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.library.CollectedBook;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;

import java.time.Instant;
import java.util.UUID;

import static com.gakshintala.bookmybook.core.domain.catalogue.BookType.Circulating;
import static com.gakshintala.bookmybook.core.domain.catalogue.BookType.Restricted;
import static com.gakshintala.bookmybook.fixtures.LibraryBranchFixture.anyBranch;

public class BookFixture {

    public static BookOnHold bookOnHold(CatalogueBookInstanceUUID bookId, LibraryBranchId libraryBranchId) {
        return new BookOnHold(new BookInformation(bookId, Circulating), libraryBranchId, anyPatronId(), Instant.now(), version0());
    }

    public static AvailableBook circulatingBook() {
        return new AvailableBook(new BookInformation(anyBookId(), Circulating), anyBranch(), version0());
    }

    public static BookOnHold bookOnHold() {
        return new BookOnHold(new BookInformation(anyBookId(), Circulating), anyBranch(), anyPatronId(), Instant.now(), version0());
    }

    private static AvailableBook circulatingAvailableBookAt(LibraryBranchId libraryBranchId) {
        return new AvailableBook(new BookInformation(anyBookId(), Circulating), libraryBranchId, version0());
    }

    public static AvailableBook circulatingAvailableBookAt(CatalogueBookInstanceUUID bookId, LibraryBranchId libraryBranchId) {
        return new AvailableBook(new BookInformation(bookId, Circulating), libraryBranchId, version0());
    }

    public static AvailableBook aBookAt(LibraryBranchId libraryBranchId) {
        return new AvailableBook(new BookInformation(anyBookId(), Circulating), libraryBranchId, version0());
    }

    private static Version version0() {
        return new Version(0);
    }

    public static AvailableBook circulatingAvailableBook() {
        return circulatingAvailableBookAt(anyBranch());
    }

    public static CollectedBook collectedBook() {
        return new CollectedBook(new BookInformation(anyBookId(), Circulating), anyBranch(), anyPatronId(), version0());
    }

    public static AvailableBook restrictedBook() {
        return new AvailableBook(new BookInformation(anyBookId(), Restricted), anyBranch(), version0());
    }

    public static CatalogueBookInstanceUUID anyBookId() {
        return new CatalogueBookInstanceUUID(UUID.randomUUID());
    }


    private static PatronId anyPatronId() {
        return new PatronId(UUID.randomUUID());
    }


}
