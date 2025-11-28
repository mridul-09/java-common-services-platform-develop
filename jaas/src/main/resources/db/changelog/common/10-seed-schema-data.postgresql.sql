-- liquibase formatted sql

--changeset mokshesh:10-seed-schema-data-1
--comment: updating login_page, registration_page, reset_password_page to relative paths
UPDATE services
SET login_page          = 'auth/login',
    registration_page   = 'auth/register',
    reset_password_page = 'auth/forgot-password/secret-key'
WHERE id = 'c6d8285b72a84efb8fbd608c7cada484';

--changeset mokshesh:10-seed-schema-data-2
--comment: rename column login_page to login_path;
ALTER TABLE services
  RENAME COLUMN login_page TO login_path;

--changeset mokshesh:10-seed-schema-data-3
--comment: rename column registration_page to registration_path;
ALTER TABLE services
  RENAME COLUMN registration_page TO registration_path;

--changeset mokshesh:10-seed-schema-data-4
--comment: rename column login_page to reset_password_path;
ALTER TABLE services
  RENAME COLUMN reset_password_page TO reset_password_path;

--changeset mokshesh:10-seed-schema-data-5
--comment: set fqdn column to not null
ALTER TABLE organisation_services_mapping
  ALTER COLUMN fqdn SET NOT NULL;

--changeset mokshesh:10-seed-schema-data-6
--comment: rename the column, change it data type & update the error code range for c6d8285b72a84efb8fbd608c7cada484
ALTER TABLE services
  ALTER COLUMN error_code_id TYPE varchar(45);
ALTER TABLE services
  RENAME error_code_id TO error_code_range;
UPDATE services
SET error_code_range = '10xxxx'
WHERE id = 'c6d8285b72a84efb8fbd608c7cada484'