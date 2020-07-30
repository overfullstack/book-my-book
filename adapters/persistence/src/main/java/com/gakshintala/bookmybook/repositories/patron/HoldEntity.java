package com.gakshintala.bookmybook.repositories.patron;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Table("hold_entity")
public class HoldEntity {
    private UUID patronId;
    private UUID bookId;
    private UUID libraryBranchId;
    private Instant till;

    boolean is(UUID patronId, UUID bookId, UUID libraryBranchId) {
        return this.patronId.equals(patronId) &&
                this.bookId.equals(bookId) &&
                this.libraryBranchId.equals(libraryBranchId);
    }

}
