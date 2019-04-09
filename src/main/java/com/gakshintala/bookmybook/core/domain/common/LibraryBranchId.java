package com.gakshintala.bookmybook.core.domain.common;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class LibraryBranchId {

    @NonNull UUID libraryBranchId;
}
