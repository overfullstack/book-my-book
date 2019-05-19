package com.gakshintala.bookmybook.core.domain.patron;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.ToString;

@ToString
public enum PatronType {
    RESEARCHER, REGULAR;

    @JsonCreator
    public static PatronType fromString(String patronType) {
        return PatronType.valueOf(patronType.toUpperCase());
    }
}
