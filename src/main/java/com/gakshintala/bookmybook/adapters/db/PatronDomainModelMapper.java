package com.gakshintala.bookmybook.adapters.db;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.Hold;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronHolds;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import com.gakshintala.bookmybook.infrastructure.repositories.patron.PatronDatabaseEntity;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Set;
import lombok.experimental.UtilityClass;

import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.allCurrentPolicies;

@UtilityClass
public class PatronDomainModelMapper {

    public static Patron toDomainModel(PatronDatabaseEntity entity) {
        return create(
                entity.getPatronType(),
                new PatronId(entity.getPatronId()),
                mapPatronHolds(entity)
        );
    }

    private static Patron create(PatronType patronType, PatronId patronId, Set<Tuple2<CatalogueBookInstanceId, LibraryBranchId>> patronHolds) {
        return new Patron(new PatronInformation(patronId, patronType),
                allCurrentPolicies(),
                new PatronHolds(patronHolds
                        .map(tuple -> new Hold(tuple._1, tuple._2))
                        .toSet()));
    }

    private static Set<Tuple2<CatalogueBookInstanceId, LibraryBranchId>> mapPatronHolds(PatronDatabaseEntity patronDatabaseEntity) {
        return patronDatabaseEntity
                .getBooksOnHold()
                .map(entity -> Tuple.of((new CatalogueBookInstanceId(entity.getBookId())), new LibraryBranchId(entity.getLibraryBranchId())))
                .toSet();
    }

}
