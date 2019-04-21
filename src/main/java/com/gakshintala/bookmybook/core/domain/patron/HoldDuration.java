package com.gakshintala.bookmybook.core.domain.patron;

import io.vavr.control.Option;
import lombok.Value;

import java.time.Duration;
import java.time.Instant;

@Value
public class HoldDuration {

    Instant from;
    Instant to;

    private HoldDuration(Instant from, Instant to) {
        if (to != null && to.isBefore(from)) {
            throw new IllegalStateException("Close-ended duration must be valid");
        }
        this.from = from;
        this.to = to;
    }

    boolean isOpenEnded() {
        return getTo().isEmpty();
    }

    Option<Instant> getTo() {
        return Option.of(to);
    }

    public static HoldDuration openEnded() {
        return openEnded(Instant.now());
    }

    private static HoldDuration openEnded(Instant from) {
        return new HoldDuration(from, null);
    }

    private static HoldDuration closeEnded(NumberOfDays days) {
        return closeEnded(Instant.now(), days);
    }

    public static HoldDuration closeEnded(int days) {
        return closeEnded(Instant.now(), NumberOfDays.of(days));
    }

    private static HoldDuration closeEnded(Instant from, NumberOfDays days) {
        Instant till = from.plus(Duration.ofDays(days.getDays()));
        return new HoldDuration(from, till);
    }
    
    public static HoldDuration forNoOfDays(Integer noOfDays) {
        return Option.of(noOfDays)
                .map(NumberOfDays::of)
                .map(HoldDuration::closeEnded)
                .getOrElse(HoldDuration.openEnded());
    }

}


