package com.gakshintala.bookmybook.restexchange.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookInstanceResponse {
    Boolean success;
    String message;
    CatalogueBookInstanceId catalogueBookInstanceId;

    public static CatalogueBookInstanceResponse fromResult(Try<CatalogueBookInstanceId> result) {
        return result.map(catalogueBookInstanceId -> new CatalogueBookInstanceResponse(true, "Success!", catalogueBookInstanceId))
                .getOrElseGet(cause -> new CatalogueBookInstanceResponse(false, cause.getMessage(), null));
    }
}
