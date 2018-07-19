CREATE TABLE transactions (
  hash              VARCHAR(66) PRIMARY KEY,
  block_hash        VARCHAR(66)  DEFAULT NULL,
  value             VARCHAR(100) DEFAULT 0,
  gas_price         VARCHAR(100) DEFAULT NULL,
  gas               VARCHAR(100) DEFAULT NULL,
  transaction_index BIGINT       DEFAULT NULL,
  input             TEXT         DEFAULT NULL,
  nonce             VARCHAR(50)  DEFAULT NULL,
  from_address      VARCHAR(42)  DEFAULT NULL,
  to_address        VARCHAR(42)  DEFAULT NULL,
  creates           VARCHAR(42)  DEFAULT NULL,
  s                 TEXT         DEFAULT NULL,
  r                 TEXT         DEFAULT NULL,
  v                 INT          DEFAULT NULL
);