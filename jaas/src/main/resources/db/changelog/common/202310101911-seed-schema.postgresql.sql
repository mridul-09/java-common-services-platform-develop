-- liquibase formatted sql

--changeset peesa:202310101911-seed-schema-1
--comment: Add time, date formats to facilities.

ALTER TABLE public.facilities
ADD COLUMN IF NOT EXISTS time_format VARCHAR(50) NOT NULL DEFAULT 'HH:mm',
ADD COLUMN IF NOT EXISTS date_format VARCHAR(50) NOT NULL DEFAULT 'MMM dd, yyyy',
ADD COLUMN IF NOT EXISTS date_time_format VARCHAR(50) NOT NULL DEFAULT 'MMM dd, yyyy HH:mm';
