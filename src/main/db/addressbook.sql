PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;

CREATE TABLE Addresses (
  addressID integer,
  firstName text NOT NULL,
  lastName text,
  email text,
  phoneNumber text,

  PRIMARY KEY(addressID)
);

COMMIT;

