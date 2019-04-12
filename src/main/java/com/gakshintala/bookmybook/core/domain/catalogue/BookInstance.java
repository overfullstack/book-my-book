package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class BookInstance {

    @NonNull
    ISBN bookIsbn;
    @NonNull
    CatalogueBookInstanceUUID catalogueBookInstanceUUID;
    @NonNull
    BookType bookType;

    public static BookInstance instanceOf(CatalogueBook catalogueBook, BookType bookType) {
        return new BookInstance(catalogueBook.getBookIsbn(), new CatalogueBookInstanceUUID(UUID.randomUUID()), bookType);
    }
}
