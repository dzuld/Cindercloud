CREATE TABLE leaked_credentials (
  address     VARCHAR(42) NOT NULL PRIMARY KEY,
  private_key VARCHAR(66) NOT NULL,
  date_added  TIMESTAMP DEFAULT now()
);