CREATE TABLE session (
    session_id varchar(64) PRIMARY KEY,
    username   varchar(255) NOT NULL REFERENCES users (username)
);
