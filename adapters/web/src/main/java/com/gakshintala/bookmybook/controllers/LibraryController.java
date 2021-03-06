package com.gakshintala.bookmybook.controllers;


import com.gakshintala.bookmybook.UseCaseExecutor;
import com.gakshintala.bookmybook.restexchange.library.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.restexchange.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.usecases.library.AddBookToLibrary;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {
    private final AddBookToLibrary addBookToLibrary;

    @Async
    @PostMapping("/add-book")
    public CompletableFuture<AddBookToLibraryResponse> addBookToLibrary(@Valid @RequestBody AddBookToLibraryRequest addBookToLibraryRequest) {
        return UseCaseExecutor.execute(
                addBookToLibrary,
                addBookToLibraryRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }
}
