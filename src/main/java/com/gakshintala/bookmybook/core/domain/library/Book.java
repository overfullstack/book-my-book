package com.gakshintala.bookmybook.core.domain.library;

import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;

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

