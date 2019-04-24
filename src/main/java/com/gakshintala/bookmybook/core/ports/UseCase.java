package com.gakshintala.bookmybook.core.ports;

import io.vavr.control.Try;

@FunctionalInterface
public interface UseCase<I, O extends Try> {
    O execute(I input);
}
