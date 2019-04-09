package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import lombok.NonNull;
import lombok.Value;

@Value
class Hold {

    @NonNull BookId bookId;
    @NonNull LibraryBranchId libraryBranchId;

}
