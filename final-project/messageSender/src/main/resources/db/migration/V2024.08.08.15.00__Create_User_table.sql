CREATE TABLE users
(
    id integer generated by default as identity,
    username VARCHAR(50)  NOT NULL PRIMARY KEY,
    password VARCHAR(500) not null,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);


INSERT INTO users (username, password, enabled)
VALUES ('user', '6765cfd280db46d0e6d62275f73edd376c83407d517450d02d2a7cba2c9b961564cf83b87d7e2f5e5e2b75bdc535bc6d', true); --password
INSERT INTO users (username, password, enabled)
VALUES ('admin', '6765cfd280db46d0e6d62275f73edd376c83407d517450d02d2a7cba2c9b961564cf83b87d7e2f5e5e2b75bdc535bc6d', true); --password

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');
