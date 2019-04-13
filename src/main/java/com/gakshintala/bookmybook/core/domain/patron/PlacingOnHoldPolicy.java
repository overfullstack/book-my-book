package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.library.AvailableBook;
import io.vavr.Function3;
import io.vavr.control.Either;

@FunctionalInterface
public interface PlacingOnHoldPolicy extends Function3<AvailableBook, Patron, HoldDuration, Either<Rejection, Allowance>> {
}

