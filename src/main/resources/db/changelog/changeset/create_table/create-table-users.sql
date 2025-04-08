-- liquibase formatted sql

-- changeset sergeev:create-table-users

CREATE TABLE users(
    id          bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name  varchar(100)        NOT NULL,
    last_name   varchar(100)        NOT NULL,
    middle_name varchar(100)        NULL,
    login       varchar(255) UNIQUE NULL,
    password    varchar(255)        NULL,
    birth_date  DATE,
    created_at  timestamp           NOT NULL DEFAULT now(),
    updated_at  timestamp           NOT NULL DEFAULT now()
);

-- rollback DROP TABLE IF EXISTS users CASCADE;