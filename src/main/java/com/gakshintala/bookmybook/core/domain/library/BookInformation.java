package com.gakshintala.bookmybook.core.domain.library;


import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import lombok.NonNull;
import lombok.Value;

@Value
public class BookInformation {

    @NonNull
    CatalogueBookInstanceUUID catalogueBookInstanceUUID;

    @NonNull
    BookType bookType;
}
