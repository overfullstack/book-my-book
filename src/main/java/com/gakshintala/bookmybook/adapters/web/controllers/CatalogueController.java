package com.gakshintala.bookmybook.adapters.web.controllers;

import com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.request.AddBookInstanceToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.response.CatalogueBookIdResponse;
import com.gakshintala.bookmybook.adapters.web.restexchange.catalogue.response.CatalogueBookInstanceIdResponse;
import com.gakshintala.bookmybook.core.ports.UseCaseExecutor;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.core.usecases.catalogue.AddBookToCatalogue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/catalogue")
@RequiredArgsConstructor
public class CatalogueController {
    private final UseCaseExecutor useCaseExecutor;
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;

    @PostMapping("/add-book")
    public CompletableFuture<CatalogueBookIdResponse> addBookToCatalogue(@Valid AddBookToCatalogueRequest addBookToCatalogueRequest) {
        return useCaseExecutor.execute(
                addBookToCatalogue,
                addBookToCatalogueRequest.toCommand(),
                CatalogueBookIdResponse::fromResult
        );
    }

    @PostMapping("/add-book-instance")
    public CompletableFuture<CatalogueBookInstanceIdResponse> addBookInstanceToCatalogue(@Valid AddBookInstanceToCatalogueRequest addBookInstanceToCatalogueRequest) {
        return useCaseExecutor.execute(
                addBookInstanceToCatalogue,
                addBookInstanceToCatalogueRequest.toCommand(),
                CatalogueBookInstanceIdResponse::fromResult
        );
    }
}
