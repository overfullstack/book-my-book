package com.gakshintala.bookmybook.repositories;

import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.function.UnaryOperator;

@UtilityClass
public class DBUtils {
    public static Try<Integer> insertAndGetGeneratedKey(UnaryOperator<KeyHolder> insert) {
        return Try.of(GeneratedKeyHolder::new)
                .map(insert)
                .map(KeyHolder::getKey)
                .map(Number::intValue);
    }
}
