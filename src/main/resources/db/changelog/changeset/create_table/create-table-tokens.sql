-- liquibase formatted sql

-- changeset sergeev:create-table-tokens

CREATE TABLE IF NOT EXISTS tokens (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id bigint NOT NULL,
    token VARCHAR NOT NULL,
    session_id UUID NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_user_tokens_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE

);

-- rollback DROP TABLE IF EXISTS tokens CASCADE;
