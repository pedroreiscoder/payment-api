CREATE SCHEMA IF NOT EXISTS payments_schema;

CREATE TABLE IF NOT EXISTS payments_schema.payments (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    zip_code CHAR(4) NOT NULL,
    card_number BYTEA NOT NULL
);
