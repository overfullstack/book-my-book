package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CatalogueBookInstanceId {

    @NonNull
    UUID bookInstanceUUID;

    public static CatalogueBookInstanceId fromString(String catalogueBookId) {
        return new CatalogueBookInstanceId(UUID.fromString(catalogueBookId));
    }
}
