create table vechain_transactions (
  id              varchar(66) primary key,
  chain_tag       varchar(42) default null,
  block_ref       varchar(42) default null,
  expiration      bigint      default 0,
  gas_price_coef  bigint      default 0,
  gas             bigint      default 0,
  origin          varchar(66) default null,
  nonce           varchar(50) default null,
  depends_on      varchar(66) default null,
  size            bigint      default 0,
  block_id        varchar(66) default null,
  block_number    bigint      default 0,
  block_timestamp timestamp   default null
);

create table vechain_transaction_clauses (
  id             serial primary key,
  transaction_id varchar(66),
  to_address     varchar(42) default null,
  data           TEXT        default null,
  value          TEXT        default null
);

alter table vechain_transaction_clauses
  add constraint fk_transaction_clauses_tx_id foreign key (transaction_id) references vechain_transactions (id);