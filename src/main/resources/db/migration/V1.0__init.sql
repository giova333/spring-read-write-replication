CREATE TABLE users
(
    id       bigint(20) AUTO_INCREMENT,
    username varchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;