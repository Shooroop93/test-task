-- liquibase formatted sql

-- changeset sergeev:create-table-users

CREATE TABLE users (
    id          bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name   varchar(100)        NOT NULL,
    last_name   varchar(100)        NOT NULL,
    middle_name varchar(100)        NULL,
    birth_date  DATE,
    email       varchar(255) UNIQUE NULL,
    phone       varchar(25) UNIQUE  NULL,
    created_at  timestamp           NOT NULL DEFAULT now(),
    updated_at  timestamp           NOT NULL DEFAULT now()
);

-- rollback DROP TABLE IF EXISTS users CASCADE;