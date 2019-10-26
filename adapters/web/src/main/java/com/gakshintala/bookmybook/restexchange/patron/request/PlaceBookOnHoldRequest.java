package com.gakshintala.bookmybook.restexchange.patron.request;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.usecases.patron.PatronPlaceBookOnHold.PlaceOnHoldCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
public class PlaceBookOnHoldRequest {
    @NotBlank
    String patronId;
    @NotBlank
    String catalogueBookInstanceId;
    Integer noOfDays;

    public PlaceOnHoldCommand toCommand() {
        return new PlaceOnHoldCommand(
                Instant.now(),
                PatronId.fromString(patronId),
                CatalogueBookInstanceId.fromString(catalogueBookInstanceId),
                HoldDuration.forNoOfDays(noOfDays)
        );
    }
}
