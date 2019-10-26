package com.gakshintala.bookmybook.repositories.catalogue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "catalogue_book")
@NoArgsConstructor
public class CatalogueBookDatabaseEntity {
    @Id
    String isbn;
    String author;
    String title;
}
