package com.gakshintala.bookmybook.core.domain.catalogue;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class BookId {

    @NonNull
    UUID bookId;
}
