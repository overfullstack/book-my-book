package com.gakshintala.bookmybook.core.usecases;

import io.vavr.control.Try;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface UseCaseExecutor {
    <RX, I extends UseCase.InputValues, O extends Try> CompletableFuture<RX> execute(
            UseCase<I, O> useCase,
            I input,
            Function<O, RX> outputMapper);
}
