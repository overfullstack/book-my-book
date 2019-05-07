package com.gakshintala.bookmybook.core.ports.controllers;

import com.gakshintala.bookmybook.adapters.rest.catalogue.request.AddBookInstanceToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.catalogue.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.catalogue.response.CatalogueBookIdResponse;
import com.gakshintala.bookmybook.adapters.rest.catalogue.response.CatalogueBookInstanceIdResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/catalogue")
public interface CatalogueResource {

    @PostMapping("/add-book")
    CompletableFuture<CatalogueBookIdResponse> addBookToCatalogue(
            @Valid @RequestBody AddBookToCatalogueRequest addBookToCatalogueRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/add-book-instance")
    CompletableFuture<CatalogueBookInstanceIdResponse> addBookInstanceToCatalogue(
            @Valid @RequestBody AddBookInstanceToCatalogueRequest addBookInstanceToCatalogueRequest, HttpServletRequest httpServletRequest);
    
}
