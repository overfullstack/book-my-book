package com.gakshintala.bookmybook.mappers;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.domain.patron.Hold;
import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronHolds;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.domain.patron.PatronType;
import com.gakshintala.bookmybook.repositories.patron.PatronEntity;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;

import static com.gakshintala.bookmybook.domain.patron.PlacingOnHoldPolicies.allCurrentPolicies;


@UtilityClass
public class PatronDomainModelMapper {

    public static Patron toDomainModel(PatronEntity entity) {
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

    private static Set<Tuple2<CatalogueBookInstanceId, LibraryBranchId>> mapPatronHolds(PatronEntity patronEntity) {
        return Stream.ofAll(patronEntity.getBooksOnHold())
                .map(entity -> Tuple.of((new CatalogueBookInstanceId(entity.getBookId())), new LibraryBranchId(entity.getLibraryBranchId())))
                .toSet();
    }

}
