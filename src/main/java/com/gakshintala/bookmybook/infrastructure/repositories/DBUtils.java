package com.gakshintala.bookmybook.infrastructure.repositories;

import io.vavr.control.Option;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@UtilityClass
public class DBUtils {
    public static Option<String> insertAndGetGeneratedKey(Function<JdbcTemplate, UnaryOperator<KeyHolder>> jdbcOperation, JdbcTemplate jdbcTemplate) {
        return Option.of(new GeneratedKeyHolder())
                .map(jdbcOperation.apply(jdbcTemplate))
                .map(KeyHolder::getKey)
                .map(Number::toString);
    }

    public static int performUpdateOperation(Function<JdbcTemplate, Integer> jdbcOperation, JdbcTemplate jdbcTemplate) {
        return jdbcOperation.apply(jdbcTemplate);
    }
}
