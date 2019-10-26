package com.gakshintala.bookmybook.fixtures;


import com.gakshintala.bookmybook.domain.library.LibraryBranchId;

import java.util.UUID;

class LibraryBranchFixture {

    public static LibraryBranchId anyBranch() {
        return new LibraryBranchId(UUID.randomUUID());
    }
}
