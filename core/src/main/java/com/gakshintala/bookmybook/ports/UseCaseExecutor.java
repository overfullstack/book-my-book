package com.gakshintala.bookmybook.ports;

import io.vavr.control.Try;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface UseCaseExecutor {
    <ResponseT, I, O extends Try> CompletableFuture<ResponseT> execute(
            UseCase<I, O> useCase,
            I input,
            Function<O, ResponseT> outputMapper);
}
