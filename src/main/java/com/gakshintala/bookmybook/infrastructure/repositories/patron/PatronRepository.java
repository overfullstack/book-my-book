package com.gakshintala.bookmybook.infrastructure.repositories.patron;


import com.gakshintala.bookmybook.adapters.db.PatronDomainModelMapper;
import com.gakshintala.bookmybook.core.domain.common.DomainEvents;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.ports.repositories.patron.FindPatron;
import com.gakshintala.bookmybook.core.ports.repositories.patron.PersistPatron;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
class PatronRepository implements FindPatron, PersistPatron {
    private final PatronEntityRepository patronEntityRepository;
    private final DomainEvents domainEvents;

    @Override
    public Option<Patron> findBy(PatronId patronId) {
        return Option.of(patronEntityRepository
                .findByPatronId(patronId.getPatronId()))
                .map(PatronDomainModelMapper::toDomainModel);
    }

    @Override
    public Patron persist(PatronInformation patronInformation) {
        PatronDatabaseEntity entity = patronEntityRepository
                .save(new PatronDatabaseEntity(patronInformation.getPatronId(), patronInformation.getType()));
        return PatronDomainModelMapper.toDomainModel(entity);
    }

}

interface PatronEntityRepository extends CrudRepository<PatronDatabaseEntity, Long> {

    @Query("SELECT p.* FROM patron_database_entity p where p.patron_id = :patronId")
    PatronDatabaseEntity findByPatronId(@Param("patronId") UUID patronId);

}

