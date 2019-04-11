package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookInstanceToCatalogueCommand;
import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.book.response.ApiResponse;
import com.gakshintala.bookmybook.adapters.rest.book.response.CatalogueBookResponse;
import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.core.usecases.AddBookToCatalogue;
import com.gakshintala.bookmybook.core.usecases.UseCaseExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class CatalogueController implements CatalogueResource {
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;

    @Override
    public CompletableFuture<ResponseEntity<ApiResponse>> addBookToCatalogue(@Valid AddBookToCatalogueRequest addBookToCatalogueRequest,
                                                                             HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToCatalogue,
                addBookToCatalogueRequest.toInput(),
                ApiResponse::fromResult
        );
    }

    @Override
    public CompletableFuture<CatalogueBookResponse> addBookInstanceToCatalogue(@Valid AddBookInstanceToCatalogueCommand addBookInstanceToCatalogueCommand,
                                                                               HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookInstanceToCatalogue,
                addBookInstanceToCatalogueCommand.toInput(),
                CatalogueBookResponse::fromResult
        );
    }
}
