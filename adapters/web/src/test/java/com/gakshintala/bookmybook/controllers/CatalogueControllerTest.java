package com.gakshintala.bookmybook.controllers;

import com.gakshintala.bookmybook.usecaseexecutor.UseCaseExecutorImpl;
import com.gakshintala.bookmybook.usecases.catalogue.AddBookToCatalogue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.gakshintala.bookmybook.utils.Utils.fileToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatalogueController.class)
@ContextConfiguration(classes = {UseCaseExecutorImpl.class})
class CatalogueControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AddBookToCatalogue addBookToCatalogue;
    
    @Test
    void addBookToCatalogue() throws Exception {
        mockMvc.perform(post("/api/v1/catalogue/add-book")
                .header("Content-Type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fileToString("addBookToCatalogue.json")))
                .andExpect(status().isOk());
    }
}