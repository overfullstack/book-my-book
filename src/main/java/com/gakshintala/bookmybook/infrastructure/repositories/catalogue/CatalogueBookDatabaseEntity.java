package com.gakshintala.bookmybook.infrastructure.repositories.catalogue;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CatalogueBookDatabaseEntity {
    String isbn;
    String author;
    String title;
}
