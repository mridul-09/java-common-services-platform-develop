-- liquibase formatted sql

--changeset mokshesh:202209291500-seed-data-1
--comment: add central admin role
INSERT INTO roles (id, created_at, created_by, modified_at, modified_by, archived, name, services_id)
VALUES (7, 0, 1, 0, 1, false, 'CENTRAL_ADMIN', 'c6d8285b72a84efb8fbd608c7cada484');

--changeset mokshesh:202209291500-seed-data-2
--comment: rename central admin to global admin
UPDATE roles SET name = 'GLOBAL_ADMIN' WHERE id = 7;
