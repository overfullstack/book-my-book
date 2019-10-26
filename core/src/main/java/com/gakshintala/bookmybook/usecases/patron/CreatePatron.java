package com.gakshintala.bookmybook.usecases.patron;

import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.ports.persistence.patron.PersistPatron;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePatron implements UseCase<CreatePatron.CreatePatronCommand, Try<Patron>> {
    private final PersistPatron persistPatron;

    @Override
    public Try<Patron> execute(CreatePatronCommand command) {
        return persistPatron.persist(command.getPatronInformation());
    }

    @Value
    public static class CreatePatronCommand {
        private final PatronInformation patronInformation;
    }

}
