CREATE TABLE tokens (
  id                 SERIAL PRIMARY KEY,
  coinmarketcap_name VARCHAR(100) DEFAULT NULL,
  address            VARCHAR(42)  NOT NULL,
  name               VARCHAR(100) NOT NULL,
  slug               VARCHAR(100) NOT NULL,
  image              TEXT         DEFAULT NULL,
  decimals           SMALLINT      DEFAULT 18
)