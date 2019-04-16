package com.gakshintala.bookmybook.adapters.rest.patron.request;

import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import com.gakshintala.bookmybook.core.usecases.patron.CreatePatron.CreatePatronCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CreatePatronRequest {
    @NotBlank private final String patronType;

    public CreatePatronCommand toCommand() {
        return new CreatePatronCommand(
                new PatronInformation(
                        PatronId.anyPatronId(),
                        PatronType.fromString(getPatronType())
                )
        );
    }
}
