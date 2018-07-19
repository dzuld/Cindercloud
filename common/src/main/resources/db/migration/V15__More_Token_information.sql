alter table tokens
    add column symbol varchar(20) default null;

alter table tokens
    add column social text default null;

alter table tokens
    add column website text default null;