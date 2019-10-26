package com.gakshintala.bookmybook.fixtures;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.library.BookInformation;
import com.gakshintala.bookmybook.domain.library.BookOnHold;
import com.gakshintala.bookmybook.domain.library.CollectedBook;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.domain.library.Version;
import com.gakshintala.bookmybook.domain.patron.PatronId;

import java.time.Instant;
import java.util.UUID;

import static com.gakshintala.bookmybook.domain.catalogue.BookType.CIRCULATING;
import static com.gakshintala.bookmybook.domain.catalogue.BookType.RESTRICTED;
import static com.gakshintala.bookmybook.fixtures.LibraryBranchFixture.anyBranch;


public class BookFixture {

    public static BookOnHold bookOnHold(CatalogueBookInstanceId bookId, LibraryBranchId libraryBranchId) {
        return new BookOnHold(new BookInformation(bookId, CIRCULATING), libraryBranchId, anyPatronId(), Instant.now(), version0());
    }

    private static Version version0() {
        return new Version(0);
    }

    public static PatronId anyPatronId() {
        return new PatronId(UUID.randomUUID());
    }

    public static AvailableBook circulatingBook() {
        return new AvailableBook(new BookInformation(anyBookId(), CIRCULATING), anyBranch(), version0());
    }

    public static CatalogueBookInstanceId anyBookId() {
        return new CatalogueBookInstanceId(UUID.randomUUID());
    }

    public static BookOnHold bookOnHold() {
        return new BookOnHold(new BookInformation(anyBookId(), CIRCULATING), anyBranch(), anyPatronId(), Instant.now(), version0());
    }

    public static AvailableBook circulatingAvailableBookAt(CatalogueBookInstanceId bookId, LibraryBranchId libraryBranchId) {
        return new AvailableBook(new BookInformation(bookId, CIRCULATING), libraryBranchId, version0());
    }

    public static AvailableBook circulatingAvailableBook() {
        return circulatingAvailableBookAt(anyBranch());
    }

    private static AvailableBook circulatingAvailableBookAt(LibraryBranchId libraryBranchId) {
        return new AvailableBook(new BookInformation(anyBookId(), CIRCULATING), libraryBranchId, version0());
    }

    public static AvailableBook aBookAt(LibraryBranchId libraryBranchId) {
        return new AvailableBook(new BookInformation(anyBookId(), CIRCULATING), libraryBranchId, version0());
    }

    public static CollectedBook collectedBook() {
        return new CollectedBook(new BookInformation(anyBookId(), CIRCULATING), anyBranch(), anyPatronId(), version0());
    }

    public static AvailableBook restrictedBook() {
        return new AvailableBook(new BookInformation(anyBookId(), RESTRICTED), anyBranch(), version0());
    }


}
