package com.gakshintala.bookmybook.mappers;

import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.repositories.catalogue.CatalogueBookEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CatalogueBookDomainMapper {
    public static CatalogueBook toDomainModel(CatalogueBookEntity catalogueBookEntity) {
        return new CatalogueBook(catalogueBookEntity.getIsbn(),
                catalogueBookEntity.getAuthor(),
                catalogueBookEntity.getTitle());
    }
}
