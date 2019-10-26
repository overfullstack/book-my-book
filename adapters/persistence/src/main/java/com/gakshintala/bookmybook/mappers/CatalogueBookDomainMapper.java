package com.gakshintala.bookmybook.mappers;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.repositories.catalogue.CatalogueBookDatabaseEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CatalogueBookDomainMapper {
    public static CatalogueBook toDomainModel(CatalogueBookDatabaseEntity catalogueBookDatabaseEntity) {
        return new CatalogueBook(catalogueBookDatabaseEntity.getIsbn(),
                catalogueBookDatabaseEntity.getAuthor(),
                catalogueBookDatabaseEntity.getTitle());
    }
}
