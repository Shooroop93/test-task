-- liquibase formatted sql

-- changeset sergeev:create-table-users-photo.sql

CREATE TABLE users_photo(
    id         bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id    bigint    NOT NULL,
    url_photo  varchar   NOT NULL DEFAULT 'https://images.app.goo.gl/HQ1UfYYT6QnUSm9x5',
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now(),

    CONSTRAINT fk_users_photo_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- rollback DROP TABLE IF EXISTS users_photo CASCADE;