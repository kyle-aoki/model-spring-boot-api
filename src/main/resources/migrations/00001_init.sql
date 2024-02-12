CREATE TABLE users (
    id            bigserial PRIMARY KEY,
    username      varchar(255) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL
);
