-- liquibase formatted sql

-- changeset sergeev:create-table-user-contacts

CREATE TABLE user_contacts(
    id         bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id    bigint              NOT NULL,
    email      varchar(255) UNIQUE NULL,
    phone      varchar(25) UNIQUE  NULL,
    created_at timestamp           NOT NULL DEFAULT now(),
    updated_at timestamp           NOT NULL DEFAULT now(),

    CONSTRAINT fk_user_contacts_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- rollback DROP TABLE IF EXISTS user_contacts CASCADE;