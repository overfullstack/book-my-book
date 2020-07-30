package com.gakshintala.bookmybook.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gakshintala.bookmybook.restexchange.catalogue.request.AddBookToCatalogueRequest;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookInstanceToCatalogue;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookToCatalogue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogueController.class)
class CatalogueControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AddBookToCatalogue addBookToCatalogue;

    @MockBean
    AddBookInstanceToCatalogue addBookInstanceToCatalogue;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addBookToCatalogue() throws Exception {
        var addBookToCatalogueRequest = 
                new AddBookToCatalogueRequest("0321125215", "title", "author");
        mockMvc.perform(
                post("/catalogue/add-book")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBookToCatalogueRequest)))
                .andExpect(status().isOk());
    }
}