package com.gakshintala.bookmybook.adapters.db;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.Hold;
import com.gakshintala.bookmybook.core.domain.patron.OverdueCheckouts;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronHolds;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import com.gakshintala.bookmybook.infrastructure.repositories.patron.OverdueCheckoutDatabaseEntity;
import com.gakshintala.bookmybook.infrastructure.repositories.patron.PatronDatabaseEntity;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import lombok.experimental.UtilityClass;

import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.allCurrentPolicies;

@UtilityClass
public class PatronDomainModelMapper {

    public static Patron toDomainModel(PatronDatabaseEntity entity) {
        return create(
                entity.getPatronType(),
                new PatronId(entity.getPatronId()),
                mapPatronHolds(entity),
                mapPatronOverdueCheckouts(entity)
        );
    }

    private static Patron create(PatronType patronType, PatronId patronId, Set<Tuple2<CatalogueBookInstanceUUID, LibraryBranchId>> patronHolds,
                                 Map<LibraryBranchId, Set<CatalogueBookInstanceUUID>> overdueCheckouts) {
        return new Patron(new PatronInformation(patronId, patronType),
                allCurrentPolicies(),
                new OverdueCheckouts(overdueCheckouts),
                new PatronHolds(patronHolds.toStream()
                        .map(tuple -> new Hold(tuple._1, tuple._2))
                        .toSet()));
    }

    private static Map<LibraryBranchId, Set<CatalogueBookInstanceUUID>> mapPatronOverdueCheckouts(PatronDatabaseEntity patronDatabaseEntity) {
        return patronDatabaseEntity
                .getCheckouts()
                .toStream()
                .toMap(OverdueCheckoutDatabaseEntity::getLibraryBranchId, HashSet::of)
                .toSet()
                .toStream()
                .toMap(entry -> new LibraryBranchId(entry._1()),
                        entry -> entry
                                ._2()
                                .toStream()
                                .map(entity -> (new CatalogueBookInstanceUUID(entity.getBookId())))
                                .toSet());
    }

    private static Set<Tuple2<CatalogueBookInstanceUUID, LibraryBranchId>> mapPatronHolds(PatronDatabaseEntity patronDatabaseEntity) {
        return patronDatabaseEntity
                .getBooksOnHold()
                .toStream()
                .map(entity -> Tuple.of((new CatalogueBookInstanceUUID(entity.getBookId())), new LibraryBranchId(entity.getLibraryBranchId())))
                .toSet();
    }

}
