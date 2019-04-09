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
    @PostMapping("/addToCatalogue")
    CompletableFuture<ResponseEntity<ApiResponse>> addBookToCatalogue(
            @Valid @RequestBody AddBookRequest request, HttpServletRequest httpServletRequest);

}
