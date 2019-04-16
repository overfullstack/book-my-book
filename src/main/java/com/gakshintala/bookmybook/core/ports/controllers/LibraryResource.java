package com.gakshintala.bookmybook.core.ports.controllers;

import com.gakshintala.bookmybook.adapters.rest.library.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.adapters.rest.library.response.AddBookToLibraryResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/library")
public interface LibraryResource {

    @PostMapping("/add-book")
    CompletableFuture<AddBookToLibraryResponse> addBookToLibrary(
            @Valid @RequestBody AddBookToLibraryRequest addBookToLibraryRequest, HttpServletRequest httpServletRequest);
    
}
