package com.gakshintala.bookmybook.restexchange.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookId;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookResponse {
    Boolean success;
    String message;
    Tuple2<CatalogueBookId, CatalogueBook> catalogueBookInfo;

    public static CatalogueBookResponse fromResult(Try<Tuple2<CatalogueBookId, CatalogueBook>> result) {
        return result.map(catalogueBookInfo -> new CatalogueBookResponse(true, "Success!", catalogueBookInfo))
                .getOrElseGet(cause -> new CatalogueBookResponse(false, cause.getMessage(), null));
    }
}
