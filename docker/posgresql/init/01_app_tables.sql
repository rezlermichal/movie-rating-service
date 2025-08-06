DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS authorities;
DROP SEQUENCE IF EXISTS rating_sequence;


CREATE TABLE users
(
    id       BIGINT       NOT NULL    PRIMARY KEY,
    version  BIGINT       NOT NULL,
    email    VARCHAR(50)  NOT NULL    UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled  INT          NOT NULL    DEFAULT 1
);

CREATE INDEX IX1_USERS ON users (email);

CREATE TABLE movie
(
    id       BIGINT       NOT NULL    PRIMARY KEY,
    version  BIGINT       NOT NULL,
    name     VARCHAR(50)  NOT NULL
);

CREATE TABLE rating
(
    id       BIGINT       NOT NULL    PRIMARY KEY,
    version  BIGINT       NOT NULL,
    user_id  BIGINT       NOT NULL,
    movie_id BIGINT       NOT NULL,
    rating   INT          NOT NULL,
    rated_at timestamp    NOT NULL,
    FOREIGN KEY (user_id)  REFERENCES users (id),
    FOREIGN KEY (movie_id) REFERENCES movie (id)
);

CREATE UNIQUE INDEX IX1_RATING ON rating (user_id, movie_id);

CREATE TABLE authorities
(
    email     VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (email) REFERENCES users (email)
);

CREATE SEQUENCE user_sequence START 1 INCREMENT BY 50;
CREATE SEQUENCE movie_sequence START 1 INCREMENT BY 50;
CREATE SEQUENCE rating_sequence START 1 INCREMENT BY 50;
