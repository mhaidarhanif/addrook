PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;

CREATE TABLE People (
    PersonID integer,
    FirstName text NOT NULL,
    LastName text,
    Address text,
    City text,
    State text,
    ZIP text,
    Phone text,
    Email text,
    PRIMARY KEY(PersonID)
);

COMMIT;
