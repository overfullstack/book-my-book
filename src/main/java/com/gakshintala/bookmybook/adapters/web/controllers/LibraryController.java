package com.gakshintala.bookmybook.adapters.web.controllers;

import com.gakshintala.bookmybook.adapters.web.restexchange.library.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.core.ports.UseCaseExecutor;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere;
import com.gakshintala.bookmybook.core.usecases.library.AddBookToLibrary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final AddBookEverywhere addBookEverywhere;

    @PostMapping("/add-book")
    public CompletableFuture<AddBookToLibraryResponse> addBookToLibrary(@Valid AddBookToLibraryRequest addBookToLibraryRequest) {
        return useCaseExecutor.execute(
                addBookToLibrary,
                addBookToLibraryRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }
}
