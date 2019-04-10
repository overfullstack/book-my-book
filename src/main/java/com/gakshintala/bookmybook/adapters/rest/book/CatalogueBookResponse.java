package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstance;
import io.vavr.control.Try;
import lombok.Value;

@Value
public class CatalogueBookResponse {
    private final Boolean success;
    private final String message;
    private final CatalogueBookInstance catalogueBookResponse;

    public static CatalogueBookResponse fromResult(Try<CatalogueBookInstance> result) {
        return result.map(catalogueBookInstance -> new CatalogueBookResponse(true, "Success!", catalogueBookInstance))
                .getOrElseGet(cause -> new CatalogueBookResponse(false, cause.getMessage(), null));
    }
}
