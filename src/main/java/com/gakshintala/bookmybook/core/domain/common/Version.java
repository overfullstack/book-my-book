package com.gakshintala.bookmybook.core.domain.common;

import lombok.Value;

@Value
public class Version {
    int version;

    public static Version zero() {
        return new Version(0);
    }
}
