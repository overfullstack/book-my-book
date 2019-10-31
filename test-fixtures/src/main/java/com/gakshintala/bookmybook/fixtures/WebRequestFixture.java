package com.gakshintala.bookmybook.fixtures;

import com.gakshintala.bookmybook.restexchange.catalogue.request.AddBookInstanceToCatalogueRequest;
import com.gakshintala.bookmybook.restexchange.catalogue.request.AddBookToCatalogueRequest;

/*
 * gakshintala created on 10/31/19
 */
public class WebRequestFixture {

    public static AddBookToCatalogueRequest addBookToCatalogueRequest() {
        return new AddBookToCatalogueRequest("0321125215", "title", "author");
    }

    public static AddBookInstanceToCatalogueRequest addBookInstanceToCatalogueRequest() {
        return new AddBookInstanceToCatalogueRequest("0321125215");
    }
}
