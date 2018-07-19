alter table blocks
    add column block_timestamp TIMESTAMP not null;

alter table blocks
    add column uncle boolean default false;