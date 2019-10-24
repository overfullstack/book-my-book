package com.gakshintala.bookmybook.adapters.web.controllers;

import com.gakshintala.bookmybook.adapters.web.restexchange.compound.request.AddBookEverywhereRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.adapters.web.restexchange.patron.response.PatronEventResponse;
import com.gakshintala.bookmybook.core.ports.UseCaseExecutor;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere;
import com.gakshintala.bookmybook.core.usecases.compound.PlaceBookOnHoldCompound;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/compound")
@RequiredArgsConstructor
public class CompoundController {
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookEverywhere addBookEverywhere;
    private final PlaceBookOnHoldCompound placeBookOnHoldCompound;

    @PostMapping("/add-book-everywhere")
    public CompletableFuture<AddBookToLibraryResponse> addBookEverywhere(@Valid AddBookEverywhereRequest addBookEverywhereRequest) {
        return useCaseExecutor.execute(
                addBookEverywhere,
                addBookEverywhereRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }

    @PostMapping("/place-on-hold")
    public CompletableFuture<PatronEventResponse> placeOnHoldCompound(@Valid PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest) {
        return useCaseExecutor.execute(
                placeBookOnHoldCompound,
                placeBookOnHoldCompoundRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }
}
