package com.gakshintala.bookmybook.adapters.persistence.repositories.patron;


import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Wither
@AllArgsConstructor
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
    @ElementCollection
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "patron_database_entity_id", referencedColumnName = "id")
    private Set<HoldDatabaseEntity> booksOnHold;

    PatronDatabaseEntity(PatronId patronId, PatronType patronType) {
        this.patronId = patronId.getPatronId();
        this.patronType = patronType;
        booksOnHold = new HashSet<>();
    }
}


