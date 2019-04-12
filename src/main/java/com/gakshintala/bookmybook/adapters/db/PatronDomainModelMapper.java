package com.gakshintala.bookmybook.adapters.db;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.*;
import com.gakshintala.bookmybook.infrastructure.repositories.patron.OverdueCheckoutDatabaseEntity;
import com.gakshintala.bookmybook.infrastructure.repositories.patron.PatronDatabaseEntity;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.allCurrentPolicies;
import static java.util.stream.Collectors.*;

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
                new PatronHolds(patronHolds.stream()
                        .map(tuple -> new Hold(tuple._1, tuple._2))
                        .collect(toSet())));
    }

    private static Map<LibraryBranchId, Set<CatalogueBookInstanceUUID>> mapPatronOverdueCheckouts(PatronDatabaseEntity patronDatabaseEntity) {
        return patronDatabaseEntity
                .getCheckouts()
                .stream()
                .collect(groupingBy(OverdueCheckoutDatabaseEntity::getLibraryBranchId, toSet()))
                .entrySet()
                .stream()
                .collect(toMap(
                        (Map.Entry<UUID, Set<OverdueCheckoutDatabaseEntity>> entry) -> new LibraryBranchId(entry.getKey()), entry -> entry
                                .getValue()
                                .stream()
                                .map(entity -> (new CatalogueBookInstanceUUID(entity.getBookId())))
                                .collect(toSet())));
    }

    private static Set<Tuple2<CatalogueBookInstanceUUID, LibraryBranchId>> mapPatronHolds(PatronDatabaseEntity patronDatabaseEntity) {
        return patronDatabaseEntity
                .getBooksOnHold()
                .stream()
                .map(entity -> Tuple.of((new CatalogueBookInstanceUUID(entity.getBookId())), new LibraryBranchId(entity.getLibraryBranchId())))
                .collect(toSet());
    }

}
