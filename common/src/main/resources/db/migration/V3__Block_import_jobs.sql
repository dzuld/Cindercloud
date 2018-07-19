CREATE TABLE block_import_jobs (
  id         SERIAL PRIMARY KEY,
  active     BOOL      DEFAULT FALSE,
  from_block BIGINT    DEFAULT 0,
  to_block   BIGINT    DEFAULT 0,
  start_time TIMESTAMP DEFAULT NULL,
  end_time   TIMESTAMP DEFAULT NULL
);