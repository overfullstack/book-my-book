CREATE TABLE IF NOT EXISTS catalogue_book
(
    id     INTEGER IDENTITY PRIMARY KEY,
    isbn   VARCHAR(100) NOT NULL UNIQUE,
    title  VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS catalogue_book_instance
(
    id      INTEGER IDENTITY PRIMARY KEY,
    isbn    VARCHAR(100) NOT NULL,
    book_id UUID         NOT NULL UNIQUE
);

CREATE SEQUENCE catalogue_book_seq;
CREATE SEQUENCE catalogue_book_instance_seq;


CREATE TABLE IF NOT EXISTS book_database_entity
(
    id                  INTEGER IDENTITY PRIMARY KEY,
    book_id             UUID UNIQUE,
    book_type           VARCHAR(100) NOT NULL,
    book_state          VARCHAR(100) NOT NULL,
    available_at_branch UUID,
    on_hold_at_branch   UUID,
    on_hold_by_patron   UUID,
    collected_at_branch UUID,
    collected_by_patron UUID,
    on_hold_till        TIMESTAMP,
    version             INTEGER
);

CREATE SEQUENCE book_database_entity_seq;


CREATE TABLE IF NOT EXISTS patron_database_entity
(
    id          INTEGER IDENTITY PRIMARY KEY,
    patron_type VARCHAR(100) NOT NULL,
    patron_id   UUID UNIQUE
);

CREATE TABLE IF NOT EXISTS hold_database_entity
(
    id                     INTEGER IDENTITY PRIMARY KEY,
    book_id                UUID      NOT NULL,
    patron_id              UUID      NOT NULL,
    library_branch_id      UUID      NOT NULL,
    patron_database_entity INTEGER   NOT NULL,
    till                   TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS overdue_checkout_database_entity
(
    id                     INTEGER IDENTITY PRIMARY KEY,
    book_id                UUID    NOT NULL,
    patron_id              UUID    NOT NULL,
    library_branch_id      UUID    NOT NULL,
    patron_database_entity INTEGER NOT NULL
);


CREATE TABLE IF NOT EXISTS checkouts_sheet
(
    id                     INTEGER IDENTITY PRIMARY KEY,
    book_id                UUID        NOT NULL,
    status                 VARCHAR(20) NOT NULL,
    checkout_event_id      UUID UNIQUE,
    collected_by_patron_id UUID,
    collected_at           TIMESTAMP,
    returned_at            TIMESTAMP,
    collected_at_branch    UUID,
    checkout_till          TIMESTAMP
);


CREATE TABLE IF NOT EXISTS holds_sheet
(
    id                INTEGER IDENTITY PRIMARY KEY,
    book_id           UUID        NOT NULL,
    status            VARCHAR(20) NOT NULL,
    hold_event_id     UUID UNIQUE,
    hold_at_branch    UUID,
    hold_by_patron_id UUID,
    hold_at           TIMESTAMP,
    hold_till         TIMESTAMP,
    expired_at        TIMESTAMP,
    canceled_at       TIMESTAMP,
    collected_at      TIMESTAMP
);

CREATE SEQUENCE holds_sheet_seq;
CREATE SEQUENCE checkouts_sheet_seq;

