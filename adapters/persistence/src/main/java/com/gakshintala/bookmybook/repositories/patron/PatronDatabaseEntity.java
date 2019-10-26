package com.gakshintala.bookmybook.repositories.patron;


import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.domain.patron.PatronType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Table(name = "patron_database_entity")
public class PatronDatabaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID patronId;

    @Enumerated(EnumType.STRING)
    private PatronType patronType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<HoldDatabaseEntity> booksOnHold;

    PatronDatabaseEntity(PatronId patronId, PatronType patronType) {
        this.patronId = patronId.getPatronId();
        this.patronType = patronType;
        booksOnHold = new HashSet<>();
    }
}


