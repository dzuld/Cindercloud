CREATE TABLE addressbook_contacts (
  id            SERIAL PRIMARY KEY,
  owner_address VARCHAR(100) NOT NULL,
  address       VARCHAR(100) NOT NULL,
  last_modified TIMESTAMP    NOT NULL,
  nickname      VARCHAR(200) DEFAULT NULL
);