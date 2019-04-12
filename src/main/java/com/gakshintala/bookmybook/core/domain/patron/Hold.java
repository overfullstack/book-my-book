package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import lombok.NonNull;
import lombok.Value;

@Value
public class Hold {

    @NonNull CatalogueBookInstanceUUID catalogueBookInstanceUUID;
    @NonNull LibraryBranchId libraryBranchId;

}
