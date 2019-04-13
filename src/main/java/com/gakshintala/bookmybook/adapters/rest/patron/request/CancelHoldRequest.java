package com.gakshintala.bookmybook.adapters.rest.patron.request;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.usecases.patron.PatronCancelHold.CancelHoldCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
public class CancelHoldRequest {
    @NotBlank String patronId;
    @NotBlank String catalogueBookInstanceUUID;
    Integer noOfDays;

    public CancelHoldCommand toCommand() {
        return new CancelHoldCommand(
                Instant.now(),
                PatronId.fromString(patronId),
                CatalogueBookInstanceUUID.fromString(catalogueBookInstanceUUID)
        );
    }
}
