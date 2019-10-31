package com.gakshintala.bookmybook.repositories.catalogue;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class CatalogueBookEntity {
    @Id
    String isbn;
    String author;
    String title;
}
