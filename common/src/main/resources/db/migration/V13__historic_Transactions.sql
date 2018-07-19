CREATE TABLE historic_transactions (
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
  v                 INT          DEFAULT NULL,
  block_height      BIGINT NOT NULL
);

CREATE INDEX idx_h_tx_from_adddress
  ON historic_transactions (from_address);

CREATE INDEX idx_h_tx_to_address
  ON historic_transactions (to_address);

CREATE INDEX idx_h_tx_block_hash
  ON historic_transactions (block_hash);

CREATE INDEX idx_h_tx_block_height
  ON historic_transactions (block_height);

