package com.gakshintala.bookmybook.adapters.web.controllers;

import com.gakshintala.bookmybook.adapters.web.restexchange.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.request.CancelHoldRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.request.CollectBookOnHoldRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.request.PlaceBookOnHoldRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.response.PatronEventResponse;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.response.PatronResponse;
import com.gakshintala.bookmybook.core.ports.UseCaseExecutor;
import com.gakshintala.bookmybook.core.usecases.compound.PlaceBookOnHoldCompound;
import com.gakshintala.bookmybook.core.usecases.patron.CreatePatron;
import com.gakshintala.bookmybook.core.usecases.patron.PatronCancelBookOnHold;
import com.gakshintala.bookmybook.core.usecases.patron.PatronCollectBookOnHold;
import com.gakshintala.bookmybook.core.usecases.patron.PatronPlaceBookOnHold;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/patron")
@RequiredArgsConstructor
public class PatronController {
    private final UseCaseExecutor useCaseExecutor;
    private final CreatePatron createPatron;
    private final PatronPlaceBookOnHold patronPlaceBookOnHold;
    private final PatronCollectBookOnHold patronCollectBookOnHold;
    private final PatronCancelBookOnHold patronCancelBookOnHold;
    private final PlaceBookOnHoldCompound placeBookOnHoldCompound;

    @PostMapping("/create")
    public CompletableFuture<PatronResponse> createPatron(@Valid CreatePatronRequest createPatronRequest) {
        return useCaseExecutor.execute(
                createPatron,
                createPatronRequest.toCommand(),
                PatronResponse::fromResult
        );
    }

    @PostMapping("/place-on-hold")
    public CompletableFuture<PatronEventResponse> placeOnHold(@Valid PlaceBookOnHoldRequest placeBookOnHoldRequest) {
        return useCaseExecutor.execute(
                patronPlaceBookOnHold,
                placeBookOnHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @PostMapping("/collect")
    public CompletableFuture<PatronEventResponse> collectOnHold(@Valid CollectBookOnHoldRequest collectBookOnHoldRequest) {
        return useCaseExecutor.execute(
                patronCollectBookOnHold,
                collectBookOnHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @PostMapping("/cancel")
    public CompletableFuture<PatronEventResponse> cancelOnHold(@Valid CancelHoldRequest cancelHoldRequest) {
        return useCaseExecutor.execute(
                patronCancelBookOnHold,
                cancelHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @PostMapping("/compound-place-on-hold")
    public CompletableFuture<PatronEventResponse> placeOnHoldCompound(@Valid PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest) {
        return useCaseExecutor.execute(
                placeBookOnHoldCompound,
                placeBookOnHoldCompoundRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }
}
