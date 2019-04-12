package com.gakshintala.bookmybook.infrastructure.controllers;

import com.gakshintala.bookmybook.adapters.rest.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronResponse;
import com.gakshintala.bookmybook.core.ports.controllers.PatronResource;
import com.gakshintala.bookmybook.core.usecases.CreatePatron;
import com.gakshintala.bookmybook.core.usecases.UseCaseExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class PatronController implements PatronResource {
    private final UseCaseExecutor useCaseExecutor;
    private final CreatePatron createPatron;

    @Override
    public CompletableFuture<PatronResponse> createPatron(@Valid CreatePatronRequest createPatronRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                createPatron,
                createPatronRequest.toCommand(),
                PatronResponse::fromResult
        );
    }
}
