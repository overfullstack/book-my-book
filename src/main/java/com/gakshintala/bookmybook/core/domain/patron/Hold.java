package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import lombok.NonNull;
import lombok.Value;

@Value
public class Hold {

    @NonNull CatalogueBookInstanceId catalogueBookInstanceId;
    @NonNull LibraryBranchId libraryBranchId;

}
