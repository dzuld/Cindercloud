CREATE TABLE token_transfers (
  id BIGSERIAL PRIMARY KEY,
  from_address     VARCHAR(42)  DEFAULT NULL,
  to_address       VARCHAR(42)  DEFAULT NULL,
  transaction_hash VARCHAR(66) NOT NULL,
  log_index        VARCHAR(50)  DEFAULT NULL,
  origin_address   VARCHAR(42)  DEFAULT NULL,
  removed          BOOL         DEFAULT FALSE,
  block_timestamp  TIMESTAMP   NOT NULL,
  block_height     BIGINT      NOT NULL,
  token_address    VARCHAR(42) NOT NULL,
  amount           VARCHAR(100) DEFAULT 0
);

ALTER TABLE token_transfers
  ADD CONSTRAINT uq_tx_hash_log_idx UNIQUE (transaction_hash, log_index);