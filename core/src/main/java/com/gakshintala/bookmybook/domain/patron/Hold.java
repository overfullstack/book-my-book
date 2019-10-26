package com.gakshintala.bookmybook.domain.patron;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import lombok.NonNull;
import lombok.Value;

@Value
public class Hold {

    @NonNull CatalogueBookInstanceId catalogueBookInstanceId;
    @NonNull LibraryBranchId libraryBranchId;

}
