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
    public static Option<String> operateAndGetGeneratedKey(Function<JdbcTemplate, UnaryOperator<KeyHolder>> jdbcOperation, JdbcTemplate jdbcTemplate) {
        return Option.of(new GeneratedKeyHolder())
                .map(jdbcOperation.apply(jdbcTemplate))
                .map(KeyHolder::getKey)
                .map(Number::toString);
    }
}
