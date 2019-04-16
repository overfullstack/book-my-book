package com.gakshintala.bookmybook.core.ports.controllers;

import com.gakshintala.bookmybook.adapters.rest.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.CancelHoldRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.CollectBookOnHoldRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.request.PlaceBookOnHoldRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronResponse;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronEventResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/patron")
public interface PatronResource {

    @PostMapping("/create")
    CompletableFuture<PatronResponse> createPatron(
            @Valid @RequestBody CreatePatronRequest createPatronRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/place-on-hold")
    CompletableFuture<PatronEventResponse> placeOnHold(
            @Valid @RequestBody PlaceBookOnHoldRequest placeBookOnHoldRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/collect")
    CompletableFuture<PatronEventResponse> collectOnHold(
            @Valid @RequestBody CollectBookOnHoldRequest collectHoldRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/cancel")
    CompletableFuture<PatronEventResponse> cancelOnHold(
            @Valid @RequestBody CancelHoldRequest cancelHoldRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/compound-place-on-hold")
    CompletableFuture<PatronEventResponse> placeOnHoldCompound(
            @Valid @RequestBody PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest, HttpServletRequest httpServletRequest);
    
}
