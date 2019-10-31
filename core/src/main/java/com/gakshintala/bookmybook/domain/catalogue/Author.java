package com.gakshintala.bookmybook.domain.catalogue;

import lombok.Value;

import java.beans.ConstructorProperties;

@Value
public class Author {

    String name;

    @ConstructorProperties({"name"})
    Author(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        this.name = name.trim();
    }
}
