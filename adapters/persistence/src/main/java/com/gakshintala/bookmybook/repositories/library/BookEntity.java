package com.gakshintala.bookmybook.repositories.library;


import com.gakshintala.bookmybook.domain.catalogue.BookType;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class BookEntity {

    @Id
    int id;

    public enum BookState {
        AVAILABLE, ON_HOLD, COLLECTED
    }

    UUID book_id;
    BookType book_type;
    BookState book_state;
    UUID available_at_branch;
    UUID on_hold_at_branch;
    UUID on_hold_by_patron;
    Instant on_hold_till;
    UUID collected_at_branch;
    UUID collected_by_patron;
    int version;

}

