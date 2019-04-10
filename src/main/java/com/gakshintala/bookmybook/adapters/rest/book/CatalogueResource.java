package com.gakshintala.bookmybook.adapters.rest.book;

import org.springframework.http.ResponseEntity;
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
    CompletableFuture<ResponseEntity<ApiResponse>> addBookToCatalogue(
            @Valid @RequestBody AddBookRequest addBookRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/add-book-instance")
    CompletableFuture<CatalogueBookResponse> addBookInstanceToCatalogue(
            @Valid @RequestBody AddBookInstanceRequest addBookInstanceRequest, HttpServletRequest httpServletRequest);
    
}
