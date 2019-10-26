package com.gakshintala.bookmybook.controllers;


import com.gakshintala.bookmybook.ports.UseCaseExecutor;
import com.gakshintala.bookmybook.restexchange.library.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.restexchange.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.usecases.library.AddBookToLibrary;
import lombok.RequiredArgsConstructor;
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
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookToLibrary addBookToLibrary;

    @PostMapping("/add-book")
    public CompletableFuture<AddBookToLibraryResponse> addBookToLibrary(@Valid @RequestBody AddBookToLibraryRequest addBookToLibraryRequest) {
        return useCaseExecutor.execute(
                addBookToLibrary,
                addBookToLibraryRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }
}
