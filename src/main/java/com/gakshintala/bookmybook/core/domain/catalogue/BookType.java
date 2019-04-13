package com.gakshintala.bookmybook.core.domain.catalogue;

public enum BookType {
    Restricted, Circulating;
    
    public static BookType fromString(String bookType) {
        return BookType.valueOf(bookType);
    }
}

