CREATE TABLE user(
    id BIGINT,
    name varchar(32) NOT NULL,
    passord varchar(64) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE user_group(
    id BIGINT,
    name varchar(16),
    PRIMARY KEY (id)
);
CREATE TABLE group_power(
    gid BIGINT,
    power varchar(16),
    PRIMARY KEY (gid, power)
)