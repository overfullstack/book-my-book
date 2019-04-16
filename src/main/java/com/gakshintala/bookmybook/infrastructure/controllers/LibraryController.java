package com.gakshintala.bookmybook.infrastructure.controllers;

import com.gakshintala.bookmybook.adapters.rest.compound.request.AddBookEverywhereRequest;
import com.gakshintala.bookmybook.adapters.rest.library.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.adapters.rest.library.response.AddBookToLibraryResponse;
import com.gakshintala.bookmybook.core.ports.controllers.LibraryResource;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere;
import com.gakshintala.bookmybook.core.usecases.library.AddBookToLibrary;
import com.gakshintala.bookmybook.core.usecases.UseCaseExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class LibraryController implements LibraryResource {
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookToLibrary addBookToLibrary;
    private final AddBookEverywhere addBookEverywhere;
    
    @Override
    public CompletableFuture<AddBookToLibraryResponse> addBookToLibrary(@Valid AddBookToLibraryRequest addBookToLibraryRequest,
                                                                        HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToLibrary,
                addBookToLibraryRequest.toCommand(),
                AddBookToLibraryResponse::fromResult
        );
    }
}
