package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookEverywhereCommand;
import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.adapters.rest.book.response.LibraryBookResponse;
import com.gakshintala.bookmybook.core.usecases.AddBookEverywhere;
import com.gakshintala.bookmybook.core.usecases.AddBookToLibrary;
import com.gakshintala.bookmybook.core.usecases.UseCaseExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class LibraryController implements LibraryResource{
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookToLibrary addBookToLibrary;
    private final AddBookEverywhere addBookEverywhere;
    
    @Override
    public CompletableFuture<LibraryBookResponse> addBookToLibrary(@Valid AddBookToLibraryRequest addBookToLibraryRequest,
                                                                   HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToLibrary,
                addBookToLibraryRequest.toInput(),
                LibraryBookResponse::fromResult
        );
    }

    @Override
    public CompletableFuture<LibraryBookResponse> addBookEverywhere(@Valid AddBookEverywhereCommand addBookEverywhereCommand, HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookEverywhere,
                addBookEverywhereCommand.toInput(),
                LibraryBookResponse::fromResult
        );
    }
}
