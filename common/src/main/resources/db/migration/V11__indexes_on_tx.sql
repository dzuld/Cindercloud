create index idx_tx_from_adddress
   on transactions(from_address);

create index idx_tx_to_address
  on transactions(to_address);

create index idx_tx_block_hash
  on transactions(block_hash);

create index idx_tx_block_height
  on transactions(block_height);

