package com.gakshintala.bookmybook.core.domain.library;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.Version;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollected;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCanceled;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldExpired;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookReturned;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
@EqualsAndHashCode(of = "bookInformation")
public class BookOnHold implements Book {

    @NonNull
    BookInformation bookInformation;

    @NonNull
    LibraryBranchId holdPlacedAt;

    @NonNull
    PatronId byPatron;

    @NonNull
    Instant holdTill;

    @NonNull
    Version version;

    public BookOnHold(CatalogueBookInstanceUUID catalogueBookInstanceUUID, BookType type, LibraryBranchId libraryBranchId, PatronId patronId, Instant holdTill, Version version) {
        this(new BookInformation(catalogueBookInstanceUUID, type), libraryBranchId, patronId, holdTill, version);
    }

    public AvailableBook mapFrom(BookReturned bookReturned) {
        return new AvailableBook(
                bookInformation, new LibraryBranchId(bookReturned.getLibraryBranchId()),
                version);
    }

    public AvailableBook mapFrom(BookHoldExpired bookHoldExpired) {
        return new AvailableBook(
                bookInformation,
                new LibraryBranchId(bookHoldExpired.getLibraryBranchId()),
                version);
    }

    public CollectedBook mapFrom(BookCollected bookCollected) {
        return new CollectedBook(
                bookInformation,
                new LibraryBranchId(bookCollected.getLibraryBranchId()),
                new PatronId(bookCollected.getPatronId()),
                version);
    }

    public AvailableBook mapFrom(BookHoldCanceled bookHoldCanceled) {
        return new AvailableBook(
                bookInformation, new LibraryBranchId(bookHoldCanceled.getLibraryBranchId()),
                version);
    }


    public CatalogueBookInstanceUUID getBookId() {
        return bookInformation.getCatalogueBookInstanceUUID();
    }

    public boolean by(PatronId patronId) {
        return byPatron.equals(patronId);
    }
}

