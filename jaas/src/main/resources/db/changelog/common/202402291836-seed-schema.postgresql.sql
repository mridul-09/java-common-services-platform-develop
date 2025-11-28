-- liquibase formatted sql

--changeset peesa:202402291836-seed-schema-1
--comment: Add timeZone to facilities.

ALTER TABLE public.facilities
ADD COLUMN IF NOT EXISTS time_zone VARCHAR(50) NOT NULL DEFAULT 'UTC'
