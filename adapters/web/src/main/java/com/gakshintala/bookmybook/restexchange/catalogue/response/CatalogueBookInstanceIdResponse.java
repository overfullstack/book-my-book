package com.gakshintala.bookmybook.restexchange.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookInstanceIdResponse {
    Boolean success;
    String message;
    CatalogueBookInstanceId catalogueBookInstanceId;

    public static CatalogueBookInstanceIdResponse fromResult(Try<CatalogueBookInstanceId> result) {
        return result.map(catalogueBookInstanceId -> new CatalogueBookInstanceIdResponse(true, "Success!", catalogueBookInstanceId))
                .getOrElseGet(cause -> new CatalogueBookInstanceIdResponse(false, cause.getMessage(), null));
    }
}
