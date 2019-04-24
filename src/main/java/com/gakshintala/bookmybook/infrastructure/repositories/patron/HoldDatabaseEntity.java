package com.gakshintala.bookmybook.infrastructure.repositories.patron;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "hold_database_entity")
@NoArgsConstructor
public class HoldDatabaseEntity {

    @Id
    Long id;
    UUID patronId;
    UUID bookId;
    UUID libraryBranchId;
    Instant till;

    HoldDatabaseEntity(UUID bookId, UUID patronId, UUID libraryBranchId, Instant till) {
        this.bookId = bookId;
        this.patronId = patronId;
        this.libraryBranchId = libraryBranchId;
        this.till = till;
    }

    boolean is(UUID patronId, UUID bookId, UUID libraryBranchId) {
        return  this.patronId.equals(patronId) &&
                this.bookId.equals(bookId) &&
                this.libraryBranchId.equals(libraryBranchId);
    }

}
