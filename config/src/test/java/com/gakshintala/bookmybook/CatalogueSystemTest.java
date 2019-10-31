package com.gakshintala.bookmybook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gakshintala.bookmybook.ports.persistence.catalogue.FindCatalogueBook;
import com.gakshintala.bookmybook.restexchange.catalogue.response.CatalogueBookInstanceResponse;
import com.gakshintala.bookmybook.restexchange.catalogue.response.CatalogueBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static com.gakshintala.bookmybook.fixtures.WebRequestFixture.addBookInstanceToCatalogueRequest;
import static com.gakshintala.bookmybook.fixtures.WebRequestFixture.addBookToCatalogueRequest;
import static org.assertj.core.api.BDDAssertions.then;

/* gakshintala created on 10/31/19 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class CatalogueSystemTest {
    @Autowired
    FindCatalogueBook findCatalogueBook;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addBookToCatalogue() {
        final var response = restTemplate.exchange(
                "/catalogue/add-book",
                HttpMethod.POST,
                getHttpRequest(addBookToCatalogueRequest()),
                CatalogueBookResponse.class);

        then(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        then(findCatalogueBook.findBy(Objects.requireNonNull(response.getBody())
                .getCatalogueBookInfo()._2.getIsbn())).isNotNull();
    }

    @NotNull
    private <T> HttpEntity<T> getHttpRequest(T request) {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(request, headers);
    }

    @Test
    @Sql("CatalogueSystemTest.sql")
    void addBookInstanceToCatalogue() {
        final var response = restTemplate.exchange(
                "/catalogue/add-book-instance",
                HttpMethod.POST,
                getHttpRequest(addBookInstanceToCatalogueRequest()),
                CatalogueBookInstanceResponse.class);
        log.info(Objects.requireNonNull(response.getBody()).toString());
        then(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

}
