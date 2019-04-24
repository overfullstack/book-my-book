package com.gakshintala.bookmybook.infrastructure.repositories.catalogue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "catalogue_book")
@NoArgsConstructor
public class CatalogueBookDatabaseEntity {
    String isbn;
    String author;
    String title;
}
