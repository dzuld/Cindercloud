create table custom_erc20 (
  id bigserial primary key,
  address  varchar(42)  not null,
  name     varchar(100) not null,
  symbol   varchar(5)   not null,
  added_by varchar(42)  not null,
  decimals smallint default 18
);


create index idx_added_by_erc20
  on custom_erc20(added_by);