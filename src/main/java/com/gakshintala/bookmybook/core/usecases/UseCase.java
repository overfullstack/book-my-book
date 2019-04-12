package com.gakshintala.bookmybook.core.usecases;

import io.vavr.control.Try;

@FunctionalInterface
public interface UseCase<I, O extends Try> {
    O execute(I input);
}
