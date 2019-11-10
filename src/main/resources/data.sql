INSERT INTO
    t_user
VALUES
    (1, 'root', 'root'),
    (2, 'user', 'user');

INSERT INTO
    t_group
VALUES
    (1, 'root'),
    (2, 'unauthorized');

INSERT INTO
    t_user_group
VALUES
    (1, 1),
    (2, 2);

INSERT INTO
    t_group_power
VALUES
    (1, 'index'),
    (1, 'cpu'),
    (1, 'mem'),
    (1, 'file'),
    (1, 'user'),
    (2, 'index'),
    (2, 'user');