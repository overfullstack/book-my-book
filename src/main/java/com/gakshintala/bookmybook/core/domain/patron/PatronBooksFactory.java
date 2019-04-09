package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.BookId;
import com.gakshintala.bookmybook.core.domain.common.LibraryBranchId;
import io.vavr.Tuple2;

import java.util.Map;
import java.util.Set;

import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicy.allCurrentPolicies;
import static java.util.stream.Collectors.toSet;

public class PatronBooksFactory {

    public PatronBooks create(PatronType patronType, PatronId patronId, Set<Tuple2<BookId, LibraryBranchId>> patronHolds, Map<LibraryBranchId, Set<BookId>> overdueCheckouts) {
        return new PatronBooks(new PatronInformation(patronId, patronType),
                allCurrentPolicies(),
                new OverdueCheckouts(overdueCheckouts),
                new PatronHolds(
                        patronHolds
                                .stream()
                                .map(tuple -> new Hold(tuple._1, tuple._2))
                                .collect(toSet())));
    }

}
