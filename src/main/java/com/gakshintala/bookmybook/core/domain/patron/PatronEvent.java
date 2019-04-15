package com.gakshintala.bookmybook.core.domain.patron;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceUUID;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.common.DomainEvent;
import com.gakshintala.bookmybook.core.domain.library.LibraryBranchId;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

public interface PatronEvent extends DomainEvent {

    default PatronId patronId() {
        return new PatronId(getPatronId());
    }

    UUID getPatronId();

    default UUID getAggregateId() {
        return getPatronId();
    }

    default List<DomainEvent> normalize() {
        return List.of(this);
    }

    @Value
    class PatronCreated implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull PatronType patronType;

        public static PatronCreated now(PatronId patronId, PatronType type) {
            return new PatronCreated(Instant.now(), patronId.getPatronId(), type);
        }
    }

    @Value
    class BookPlacedOnHold implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull BookType bookType;
        @NonNull UUID libraryBranchId;
        @NonNull Instant holdFrom;
        Instant holdTill;

        public static BookPlacedOnHold bookPlacedOnHoldNow(CatalogueBookInstanceUUID catalogueBookId, BookType bookType, LibraryBranchId libraryBranchId, PatronId patronId, HoldDuration holdDuration) {
            return new BookPlacedOnHold(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    bookType,
                    libraryBranchId.getLibraryBranchUUID(),
                    holdDuration.getFrom(),
                    holdDuration.getTo().getOrNull());
        }
    }

    @Value
    class BookPlacedOnHoldEvents implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull UUID patronId;
        @NonNull BookPlacedOnHold bookPlacedOnHold;
        @NonNull Option<MaximumNumberOhHoldsReached> maximumNumberOhHoldsReached;

        @Override
        public Instant getWhen() {
            return bookPlacedOnHold.when;
        }

        public static BookPlacedOnHoldEvents events(BookPlacedOnHold bookPlacedOnHold) {
            return new BookPlacedOnHoldEvents(bookPlacedOnHold.getPatronId(), bookPlacedOnHold, Option.none());
        }

        public static BookPlacedOnHoldEvents events(BookPlacedOnHold bookPlacedOnHold, MaximumNumberOhHoldsReached maximumNumberOhHoldsReached) {
            return new BookPlacedOnHoldEvents(bookPlacedOnHold.patronId, bookPlacedOnHold, Option.of(maximumNumberOhHoldsReached));
        }
        
        @Override
        public List<DomainEvent> normalize() {
            return List.<DomainEvent>of(bookPlacedOnHold).appendAll(maximumNumberOhHoldsReached.toList());
        }
    }

    @Value
    class MaximumNumberOhHoldsReached implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        int numberOfHolds;

        public static MaximumNumberOhHoldsReached now(PatronInformation patronInformation, int numberOfHolds) {
            return new MaximumNumberOhHoldsReached(
                    Instant.now(),
                    patronInformation.getPatronId().getPatronId(),
                    numberOfHolds);
        }
    }

    @Value
    class BookCollected implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull BookType bookType;
        @NonNull UUID libraryBranchId;
        @NonNull Instant till;

        public static BookCollected bookCollectedNow(CatalogueBookInstanceUUID catalogueBookId, BookType bookType, LibraryBranchId libraryBranchId, PatronId patronId, CheckoutDuration checkoutDuration) {
            return new BookCollected(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    bookType,
                    libraryBranchId.getLibraryBranchUUID(),
                    checkoutDuration.to());
        }
    }

    @Value
    class BookReturned implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull BookType bookType;
        @NonNull UUID libraryBranchId;
    }

    @Value
    class BookHoldFailed implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull String reason;
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static BookHoldFailed bookHoldFailedNow(Rejection rejection, CatalogueBookInstanceUUID catalogueBookId, LibraryBranchId libraryBranchId, PatronInformation patronInformation) {
            return new BookHoldFailed(
                    rejection.getReason().getReason(),
                    Instant.now(),
                    patronInformation.getPatronId().getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

    @Value
    class BookCollectingFailed implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull String reason;
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static BookCollectingFailed bookCollectingFailedNow(Rejection rejection, CatalogueBookInstanceUUID catalogueBookId, LibraryBranchId libraryBranchId, PatronInformation patronInformation) {
            return new BookCollectingFailed(
                    rejection.getReason().getReason(),
                    Instant.now(),
                    patronInformation.getPatronId().getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

    @Value
    class BookHoldCanceled implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static BookHoldCanceled holdCanceledNow(CatalogueBookInstanceUUID catalogueBookId, LibraryBranchId libraryBranchId, PatronId patronId) {
            return new BookHoldCanceled(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

    @Value
    class BookHoldCancelingFailed implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static BookHoldCancelingFailed holdCancelingFailedNow(CatalogueBookInstanceUUID catalogueBookId, LibraryBranchId libraryBranchId, PatronId patronId) {
            return new BookHoldCancelingFailed(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

    @Value
    class BookHoldExpired implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static BookHoldExpired now(CatalogueBookInstanceUUID catalogueBookId, PatronId patronId, LibraryBranchId libraryBranchId) {
            return new BookHoldExpired(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

    @Value
    class OverdueCheckoutRegistered implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static OverdueCheckoutRegistered now(PatronId patronId, CatalogueBookInstanceUUID catalogueBookId, LibraryBranchId libraryBranchId) {
            return new OverdueCheckoutRegistered(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

}



