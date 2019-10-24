package com.gakshintala.bookmybook.adapters.web.restexchange.patron.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookCollectingFailed;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldCancelingFailed;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookHoldFailed;
import io.vavr.Tuple3;
import io.vavr.control.Try;
import lombok.Value;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatronEventResponse {
    private final Boolean success;
    private final String message;
    private final Tuple3<PatronEvent, Patron, CatalogueBookInstanceId> holdInfo;

    public static PatronEventResponse fromResult(Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> result) {
        return result
                .map(tuple3 -> Match(tuple3._1()).of(
                        Case($(instanceOf(BookHoldFailed.class)), () -> new PatronEventResponse(false, "Hold Failed", tuple3)),
                        Case($(instanceOf(BookCollectingFailed.class)), () -> new PatronEventResponse(false, "Collecting Book Failed", tuple3)),
                        Case($(instanceOf(BookHoldCancelingFailed.class)), () -> new PatronEventResponse(false, "Cancelling Book Failed", tuple3)),
                        Case($(), () -> new PatronEventResponse(true, "Success!", tuple3))))
                .getOrElseGet(cause -> new PatronEventResponse(false, cause.getMessage(), null));
    }
}
