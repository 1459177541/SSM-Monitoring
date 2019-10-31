CREATE TABLE t_user(
    id BIGINT,
    name varchar(32) NOT NULL,
    password varchar(64) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE t_group(
    id BIGINT,
    name varchar(16),
    PRIMARY KEY (id)
);
CREATE TABLE t_group_power(
    gid BIGINT,
    power varchar(16),
    PRIMARY KEY (gid, power)
);
CREATE TABLE t_user_group(
    uid BIGINT,
    gid BIGINT,
    PRIMARY KEY (uid, gid)
)