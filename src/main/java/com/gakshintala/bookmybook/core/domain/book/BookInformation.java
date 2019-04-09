package com.gakshintala.bookmybook.core.domain.book;


import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import lombok.NonNull;
import lombok.Value;

@Value
class BookInformation {

    @NonNull
    BookId bookId;

    @NonNull
    BookType bookType;
}
