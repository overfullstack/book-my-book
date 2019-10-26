package com.gakshintala.bookmybook.domain.patron;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import lombok.NonNull;
import lombok.Value;

@Value
public class OverdueCheckouts {

    static final int MAX_COUNT_OF_OVERDUE_RESOURCES = 2;

    @NonNull Map<LibraryBranchId, Set<CatalogueBookInstanceId>> overdueCheckouts;

    int countAt(@NonNull LibraryBranchId libraryBranchId) {
        return overdueCheckouts.getOrElse(libraryBranchId, HashSet.empty()).size();
    }

}



