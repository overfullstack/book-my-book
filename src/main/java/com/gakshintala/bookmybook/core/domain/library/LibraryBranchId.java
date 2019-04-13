package com.gakshintala.bookmybook.core.domain.library;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class LibraryBranchId {

    @NonNull UUID libraryBranchUUID;

    public static LibraryBranchId randomLibraryId() {
        return new LibraryBranchId(UUID.randomUUID());
    }

    public static LibraryBranchId fromString(String libraryBranchId) {
        return new LibraryBranchId(UUID.fromString(libraryBranchId));
    }
}
