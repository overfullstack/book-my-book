package com.gakshintala.bookmybook.domain.catalogue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "bookIsbn")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CatalogueBook {

    @NonNull
    private ISBN bookIsbn;
    @NonNull
    private Title title;
    @NonNull
    private Author author;

    public CatalogueBook(String isbn, String author, String title) {
        this(new ISBN(isbn), new Title(title), new Author(author));
    }
}


