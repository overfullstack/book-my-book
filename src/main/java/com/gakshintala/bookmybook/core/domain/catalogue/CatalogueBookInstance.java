package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class CatalogueBookInstance {

    @NonNull
    ISBN bookIsbn;
    @NonNull
    BookId bookId;
    @NonNull
    BookType bookType;

    static CatalogueBookInstance instanceOf(CatalogueBook catalogueBook, BookType bookType) {
        return new CatalogueBookInstance(catalogueBook.getBookIsbn(), new BookId(UUID.randomUUID()), bookType);

    }
}
