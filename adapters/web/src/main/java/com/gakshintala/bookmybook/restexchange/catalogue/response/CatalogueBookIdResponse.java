package com.gakshintala.bookmybook.restexchange.catalogue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookId;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogueBookIdResponse {
    Boolean success;
    String message;
    Tuple2<CatalogueBookId, CatalogueBook> catalogueBookInfo;

    public static CatalogueBookIdResponse fromResult(Try<Tuple2<CatalogueBookId, CatalogueBook>> result) {
        return result.map(catalogueBookInfo -> new CatalogueBookIdResponse(true, "Success!", catalogueBookInfo))
                .getOrElseGet(cause -> new CatalogueBookIdResponse(false, cause.getMessage(), null));
    }
}
