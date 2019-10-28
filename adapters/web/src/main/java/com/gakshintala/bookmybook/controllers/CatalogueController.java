package com.gakshintala.bookmybook.controllers;

import com.gakshintala.bookmybook.UseCaseExecutor;
import com.gakshintala.bookmybook.restexchange.catalogue.request.AddBookInstanceToCatalogueRequest;
import com.gakshintala.bookmybook.restexchange.catalogue.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.restexchange.catalogue.response.CatalogueBookIdResponse;
import com.gakshintala.bookmybook.restexchange.catalogue.response.CatalogueBookInstanceIdResponse;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookToCatalogue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/catalogue")
@RequiredArgsConstructor
public class CatalogueController {
    private final AddBookToCatalogue addBookToCatalogue;
    private final AddBookInstanceToCatalogue addBookInstanceToCatalogue;

    @PostMapping("/add-book")
    public CompletableFuture<CatalogueBookIdResponse> addBookToCatalogue(@Valid @RequestBody AddBookToCatalogueRequest addBookToCatalogueRequest) {
        return UseCaseExecutor.execute(
                addBookToCatalogue,
                addBookToCatalogueRequest.toCommand(),
                CatalogueBookIdResponse::fromResult
        );
    }

    @PostMapping("/add-book-instance")
    public CompletableFuture<CatalogueBookInstanceIdResponse> addBookInstanceToCatalogue(@Valid @RequestBody AddBookInstanceToCatalogueRequest addBookInstanceToCatalogueRequest) {
        return UseCaseExecutor.execute(
                addBookInstanceToCatalogue,
                addBookInstanceToCatalogueRequest.toCommand(),
                CatalogueBookInstanceIdResponse::fromResult
        );
    }
}
