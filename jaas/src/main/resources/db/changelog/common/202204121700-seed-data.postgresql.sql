-- liquibase formatted sql

--changeset sathyam:202204121700-seed-data-1
--comment: add checklist publisher role
INSERT INTO roles (id, created_at, created_by, modified_at, modified_by, archived, name, services_id)
VALUES (6, 0, 1, 0, 1, false, 'CHECKLIST_PUBLISHER', 'c6d8285b72a84efb8fbd608c7cada484')
