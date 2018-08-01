create table vechain_transaction_receipts (
  id              varchar(66) primary key,
  gas_used        text        default 0,
  gas_payer       varchar(42) default null,
  paid            text        default 0,
  reward          text        default 0,
  reverted        bool        default false,
  block_id        varchar(66) default null,
  block_number    bigint not null,
  block_timestamp timestamp   default null
);

create table vechain_transaction_receipt_outputs (
  id               bigserial primary key,
  contract_address varchar(42) default null,
  transaction_id varchar(66) not null,
  foreign key(transaction_id) references vechain_transaction_receipts(id)
);

create table vechain_transaction_transfers (
  id        bigserial primary key,
  sender    varchar(42) default null,
  recipient varchar(42) default null,
  amount    text        default null,
  output_id bigint not null,
  foreign key (output_id) references vechain_transaction_receipt_outputs (id)
);

create table vechain_transaction_events (
  id        bigserial primary key,
  address   varchar(42) default null,
  data      text        default null,
  output_id bigint not null,
  foreign key (output_id) references vechain_transaction_receipt_outputs (id)
);


create table vechain_transaction_event_topics (
  topic    TEXT default null,
  event_id bigint not null,
  foreign key (event_id) references vechain_transaction_events (id));

