package com.gakshintala.bookmybook.domain.library;

import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;

public interface Book {

    default CatalogueBookInstanceId bookId() {
        return getBookInformation().getCatalogueBookInstanceId();
    }

    default BookType type() {
        return getBookInformation().getBookType();
    }

    BookInformation getBookInformation();

    Version getVersion();

}

