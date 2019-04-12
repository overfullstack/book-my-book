package com.gakshintala.bookmybook.core.domain.catalogue;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.ToString;

@ToString
public enum BookType {
    Restricted, Circulating;

    @JsonCreator
    public static BookType fromString(String bookType) {
        return BookType.valueOf(bookType);
    }
}

