CREATE TABLE user
(
    id          INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name  VARCHAR(50) NOT NULL,
    last_name   VARCHAR(50) NOT NULL
);

CREATE TABLE address
(
    id      INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INT REFERENCES user (id) NOT NULL,
    street  VARCHAR(50),
    city    VARCHAR(50),
    state   VARCHAR(50),
    zip     VARCHAR(10)
)