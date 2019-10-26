package com.gakshintala.bookmybook.domain.catalogue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void nameIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Author(""));
    }

}