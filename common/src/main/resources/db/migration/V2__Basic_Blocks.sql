CREATE TABLE blocks (
  hash             VARCHAR(66) PRIMARY KEY,
  height           BIGINT NOT NULL,
  parent_hash      VARCHAR(66)  DEFAULT NULL,
  sha3_uncles      VARCHAR(66)  DEFAULT NULL,
  mined_by         VARCHAR(42)  DEFAULT NULL,
  difficulty       VARCHAR(100) DEFAULT NULL,
  difficulty_total VARCHAR(100) DEFAULT NULL,
  gas_limit        VARCHAR(100) DEFAULT NULL,
  gas_used         VARCHAR(100) DEFAULT NULL,
  receipts_root    VARCHAR(66)  DEFAULT NULL,
  size             BIGINT       DEFAULT NULL,
  timestamp        BIGINT       DEFAULT NULL,
  nonce            VARCHAR(50)       DEFAULT NULL,
  extra_data       TEXT         DEFAULT NULL,
  mix_hash         VARCHAR(66)  DEFAULT NULL
);