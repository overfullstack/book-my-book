package com.gakshintala.bookmybook.infrastructure.controllers;

import com.gakshintala.bookmybook.adapters.rest.compound.request.AddBookEverywhereRequest;
import com.gakshintala.bookmybook.adapters.rest.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.adapters.rest.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronEventResponse;
import com.gakshintala.bookmybook.core.ports.controllers.CompoundResource;
import com.gakshintala.bookmybook.core.ports.UseCaseExecutor;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere;
import com.gakshintala.bookmybook.core.usecases.compound.PlaceBookOnHoldCompound;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class CompoundController implements CompoundResource {
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookEverywhere addBookEverywhere;
    private final PlaceBookOnHoldCompound placeBookOnHoldCompound;

    @Override
    public CompletableFuture<AddBookToLibraryResponse> addBookEverywhere(@Valid AddBookEverywhereRequest addBookEverywhereRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookEverywhere,
                addBookEverywhereRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }
    
    @Override
    public CompletableFuture<PatronEventResponse> placeOnHoldCompound(@Valid PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                placeBookOnHoldCompound,
                placeBookOnHoldCompoundRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }
}
