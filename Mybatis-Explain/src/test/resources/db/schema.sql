CREATE TABLE IF NOT EXISTS ACCOUNT (
    ID            BIGINT AUTO_INCREMENT PRIMARY KEY,
    IS_ENABLE     BOOLEAN NOT NULL DEFAULT TRUE,
    CREATE_TIME   BIGINT NOT NULL,
    UPDATE_TIME   BIGINT NOT NULL,
    PHONE_NUMBER  VARCHAR(50) DEFAULT '',
    EMAIL         VARCHAR(50) DEFAULT '',
    SALT_PASSWORD VARCHAR(225) DEFAULT '',
    SALT          VARCHAR(225) DEFAULT '',
    USR_NAME      VARCHAR(100) DEFAULT '',
    CONSTRAINT PHONE_NUIQUE UNIQUE (PHONE_NUMBER)
);