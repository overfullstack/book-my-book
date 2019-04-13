package com.gakshintala.bookmybook.infrastructure.controllers;

import com.gakshintala.bookmybook.adapters.rest.patron.request.CancelHoldRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.CollectBookOnHoldRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.PlaceBookOnHoldRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronResponse;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronEventResponse;
import com.gakshintala.bookmybook.core.ports.controllers.PatronResource;
import com.gakshintala.bookmybook.core.usecases.*;
import com.gakshintala.bookmybook.core.usecases.patron.CreatePatron;
import com.gakshintala.bookmybook.core.usecases.patron.PatronCancelHold;
import com.gakshintala.bookmybook.core.usecases.patron.PatronCollectBookOnHold;
import com.gakshintala.bookmybook.core.usecases.patron.PatronPlaceBookOnHold;
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
    private final PatronPlaceBookOnHold patronPlaceBookOnHold;
    private final PatronCollectBookOnHold patronCollectBookOnHold;
    private final PatronCancelHold patronCancelHold;

    @Override
    public CompletableFuture<PatronResponse> createPatron(@Valid CreatePatronRequest createPatronRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                createPatron,
                createPatronRequest.toCommand(),
                PatronResponse::fromResult
        );
    }

    @Override
    public CompletableFuture<PatronEventResponse> placeOnHold(@Valid PlaceBookOnHoldRequest placeBookOnHoldRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                patronPlaceBookOnHold,
                placeBookOnHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @Override
    public CompletableFuture<PatronEventResponse> collectOnHold(@Valid CollectBookOnHoldRequest collectBookOnHoldRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                patronCollectBookOnHold,
                collectBookOnHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @Override
    public CompletableFuture<PatronEventResponse> cancelOnHold(@Valid CancelHoldRequest cancelHoldRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                patronCancelHold,
                cancelHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }
}
