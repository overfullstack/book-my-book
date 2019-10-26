package com.gakshintala.bookmybook.restexchange.patron.request;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.usecases.patron.PatronCancelBookOnHold.CancelHoldCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
public class CancelHoldRequest {
    @NotBlank String patronId;
    @NotBlank String catalogueBookInstanceId;

    public CancelHoldCommand toCommand() {
        return new CancelHoldCommand(
                Instant.now(),
                PatronId.fromString(patronId),
                CatalogueBookInstanceId.fromString(catalogueBookInstanceId)
        );
    }
}
