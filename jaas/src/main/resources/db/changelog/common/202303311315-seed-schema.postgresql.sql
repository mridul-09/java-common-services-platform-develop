-- liquibase formatted sql

--changeset siba:202303311315-seed-schema-1
--comment: add checklist publisher role
UPDATE public.roles
SET name = 'PROCESS_PUBLISHER'
WHERE id = 6;
