package com.gakshintala.bookmybook.infrastructure.repositories.patron;


import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;
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
    //TODO 2019-04-16 gakshintala: Make these collections immutable by using vavr HashSet
    Set<HoldDatabaseEntity> booksOnHold;
    Set<OverdueCheckoutDatabaseEntity> checkouts;

    PatronDatabaseEntity(PatronId patronId, PatronType patronType) {
        this.patronId = patronId.getPatronId();
        this.patronType = patronType;
        this.booksOnHold = new HashSet<>();
        this.checkouts = new HashSet<>();
    }
}


