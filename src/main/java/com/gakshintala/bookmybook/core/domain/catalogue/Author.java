package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

@Value
public class Author {

    @NonNull String name;

    Author(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        this.name = name.trim();
    }
}
