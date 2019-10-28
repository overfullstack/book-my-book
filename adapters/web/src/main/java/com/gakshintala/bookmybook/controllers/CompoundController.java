package com.gakshintala.bookmybook.controllers;

import com.gakshintala.bookmybook.UseCaseExecutor;
import com.gakshintala.bookmybook.restexchange.compound.request.AddBookEverywhereRequest;
import com.gakshintala.bookmybook.restexchange.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.restexchange.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.restexchange.patron.response.PatronEventResponse;
import com.gakshintala.bookmybook.usecases.compound.AddBookEverywhere;
import com.gakshintala.bookmybook.usecases.compound.PlaceBookOnHoldCompound;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/compound")
@RequiredArgsConstructor
public class CompoundController {
    private final AddBookEverywhere addBookEverywhere;
    private final PlaceBookOnHoldCompound placeBookOnHoldCompound;

    @PostMapping("/add-book-everywhere")
    public CompletableFuture<AddBookToLibraryResponse> addBookEverywhere(@Valid @RequestBody AddBookEverywhereRequest addBookEverywhereRequest) {
        return UseCaseExecutor.execute(
                addBookEverywhere,
                addBookEverywhereRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }

    @PostMapping("/place-on-hold")
    public CompletableFuture<PatronEventResponse> placeOnHoldCompound(@Valid @RequestBody PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest) {
        return UseCaseExecutor.execute(
                placeBookOnHoldCompound,
                placeBookOnHoldCompoundRequest.toCommand(),
                PatronEventResponse::fromResult
        );
    }
}
