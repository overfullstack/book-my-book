package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookInstanceToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.adapters.rest.book.response.ApiResponse;
import com.gakshintala.bookmybook.adapters.rest.book.response.CatalogueBookResponse;
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
            @Valid @RequestBody AddBookToCatalogueRequest addBookToCatalogueRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/add-book-instance")
    CompletableFuture<CatalogueBookResponse> addBookInstanceToCatalogue(
            @Valid @RequestBody AddBookInstanceToCatalogueRequest addBookInstanceToCatalogueRequest, HttpServletRequest httpServletRequest);
    
}
