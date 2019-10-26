package com.gakshintala.bookmybook.usecaseexecutor;

import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.ports.UseCaseExecutor;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Component
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
