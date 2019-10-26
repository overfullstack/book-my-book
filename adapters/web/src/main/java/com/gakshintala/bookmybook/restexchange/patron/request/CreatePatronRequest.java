package com.gakshintala.bookmybook.restexchange.patron.request;

import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.domain.patron.PatronType;
import com.gakshintala.bookmybook.usecases.patron.CreatePatron.CreatePatronCommand;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CreatePatronRequest {
    @NotBlank
    String patronType;

    public CreatePatronCommand toCommand() {
        return new CreatePatronCommand(
                new PatronInformation(
                        PatronId.anyPatronId(),
                        PatronType.fromString(getPatronType())
                )
        );
    }
}
