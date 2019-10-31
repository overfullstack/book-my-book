package com.gakshintala.bookmybook.repositories.patron;

import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.ports.persistence.patron.FindPatron;
import com.gakshintala.bookmybook.ports.persistence.patron.PersistPatron;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;

import static com.gakshintala.bookmybook.fixtures.PatronFixture.regularPatron;

@DataJdbcTest
@ComponentScan
class PatronRepositoryTest {

    @Autowired
    FindPatron findPatron;
    @Autowired
    PersistPatron persistPatron;

    @Test
    void persist() {
        PatronInformation patronInformation = regularPatron().getPatronInformation();
        persistPatron.persist(patronInformation);
        final var foundPatron = findPatron.findBy(patronInformation.getPatronId());
        Assertions.assertEquals(foundPatron.get().getPatronInformation().getType(), patronInformation.getType());
    }

    @Test
    void persistWithHold() {

    }
}