package com.gakshintala.bookmybook.fixtures;

import com.gakshintala.bookmybook.core.domain.library.BookOnHold;
import com.gakshintala.bookmybook.core.domain.patron.Hold;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronHolds;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicy;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

import java.util.UUID;

import static com.gakshintala.bookmybook.core.domain.patron.PatronType.REGULAR;
import static com.gakshintala.bookmybook.core.domain.patron.PatronType.RESEARCHER;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.allCurrentPolicies;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.onlyResearcherPatronsCanHoldRestrictedBooksPolicy;
import static com.gakshintala.bookmybook.core.domain.patron.PlacingOnHoldPolicies.regularPatronMaximumNumberOfHoldsPolicy;
import static com.gakshintala.bookmybook.fixtures.BookFixture.anyBookId;
import static com.gakshintala.bookmybook.fixtures.LibraryBranchFixture.anyBranch;
import static java.util.stream.IntStream.rangeClosed;

public class PatronFixture {

    public static Patron regularPatron() {
        return regularPatron(anyPatronId());
    }

    public static Patron regularPatronWithPolicy(PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(anyPatronId(), REGULAR, placingOnHoldPolicy);
    }

    public static Patron researcherPatronWithPolicy(PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(anyPatronId(), RESEARCHER, placingOnHoldPolicy);
    }

    public static Patron regularPatronWithPolicy(PatronId patronId, PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(patronId, REGULAR, placingOnHoldPolicy);
    }

    public static Patron researcherPatronWithPolicy(PatronId patronId, PlacingOnHoldPolicy placingOnHoldPolicy) {
        return patronWithPolicy(patronId, RESEARCHER, placingOnHoldPolicy);
    }

    private static Patron patronWithPolicy(PatronId patronId, PatronType type, PlacingOnHoldPolicy placingOnHoldPolicy) {
        return new Patron(patronInformation(patronId, type),
                List.of(placingOnHoldPolicy),
                noHolds());
    }

    private static Patron regularPatron(PatronId patronId) {
        return new Patron(
                patronInformation(patronId, REGULAR),
                List.of(onlyResearcherPatronsCanHoldRestrictedBooksPolicy),
                noHolds());
    }

    public static Patron researcherPatron(PatronId patronId) {
        return new Patron(
                patronInformation(patronId, RESEARCHER),
                List.of(onlyResearcherPatronsCanHoldRestrictedBooksPolicy),
                noHolds());
    }

    private static PatronInformation patronInformation(PatronId id, PatronType type) {
        return new PatronInformation(id, type);
    }

    public static Patron regularPatronWithHolds(int numberOfHolds) {
        PatronId patronId = anyPatronId();
        return new Patron(
                patronInformation(patronId, REGULAR),
                List.of(regularPatronMaximumNumberOfHoldsPolicy),
                booksOnHold(numberOfHolds));
    }

    private static Patron regularPatronWith(Hold hold) {
        PatronId patronId = anyPatronId();
        PatronHolds patronHolds = new PatronHolds(HashSet.of(hold));
        return new Patron(
                patronInformation(patronId, REGULAR),
                allCurrentPolicies(),
                patronHolds);
    }

    public static Patron regularPatronWith(BookOnHold bookOnHold, PatronId patronId) {
        PatronHolds patronHolds = new PatronHolds(HashSet.of(new Hold(bookOnHold.getBookId(), bookOnHold.getHoldPlacedAt())));
        return new Patron(
                patronInformation(patronId, REGULAR),
                allCurrentPolicies(),
                patronHolds);
    }

    public static Hold onHold() {
        return new Hold(anyBookId(), anyBranch());
    }

    private static PatronHolds booksOnHold(int numberOfHolds) {
        return new PatronHolds(HashSet.ofAll(rangeClosed(1, numberOfHolds)
                .mapToObj(i -> new Hold(anyBookId(), anyBranch()))));
    }

    static Patron researcherPatronWithHolds(int numberOfHolds) {
        PatronId patronId = anyPatronId();
        return new Patron(
                patronInformation(patronId, RESEARCHER),
                List.of(regularPatronMaximumNumberOfHoldsPolicy),
                booksOnHold(numberOfHolds));
    }

    public static PatronId anyPatronId() {
        return patronId(UUID.randomUUID());
    }

    public static PatronId anyPatron() {
        return patronId(UUID.randomUUID());
    }

    private static PatronId patronId(UUID patronId) {
        return new PatronId(patronId);
    }

    private static PatronHolds noHolds() {
        return new PatronHolds(HashSet.empty());
    }


    public static Patron regularPatronWithHold(BookOnHold bookOnHold) {
        return regularPatronWith(new Hold(bookOnHold.getBookId(), bookOnHold.getHoldPlacedAt()));
    }


}
