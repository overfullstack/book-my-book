package com.gakshintala.bookmybook.infrastructure.repositories.patron;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "hold_database_entity")
public class HoldDatabaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID bookId;
    private UUID patronId;
    private UUID libraryBranchId;
    private Instant till;

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
