package com.gakshintala.bookmybook.core.domain.library;


import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import lombok.NonNull;
import lombok.Value;

@Value
public class BookInformation {

    @NonNull
    CatalogueBookInstanceId catalogueBookInstanceId;

    @NonNull
    BookType bookType;
}
