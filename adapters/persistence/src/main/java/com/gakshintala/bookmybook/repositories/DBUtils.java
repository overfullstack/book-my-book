package com.gakshintala.bookmybook.repositories;

import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@UtilityClass
public class DBUtils {
    public static Try<Integer> insertAndGetGeneratedKey(Function<JdbcTemplate, UnaryOperator<KeyHolder>> jdbcOperation,
                                                        JdbcTemplate jdbcTemplate) {
        return Try.of(GeneratedKeyHolder::new)
                .map(jdbcOperation.apply(jdbcTemplate))
                .map(KeyHolder::getKey)
                .map(Number::intValue);
    }

    public static Try<Integer> performUpdateOperation(Function<JdbcTemplate, Integer> jdbcOperation, JdbcTemplate jdbcTemplate) {
        return Try.of(() -> jdbcOperation.apply(jdbcTemplate));
    }
}
