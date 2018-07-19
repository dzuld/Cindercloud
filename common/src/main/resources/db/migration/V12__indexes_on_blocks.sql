create index idx_blocks_mined_by
  on blocks(mined_by);

create index idx_blocks_height
  on blocks(height);

create index idx_blocks_timestamp
  on blocks(block_timestamp);