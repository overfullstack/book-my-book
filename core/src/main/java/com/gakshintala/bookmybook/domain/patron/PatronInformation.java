package com.gakshintala.bookmybook.domain.patron;

import lombok.NonNull;
import lombok.Value;

import static com.gakshintala.bookmybook.domain.patron.PatronType.REGULAR;


@Value
public class PatronInformation {

    @NonNull PatronId patronId;

    @NonNull PatronType type;

    boolean isRegular() {
        return REGULAR.equals(type);
    }

}

