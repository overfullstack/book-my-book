package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookEverywhereRequest;
import com.gakshintala.bookmybook.adapters.rest.book.request.AddBookToLibraryRequest;
import com.gakshintala.bookmybook.adapters.rest.book.response.LibraryBookResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/library")
public interface LibraryResource {

    @PostMapping("/add-book")
    CompletableFuture<LibraryBookResponse> addBookToLibrary(
            @Valid @RequestBody AddBookToLibraryRequest addBookToLibraryRequest, HttpServletRequest httpServletRequest);

    @PostMapping("/add-book-everywhere")
    CompletableFuture<LibraryBookResponse> addBookEverywhere(
            @Valid @RequestBody AddBookEverywhereRequest addBookEverywhereRequest, HttpServletRequest httpServletRequest);
}
