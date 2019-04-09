package com.gakshintala.bookmybook.core.domain.common;

public class AggregateRootIsStale extends RuntimeException {

    public AggregateRootIsStale(String msg) {
        super(msg);
    }
}
