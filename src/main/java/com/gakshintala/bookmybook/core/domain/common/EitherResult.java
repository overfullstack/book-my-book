package com.gakshintala.bookmybook.core.domain.common;

import io.vavr.control.Either;
import lombok.experimental.UtilityClass;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@UtilityClass
public class EitherResult {

    public static <L, R> Either<L, R> failure(L left) {
        return left(left);
    }

    public static <L, R> Either<L, R> success(R right) {
        return right(right);
    }
}
