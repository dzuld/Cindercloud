CREATE TABLE login_events (
  id         SERIAL PRIMARY KEY,
  event_time TIMESTAMP    NOT NULL,
  wallet     VARCHAR(100) NOT NULL
);