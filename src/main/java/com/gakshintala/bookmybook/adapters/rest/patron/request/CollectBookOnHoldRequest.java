package com.gakshintala.bookmybook.adapters.rest.patron.request;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.CheckoutDuration;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.usecases.patron.PatronCollectBookOnHold;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
public class CollectBookOnHoldRequest {
    @NotBlank String patronId;
    @NotBlank String libraryBranchId;
    @NotBlank String catalogueBookInstanceUUID;
    Integer noOfDays;

    public PatronCollectBookOnHold.CollectBookCommand toCommand() {
        return new PatronCollectBookOnHold.CollectBookCommand(
                Instant.now(),
                PatronId.fromString(patronId),
                LibraryBranchId.fromString(libraryBranchId),
                CatalogueBookInstanceUUID.fromString(catalogueBookInstanceUUID),
                CheckoutDuration.forNoOfDays(noOfDays)
        );
    }
}
