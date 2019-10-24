package com.gakshintala.bookmybook.adapters.persistence.mappers;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.adapters.persistence.repositories.catalogue.CatalogueBookDatabaseEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CatalogueBookDomainMapper {
    public static CatalogueBook toDomainModel(CatalogueBookDatabaseEntity catalogueBookDatabaseEntity) {
        return new CatalogueBook(catalogueBookDatabaseEntity.getIsbn(),
                catalogueBookDatabaseEntity.getAuthor(),
                catalogueBookDatabaseEntity.getTitle());
    }
}
