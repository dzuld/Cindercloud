TRUNCATE login_events;

ALTER TABLE login_events
  ADD COLUMN wallet_type VARCHAR(100) NOT NULL;