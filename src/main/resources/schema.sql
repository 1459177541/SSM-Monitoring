CREATE TABLE t_user(
    id BIGINT,
    name VARCHAR(32) NOT NULL,
    password VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE t_power(
    uid BIGINT NOT NULL ,
    name VARCHAR(32) NOT NULL ,
    PRIMARY KEY (uid, name)
);