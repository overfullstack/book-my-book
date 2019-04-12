package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CatalogueBookInstanceUUID {

    @NonNull
    UUID bookInstanceUUID;
    
    public static CatalogueBookInstanceUUID fromString(String catalogueBookId) {
        return new CatalogueBookInstanceUUID(UUID.fromString(catalogueBookId));
    }
}
