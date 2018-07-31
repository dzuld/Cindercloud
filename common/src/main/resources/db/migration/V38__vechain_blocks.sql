create table vechain_blocks (
  id              varchar(66) primary key,
  number          bigint    not null,
  parent_id       varchar(66)  default null,
  size            bigint    not null,
  receipts_root   VARCHAR(66)  DEFAULT NULL,
  txs_root        VARCHAR(66)  DEFAULT NULL,
  gas_used        VARCHAR(100) DEFAULT NULL,
  gas_limit        VARCHAR(100) DEFAULT NULL,
  total_score     varchar(100) default null,
  state_root      VARCHAR(66)  DEFAULT NULL,
  beneficiary     VARCHAR(66)  DEFAULT NULL,
  block_timestamp TIMESTAMP not null,
  signer          varchar(66)  default null,
  trunk           boolean      default false
);