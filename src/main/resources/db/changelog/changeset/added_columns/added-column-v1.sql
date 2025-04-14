-- liquibase formatted sql

-- changeset sergeev:add-is-active-column-step1
ALTER TABLE users ADD COLUMN is_active BOOLEAN;

-- changeset sergeev:set-is-active-values-step2
UPDATE users SET is_active = TRUE WHERE is_active IS NULL;

-- changeset sergeev:make-is-active-not-null-step3
ALTER TABLE users ALTER COLUMN is_active SET NOT NULL;

-- changeset sergeev:make-is-active-not-null-step4
ALTER TABLE users ALTER COLUMN is_active SET DEFAULT TRUE;