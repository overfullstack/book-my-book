package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

@Value
class OverdueCheckouts {

    static int MAX_COUNT_OF_OVERDUE_RESOURCES = 2;

    @NonNull Map<LibraryBranchId, Set<BookId>> overdueCheckouts;

    int countAt(@NonNull LibraryBranchId libraryBranchId) {
        return overdueCheckouts.getOrDefault(libraryBranchId, emptySet()).size();
    }

}



