package com.gakshintala.bookmybook.domain.catalogue;

import lombok.Value;

import java.beans.ConstructorProperties;

@Value
public class Author {

    String name;
    // TODO 1/30/20 gakshintala: Try to use a static constructor and instead of throwing exception, return a Either Monad.
    @ConstructorProperties({"name"})
    Author(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        this.name = name.trim();
    }
}
