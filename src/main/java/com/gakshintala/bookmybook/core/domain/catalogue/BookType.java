package com.gakshintala.bookmybook.core.domain.catalogue;

public enum BookType {
    RESTRICTED, CIRCULATING;
    
    public static BookType fromString(String bookType) {
        return BookType.valueOf(bookType);
    }
}

