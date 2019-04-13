package com.gakshintala.bookmybook.adapters.rest.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookInstanceUUIDResponse {
    private final Boolean success;
    private final String message;
    private final CatalogueBookInstanceUUID catalogueBookInstanceUUID;

    public static CatalogueBookInstanceUUIDResponse fromResult(Try<CatalogueBookInstanceUUID> result) {
        return result.map(catalogueBookInstanceUUID -> new CatalogueBookInstanceUUIDResponse(true, "Success!", catalogueBookInstanceUUID))
                .getOrElseGet(cause -> new CatalogueBookInstanceUUIDResponse(false, cause.getMessage(), null));
    }
}
