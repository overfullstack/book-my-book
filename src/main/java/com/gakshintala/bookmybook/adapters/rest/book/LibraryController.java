package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.adapters.rest.book.response.LibraryBookResponse;
import com.gakshintala.bookmybook.core.usecases.AddBookToLibraryUseCase;
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
    private final AddBookToLibraryUseCase addBookToLibraryUseCase;
    
    @Override
    public CompletableFuture<LibraryBookResponse> addBookToLibrary(@Valid AddBookToLibraryRequest addBookToLibraryRequest,
                                                                   HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToLibraryUseCase,
                addBookToLibraryRequest.toInput(),
                outputValues -> LibraryBookResponse.fromResult(outputValues.getBook())
        );
    }
}
