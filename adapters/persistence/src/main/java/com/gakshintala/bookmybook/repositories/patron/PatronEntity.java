package com.gakshintala.bookmybook.repositories.patron;


import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.domain.patron.PatronType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@With
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table("patron_entity")
public class PatronEntity {
    @Id
    private Long id;
    private UUID patronId;

    private PatronType patronType;

    private Set<HoldEntity> booksOnHold;

    PatronEntity(PatronId patronId, PatronType patronType) {
        this.patronId = patronId.getPatronId();
        this.patronType = patronType;
        booksOnHold = new HashSet<>();
    }
}


