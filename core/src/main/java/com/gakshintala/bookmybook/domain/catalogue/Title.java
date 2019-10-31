package com.gakshintala.bookmybook.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

import java.beans.ConstructorProperties;

@Value
public class Title {

    @NonNull String title;

    @ConstructorProperties({"title"})
    Title(String title) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title.trim();
    }

}
