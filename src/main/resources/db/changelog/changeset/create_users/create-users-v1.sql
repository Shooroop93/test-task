-- liquibase formatted sql

-- changeset sergeev:create-users-v1

INSERT INTO users (first_name,last_name,middle_name,login,password,birth_date,created_at,updated_at)
VALUES ('admin','admin','admin','admin','$2a$10$8LwG0XhBUZ2M87lJki7iMeCwGciSveDVJO2nkjTFMpe302n2N5Sby', DATE '2025-01-01',now(),now());

-- rollback DELETE FROM users WHERE login = 'admin';
