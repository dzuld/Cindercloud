CREATE TABLE events (
  id         SERIAL PRIMARY KEY,
  message    TEXT DEFAULT NULL,
  event_type VARCHAR(100) NOT NULL,
  event_time TIMESTAMP    NOT NULL
);