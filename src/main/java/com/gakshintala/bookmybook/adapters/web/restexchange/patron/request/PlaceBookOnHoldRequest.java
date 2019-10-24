package com.gakshintala.bookmybook.adapters.web.restexchange.patron.request;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.usecases.patron.PatronPlaceBookOnHold.PlaceOnHoldCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
public class PlaceBookOnHoldRequest {
    @NotBlank private String patronId;
    @NotBlank
    private String catalogueBookInstanceId;
    private Integer noOfDays;

    public PlaceOnHoldCommand toCommand() {
        return new PlaceOnHoldCommand(
                Instant.now(),
                PatronId.fromString(patronId),
                CatalogueBookInstanceId.fromString(catalogueBookInstanceId),
                HoldDuration.forNoOfDays(noOfDays)
        );
    }
}
