package com.gakshintala.bookmybook.core.domain.library;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.Version;

public interface Book {

    default CatalogueBookInstanceUUID bookId() {
        return getBookInformation().getCatalogueBookInstanceUUID();
    }

    default BookType type() {
        return getBookInformation().getBookType();
    }

    BookInformation getBookInformation();

    Version getVersion();

}

