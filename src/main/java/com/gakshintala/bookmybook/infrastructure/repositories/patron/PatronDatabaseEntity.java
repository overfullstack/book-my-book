package com.gakshintala.bookmybook.infrastructure.repositories.patron;


import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class PatronDatabaseEntity {
    @Id
    Long id;
    UUID patronId;
    PatronType patronType;
    Set<HoldDatabaseEntity> booksOnHold;
    Set<OverdueCheckoutDatabaseEntity> checkouts;

    PatronDatabaseEntity(PatronId patronId, PatronType patronType) {
        this.patronId = patronId.getPatronId();
        this.patronType = patronType;
        this.booksOnHold = HashSet.empty();
        this.checkouts = HashSet.empty();
    }
}


