package com.gakshintala.bookmybook.ports;

import io.vavr.control.Try;

@FunctionalInterface
public interface UseCase<I, O extends Try> {
    O execute(I input);
}
