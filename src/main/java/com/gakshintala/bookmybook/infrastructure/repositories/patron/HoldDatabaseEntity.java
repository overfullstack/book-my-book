package com.gakshintala.bookmybook.infrastructure.repositories.patron;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
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
