package com.gakshintala.bookmybook.core.usecases.patron;

import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.ports.repositories.patron.PersistPatron;
import com.gakshintala.bookmybook.core.usecases.UseCase;
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
        return Try.of(() -> persistPatron.persist(command.getPatronInformation()));
    }

    @Value
    public static class CreatePatronCommand {
        private final PatronInformation patronInformation;
    }
    
}
