package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CatalogueBookInstance {

    @NonNull
    ISBN bookIsbn;
    @NonNull
    CatalogueBookInstanceId catalogueBookInstanceId;

    public static CatalogueBookInstance instanceOf(CatalogueBook catalogueBook) {
        return new CatalogueBookInstance(catalogueBook.getBookIsbn(), new CatalogueBookInstanceId(UUID.randomUUID()));
    }
}
