package com.gakshintala.bookmybook.core.usecases;

import io.vavr.control.Try;

@FunctionalInterface
public interface UseCase<I extends UseCase.InputValues, O extends Try> {
    O execute(I input);

    interface InputValues {
    }
}
