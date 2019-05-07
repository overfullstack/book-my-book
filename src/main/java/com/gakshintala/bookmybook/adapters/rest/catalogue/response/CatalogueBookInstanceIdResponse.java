package com.gakshintala.bookmybook.adapters.rest.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookInstanceIdResponse {
    private final Boolean success;
    private final String message;
    private final CatalogueBookInstanceId catalogueBookInstanceId;

    public static CatalogueBookInstanceIdResponse fromResult(Try<CatalogueBookInstanceId> result) {
        return result.map(catalogueBookInstanceId -> new CatalogueBookInstanceIdResponse(true, "Success!", catalogueBookInstanceId))
                .getOrElseGet(cause -> new CatalogueBookInstanceIdResponse(false, cause.getMessage(), null));
    }
}
