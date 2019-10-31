INSERT INTO
    t_user
VALUES
    (1, 'root', 'root');

INSERT INTO
    t_group
VALUES
    (1, 'root'),
    (2, 'unauthorized');

INSERT INTO
    t_user_group
VALUES
    (1, 1);