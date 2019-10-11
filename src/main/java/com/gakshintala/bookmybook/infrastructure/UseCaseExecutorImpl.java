package com.gakshintala.bookmybook.infrastructure;

import com.gakshintala.bookmybook.core.ports.UseCase;
import com.gakshintala.bookmybook.core.ports.UseCaseExecutor;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class UseCaseExecutorImpl implements UseCaseExecutor {
    @Override
    public <ResponseT, I, O extends Try> CompletableFuture<ResponseT> execute(
            UseCase<I, O> useCase, I input,
            Function<O, ResponseT> outputMapper) {
        return CompletableFuture
                .supplyAsync(() -> input)
                .thenApplyAsync(useCase::execute)
                .thenApplyAsync(outputMapper);
    }
}
