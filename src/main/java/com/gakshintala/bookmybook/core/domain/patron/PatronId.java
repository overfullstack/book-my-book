package com.gakshintala.bookmybook.core.domain.patron;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class PatronId {
    @NonNull UUID patronId;

    public static PatronId anyPatronId() {
        return new PatronId(UUID.randomUUID());
    }
    
    public static PatronId fromString(String patronIdStr) {
        return new PatronId(UUID.fromString(patronIdStr));
    }
}
