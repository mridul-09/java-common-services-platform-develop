-- liquibase formatted sql

--changeset siba:202207311814-seed-schema-1
--comment: Add extras column
ALTER TABLE organisation_settings
    ADD COLUMN IF NOT EXISTS extras jsonb default '{}';

--changeset siba:202207311814-seed-schema-2
--comment: Add column for user type (LOCAL / AZURE_AD)
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS type varchar(15) default 'LOCAL';
