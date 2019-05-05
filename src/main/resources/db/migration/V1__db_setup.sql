CREATE TABLE IF NOT EXISTS catalogue_book
(
    id     INTEGER IDENTITY PRIMARY KEY,
    isbn   VARCHAR(100) NOT NULL UNIQUE,
    title  VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL
);
CREATE SEQUENCE catalogue_book_seq;

CREATE TABLE IF NOT EXISTS catalogue_book_instance
(
    id      INTEGER IDENTITY PRIMARY KEY,
    isbn    VARCHAR(100) NOT NULL,
    book_id UUID         NOT NULL UNIQUE
);
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

