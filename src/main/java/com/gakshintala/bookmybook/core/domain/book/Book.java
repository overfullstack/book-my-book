package com.gakshintala.bookmybook.core.domain.book;

import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.Version;

public interface Book {

    default BookId bookId() {
        return getBookInformation().getBookId();
    }

    default BookType type() {
        return getBookInformation().getBookType();
    }

    BookInformation getBookInformation();

    Version getVersion();

}

