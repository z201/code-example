CREATE TABLE IF NOT EXISTS account
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_enable     BOOLEAN NOT NULL DEFAULT TRUE,
    create_time   BIGINT  NOT NULL,
    update_time   BIGINT  NOT NULL,
    phone_number  VARCHAR(50)      DEFAULT '',
    email         VARCHAR(50)      DEFAULT '',
    salt_password VARCHAR(225)     DEFAULT '',
    salt          VARCHAR(225)     DEFAULT '',
    usr_name      VARCHAR(100)     DEFAULT '',
    CONSTRAINT phone_nuique UNIQUE (phone_number)
);