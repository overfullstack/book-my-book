package com.gakshintala.bookmybook.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

import java.beans.ConstructorProperties;

@Value
public class ISBN {

    private static final String VERY_SIMPLE_ISBN_CHECK = "^\\d{9}[\\d|X]$";

    @NonNull
    String isbn;

    @ConstructorProperties({"isbn"})
    public ISBN(String isbn) {
        if (!isbn.trim().matches(VERY_SIMPLE_ISBN_CHECK)) {
            throw new IllegalArgumentException("Wrong ISBN!");
        }
        this.isbn = isbn.trim();
    }
}
