package com.gakshintala.bookmybook.core.ports.controllers;

import com.gakshintala.bookmybook.adapters.rest.compound.request.AddBookEverywhereRequest;
import com.gakshintala.bookmybook.adapters.rest.compound.request.PlaceBookOnHoldCompoundRequest;
import com.gakshintala.bookmybook.adapters.rest.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronEventResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/compound")
public interface CompoundResource {
    
    @PostMapping("/add-book-everywhere")
    CompletableFuture<AddBookToLibraryResponse> addBookEverywhere(
            @Valid @RequestBody AddBookEverywhereRequest addBookEverywhereRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/place-on-hold")
    CompletableFuture<PatronEventResponse> placeOnHoldCompound(
            @Valid @RequestBody PlaceBookOnHoldCompoundRequest placeBookOnHoldCompoundRequest, HttpServletRequest httpServletRequest);
    
}
