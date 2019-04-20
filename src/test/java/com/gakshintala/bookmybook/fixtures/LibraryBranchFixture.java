package com.gakshintala.bookmybook.fixtures;

import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;

import java.util.UUID;

public class LibraryBranchFixture {
    
    public static LibraryBranchId anyBranch() {
        return new LibraryBranchId(UUID.randomUUID());
    }
}
