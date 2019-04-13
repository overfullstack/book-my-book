package com.gakshintala.bookmybook.adapters.rest.patron.request;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.usecases.patron.PatronPlaceBookOnHold.PlaceOnHoldCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
public class PlaceBookOnHoldRequest {
    @NotBlank String patronId;
    @NotBlank String libraryBranchId;
    @NotBlank String catalogueBookInstanceUUID;
    Integer noOfDays;

    public PlaceOnHoldCommand toCommand() {
        return new PlaceOnHoldCommand(
                Instant.now(),
                PatronId.fromString(patronId),
                LibraryBranchId.fromString(libraryBranchId),
                CatalogueBookInstanceUUID.fromString(catalogueBookInstanceUUID),
                HoldDuration.forNoOfDays(noOfDays)
        );
    }
}
