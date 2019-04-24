package com.gakshintala.bookmybook.infrastructure.repositories.library;


import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "book_database_entity")
@NoArgsConstructor
public class BookDatabaseEntity {

    public enum BookState {
        Available, OnHold, Collected
    }
    
    int id;
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

