CREATE TABLE user_groups (
    id        bigserial PRIMARY KEY,
    username  varchar(255) NOT NULL REFERENCES users (username),
    groupname varchar(255) NOT NULL REFERENCES groups (groupname)
);
