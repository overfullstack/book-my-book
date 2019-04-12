package com.gakshintala.bookmybook.core.domain.library;

import com.gakshintala.bookmybook.core.domain.common.DomainEvent;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;


@Value
public class BookDuplicateHoldFound implements DomainEvent {
    @NonNull UUID eventId = UUID.randomUUID();
    @NonNull Instant when;
    @NonNull UUID firstPatronId;
    @NonNull UUID secondPatronId;
    @NonNull UUID libraryBranchId;
    @NonNull UUID bookId;

    @Override
    public UUID getAggregateId() {
        return bookId;
    }
}