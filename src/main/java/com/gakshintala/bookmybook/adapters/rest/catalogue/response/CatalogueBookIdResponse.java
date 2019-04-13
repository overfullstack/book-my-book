package com.gakshintala.bookmybook.adapters.rest.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookId;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookIdResponse {
    private final Boolean success;
    private final String message;
    private final CatalogueBookId catalogueBookId;

    public static CatalogueBookIdResponse fromResult(Try<CatalogueBookId> result) {
        return result.map(catalogueBookId -> new CatalogueBookIdResponse(true, "Success!", catalogueBookId))
                .getOrElseGet(cause -> new CatalogueBookIdResponse(false, cause.getMessage(), null));
    }
}
