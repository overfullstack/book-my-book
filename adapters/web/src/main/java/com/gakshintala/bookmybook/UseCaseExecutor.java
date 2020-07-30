package com.gakshintala.bookmybook;

import com.gakshintala.bookmybook.ports.UseCase;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@UtilityClass
public class UseCaseExecutor {
    public static <ResponseT, I, O extends Try> CompletableFuture<ResponseT> execute(
            UseCase<I, O> useCase,
            I input,
            Function<O, ResponseT> outputMapper) {
        return CompletableFuture
                .supplyAsync(() -> input)
                .thenApply(useCase::execute)
                .thenApply(outputMapper);
    }
}
