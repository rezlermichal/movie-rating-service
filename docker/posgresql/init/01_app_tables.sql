DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authorities;

CREATE TABLE users
(
    email VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled  INT          NOT NULL DEFAULT 1,
    PRIMARY KEY (email)
);

CREATE TABLE authorities
(
    email     VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (email) REFERENCES users (email)
);

CREATE TABLE movie
(
    id       BIGINT       NOT NULL,
    version  BIGINT       NOT NULL,
    name     VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id)
);