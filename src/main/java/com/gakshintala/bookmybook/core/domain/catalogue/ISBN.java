package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

@Value
public class ISBN {

    private static final String VERY_SIMPLE_ISBN_CHECK = "^\\d{9}[\\d|X]$";

    @NonNull
    String isbn;

    public ISBN(String isbn) {
        if (!isbn.trim().matches(VERY_SIMPLE_ISBN_CHECK)) {
            // TODO: 2019-04-09 Do something about these exceptions 
            throw new IllegalArgumentException("Wrong ISBN!");
        }
        this.isbn = isbn.trim();

    }
}
