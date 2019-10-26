package com.gakshintala.bookmybook.domain.library;


import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import lombok.NonNull;
import lombok.Value;

@Value
public class BookInformation {

    @NonNull
    CatalogueBookInstanceId catalogueBookInstanceId;

    @NonNull
    BookType bookType;
}
