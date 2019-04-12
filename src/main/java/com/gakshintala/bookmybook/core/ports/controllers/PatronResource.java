package com.gakshintala.bookmybook.core.ports.controllers;

import com.gakshintala.bookmybook.adapters.rest.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.adapters.rest.patron.response.PatronResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/patron")
public interface PatronResource {

    @PostMapping("/create")
    CompletableFuture<PatronResponse> createPatron(
            @Valid @RequestBody CreatePatronRequest createPatronRequest, HttpServletRequest httpServletRequest);
    
}
