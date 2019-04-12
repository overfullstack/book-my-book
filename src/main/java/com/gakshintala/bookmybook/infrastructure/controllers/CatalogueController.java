package com.gakshintala.bookmybook.infrastructure.controllers;

import com.gakshintala.bookmybook.adapters.rest.catalogue.request.AddBookInstanceToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.catalogue.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.catalogue.response.CatalogueBookInstanceUUIDResponse;
import com.gakshintala.bookmybook.adapters.rest.catalogue.response.CatalogueBookIdResponse;
import com.gakshintala.bookmybook.core.ports.controllers.CatalogueResource;
import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.core.usecases.AddBookToCatalogue;
import com.gakshintala.bookmybook.core.usecases.UseCaseExecutor;
import lombok.RequiredArgsConstructor;
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
    public CompletableFuture<CatalogueBookIdResponse> addBookToCatalogue(@Valid AddBookToCatalogueRequest addBookToCatalogueRequest,
                                                                         HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToCatalogue,
                addBookToCatalogueRequest.toCommand(),
                CatalogueBookIdResponse::fromResult
        );
    }

    @Override
    public CompletableFuture<CatalogueBookInstanceUUIDResponse> addBookInstanceToCatalogue(@Valid AddBookInstanceToCatalogueRequest addBookInstanceToCatalogueRequest,
                                                                                           HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookInstanceToCatalogue,
                addBookInstanceToCatalogueRequest.toCommand(),
                CatalogueBookInstanceUUIDResponse::fromResult
        );
    }
}
