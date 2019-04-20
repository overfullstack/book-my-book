package com.gakshintala.bookmybook.fixtures;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import com.gakshintala.bookmybook.core.domain.patron.Hold;
import com.gakshintala.bookmybook.core.domain.patron.OverdueCheckouts;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronHolds;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicy;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.util.UUID;

import static com.gakshintala.bookmybook.core.domain.patron.PatronType.Regular;
import static com.gakshintala.bookmybook.core.domain.patron.PatronType.Researcher;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.allCurrentPolicies;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.onlyResearcherPatronsCanHoldRestrictedBooksPolicy;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.overdueCheckoutsRejectionPolicy;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.regularPatronMaximumNumberOfHoldsPolicy;
import static com.gakshintala.bookmybook.fixtures.BookFixture.anyBookId;
import static com.gakshintala.bookmybook.fixtures.LibraryBranchFixture.anyBranch;
import static java.util.stream.IntStream.rangeClosed;

public class PatronFixture {

    public static Patron regularPatron() {
        return regularPatron(anyPatronId());
    }

    public static Patron regularPatronWithPolicy(PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(anyPatronId(), Regular, placingOnHoldPolicy);
    }

    public static Patron researcherPatronWithPolicy(PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(anyPatronId(), Researcher, placingOnHoldPolicy);
    }

    public static Patron regularPatronWithPolicy(PatronId patronId, PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(patronId, Regular, placingOnHoldPolicy);
    }

    public static Patron researcherPatronWithPolicy(PatronId patronId, PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(patronId, Researcher, placingOnHoldPolicy);
    }

    private static Patron patronWithPolicy(PatronId patronId, PatronType type, PlacingOnHoldPolicy placingOnHoldPolicy) {
        return new Patron(patronInformation(patronId, type),
                List.of(placingOnHoldPolicy),
                new OverdueCheckouts(HashMap.empty()),
                noHolds());
    }

    public static Patron regularPatron(PatronId patronId) {
        return new Patron(
                patronInformation(patronId, Regular),
                List.of(onlyResearcherPatronsCanHoldRestrictedBooksPolicy),
                new OverdueCheckouts(HashMap.empty()),
                noHolds());
    }

    public static Patron researcherPatron(PatronId patronId) {
        return new Patron(
                patronInformation(patronId, Researcher),
                List.of(onlyResearcherPatronsCanHoldRestrictedBooksPolicy),
                new OverdueCheckouts(HashMap.empty()),
                noHolds());
    }

    static PatronInformation patronInformation(PatronId id, PatronType type) {
        return new PatronInformation(id, type);
    }

    public static Patron regularPatronWithHolds(int numberOfHolds) {
        PatronId patronId = anyPatronId();
        return new Patron(
                patronInformation(patronId, Regular),
                List.of(regularPatronMaximumNumberOfHoldsPolicy),
                new OverdueCheckouts(HashMap.empty()),
                booksOnHold(numberOfHolds));
    }

    static Patron regularPatronWith(Hold hold) {
        PatronId patronId = anyPatronId();
        PatronHolds patronHolds = new PatronHolds(HashSet.of(hold));
        return new Patron(
                patronInformation(patronId, Regular),
                allCurrentPolicies(),
                new OverdueCheckouts(HashMap.empty()),
                patronHolds);
    }

    public static Patron regularPatronWith(BookOnHold bookOnHold, PatronId patronId) {
        PatronHolds patronHolds = new PatronHolds(HashSet.of(new Hold(bookOnHold.getBookId(), bookOnHold.getHoldPlacedAt())));
        return new Patron(
                patronInformation(patronId, Regular),
                allCurrentPolicies(),
                new OverdueCheckouts(HashMap.empty()),
                patronHolds);
    }

    public static Hold onHold() {
        return new Hold(anyBookId(), anyBranch());
    }

    static PatronHolds booksOnHold(int numberOfHolds) {
        return new PatronHolds(HashSet.ofAll(rangeClosed(1, numberOfHolds)
                .mapToObj(i -> new Hold(anyBookId(), anyBranch()))));
    }

    static Patron researcherPatronWithHolds(int numberOfHolds) {
        PatronId patronId = anyPatronId();
        return new Patron(
                patronInformation(patronId, Researcher),
                List.of(regularPatronMaximumNumberOfHoldsPolicy),
                new OverdueCheckouts(HashMap.empty()),
                booksOnHold(numberOfHolds));
    }

    static Patron regularPatronWithOverdueCheckouts(LibraryBranchId libraryBranchId, Set<CatalogueBookInstanceUUID> overdueBooks) {
        Map<LibraryBranchId, Set<CatalogueBookInstanceUUID>> overdueCheckouts = HashMap.empty();
        overdueCheckouts.put(libraryBranchId, overdueBooks);
        return new Patron(
                patronInformation(anyPatronId(), Regular),
                List.of(overdueCheckoutsRejectionPolicy),
                new OverdueCheckouts(overdueCheckouts),
                noHolds());
    }

    static Patron regularPatronWith3_OverdueCheckoutsAt(LibraryBranchId libraryBranchId) {
        Map<LibraryBranchId, Set<CatalogueBookInstanceUUID>> overdueCheckouts = HashMap.empty();
        overdueCheckouts.put(libraryBranchId, HashSet.of(anyBookId(), anyBookId(), anyBookId()));
        return new Patron(
                patronInformation(anyPatronId(), Regular),
                List.of(overdueCheckoutsRejectionPolicy),
                new OverdueCheckouts(overdueCheckouts),
                noHolds());
    }

    public static PatronId anyPatronId() {
        return patronId(UUID.randomUUID());
    }

    public static PatronId anyPatron() {
        return patronId(UUID.randomUUID());
    }

    static PatronId patronId(UUID patronId) {
        return new PatronId(patronId);
    }

    static PatronHolds noHolds() {
        return new PatronHolds(HashSet.empty());
    }


    public static Patron regularPatronWithHold(BookOnHold bookOnHold) {
        return regularPatronWith(new Hold(bookOnHold.getBookId(), bookOnHold.getHoldPlacedAt()));
    }


}
