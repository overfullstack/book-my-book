package com.gakshintala.bookmybook.domain.patron;

import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.library.LibraryBranchId;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

public interface PatronEvent {

    default PatronId patronId() {
        return new PatronId(getPatronId());
    }

    UUID getPatronId();

    default UUID getAggregateId() {
        return getPatronId();
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
    class BookPlacedOnHoldNow implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull BookType bookType;
        @NonNull UUID libraryBranchId;
        @NonNull Instant holdFrom;
        Instant holdTill;

        public static BookPlacedOnHoldNow bookPlacedOnHoldNow(CatalogueBookInstanceId catalogueBookId, BookType bookType,
                                                              LibraryBranchId libraryBranchId, PatronId patronId, HoldDuration holdDuration) {
            return new BookPlacedOnHoldNow(
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
    class BookPlacedOnHold implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull UUID patronId;
        @NonNull PatronEvent.BookPlacedOnHoldNow bookPlacedOnHoldNow;
        @NonNull Option<MaximumNumberOfHoldsReached> maximumNumberOfHoldsReached;

        public static BookPlacedOnHold with(BookPlacedOnHoldNow bookPlacedOnHoldNow) {
            return new BookPlacedOnHold(bookPlacedOnHoldNow.getPatronId(), bookPlacedOnHoldNow, Option.none());
        }

        public static BookPlacedOnHold with(BookPlacedOnHoldNow bookPlacedOnHoldNow, MaximumNumberOfHoldsReached maximumNumberOfHoldsReached) {
            return new BookPlacedOnHold(bookPlacedOnHoldNow.patronId, bookPlacedOnHoldNow, Option.of(maximumNumberOfHoldsReached));
        }

    }

    @Value
    class MaximumNumberOfHoldsReached implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        int numberOfHolds;

        public static MaximumNumberOfHoldsReached now(PatronInformation patronInformation, int numberOfHolds) {
            return new MaximumNumberOfHoldsReached(
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

        public static BookCollected bookCollectedNow(CatalogueBookInstanceId catalogueBookId, BookType bookType,
                                                     LibraryBranchId libraryBranchId, PatronId patronId, CheckoutDuration checkoutDuration) {
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
    class BookHoldFailed implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull String reason;
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        UUID libraryBranchId;

        public static BookHoldFailed bookHoldFailedNow(Rejection rejection, CatalogueBookInstanceId catalogueBookId,
                                                       UUID libraryBranchId, PatronId patronId) {
            return new BookHoldFailed(
                    rejection.getReason().getReason(),
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId);
        }
    }

    @Value
    class BookCollectingFailed implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull String reason;
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        UUID libraryBranchId;

        public static BookCollectingFailed bookCollectingFailedNow(Rejection rejection, CatalogueBookInstanceId catalogueBookId, LibraryBranchId libraryBranchId, PatronId patronId) {
            return new BookCollectingFailed(
                    rejection.getReason().getReason(),
                    Instant.now(),
                    patronId.getPatronId(),
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

        public static BookHoldCanceled holdCanceledNow(CatalogueBookInstanceId catalogueBookId, LibraryBranchId libraryBranchId, PatronId patronId) {
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
        UUID libraryBranchId;
        @NonNull String reason;

        public static BookHoldCancelingFailed holdCancelingFailedNow(CatalogueBookInstanceId catalogueBookId,
                                                                     LibraryBranchId libraryBranchId, PatronId patronId,
                                                                     Rejection rejection) {
            return new BookHoldCancelingFailed(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID(),
                    rejection.getReason().getReason());
        }
    }

    // TODO 2019-04-16 gakshintala: clear out these eventIds
    @Value
    class BookHoldExpired implements PatronEvent {
        @NonNull UUID eventId = UUID.randomUUID();
        @NonNull Instant when;
        @NonNull UUID patronId;
        @NonNull UUID bookId;
        @NonNull UUID libraryBranchId;

        public static BookHoldExpired now(CatalogueBookInstanceId catalogueBookId, PatronId patronId, LibraryBranchId libraryBranchId) {
            return new BookHoldExpired(
                    Instant.now(),
                    patronId.getPatronId(),
                    catalogueBookId.getBookInstanceUUID(),
                    libraryBranchId.getLibraryBranchUUID());
        }
    }

}



