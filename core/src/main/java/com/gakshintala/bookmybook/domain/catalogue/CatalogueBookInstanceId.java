package com.gakshintala.bookmybook.domain.catalogue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CatalogueBookInstanceId {

    @NonNull
    @JsonProperty("bookInstanceUUID")
    UUID bookInstanceUUID;

    public static CatalogueBookInstanceId fromString(String catalogueBookId) {
        return new CatalogueBookInstanceId(UUID.fromString(catalogueBookId));
    }
}
