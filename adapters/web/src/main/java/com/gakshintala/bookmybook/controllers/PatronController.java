package com.gakshintala.bookmybook.controllers;


import com.gakshintala.bookmybook.UseCaseExecutor;
import com.gakshintala.bookmybook.restexchange.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.restexchange.patron.request.CancelHoldRequest;
import com.gakshintala.bookmybook.restexchange.patron.request.CollectBookOnHoldRequest;
import com.gakshintala.bookmybook.restexchange.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.restexchange.patron.request.PlaceBookOnHoldRequest;
import com.gakshintala.bookmybook.restexchange.patron.response.PatronEventResponse;
import com.gakshintala.bookmybook.restexchange.patron.response.PatronResponse;
import com.gakshintala.bookmybook.usecases.compound.PlaceBookOnHoldCompound;
import com.gakshintala.bookmybook.usecases.patron.CreatePatron;
import com.gakshintala.bookmybook.usecases.patron.PatronCancelBookOnHold;
import com.gakshintala.bookmybook.usecases.patron.PatronCollectBookOnHold;
import com.gakshintala.bookmybook.usecases.patron.PatronPlaceBookOnHold;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/patron")
@RequiredArgsConstructor
public class PatronController {
    private final CreatePatron createPatron;
    private final PatronPlaceBookOnHold patronPlaceBookOnHold;
    private final PatronCollectBookOnHold patronCollectBookOnHold;
    private final PatronCancelBookOnHold patronCancelBookOnHold;
    private final PlaceBookOnHoldCompound placeBookOnHoldCompound;

    @Async
    @PostMapping("/create")
    public CompletableFuture<PatronResponse> createPatron(@Valid @RequestBody CreatePatronRequest createPatronRequest) {
        return UseCaseExecutor.execute(
                createPatron,
                createPatronRequest.toCommand(),
                PatronResponse::fromResult
        );
    }

    @Async
    @PostMapping("/place-on-hold")
    public CompletableFuture<PatronEventResponse> placeOnHold(@Valid @RequestBody PlaceBookOnHoldRequest placeBookOnHoldRequest) {
        return UseCaseExecutor.execute(
                patronPlaceBookOnHold,
                placeBookOnHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @Async
    @PostMapping("/collect")
    public CompletableFuture<PatronEventResponse> collectOnHold(@Valid @RequestBody CollectBookOnHoldRequest collectBookOnHoldRequest) {
        return UseCaseExecutor.execute(
                patronCollectBookOnHold,
                collectBookOnHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @Async
    @PostMapping("/cancel")
    public CompletableFuture<PatronEventResponse> cancelOnHold(@Valid @RequestBody CancelHoldRequest cancelHoldRequest) {
        return UseCaseExecutor.execute(
                patronCancelBookOnHold,
                cancelHoldRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }

    @Async
    @PostMapping("/compound-place-on-hold")
    public CompletableFuture<PatronEventResponse> placeOnHoldCompound(@Valid @RequestBody PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest) {
        return UseCaseExecutor.execute(
                placeBookOnHoldCompound,
                placeBookOnHoldCompoundRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }
}
