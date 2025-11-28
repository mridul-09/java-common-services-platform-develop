-- liquibase formatted sql

-- changeset mokshesh:05-seed-schema-1
ALTER TABLE services
  ADD COLUMN IF NOT EXISTS error_code_id INT;

-- changeset mokshesh:05-seed-schema-2
ALTER TABLE services
  ADD CONSTRAINT uk_ilj0qjmo8ypukam8h1c6d0ib9 UNIQUE (error_code_id);

-- changeset mokshesh:05-seed-schema-3
ALTER TABLE password_policies
  ADD COLUMN IF NOT EXISTS password_expiration INT;
UPDATE password_policies
SET password_expiration = 90
WHERE password_expiration IS NULL;

-- changeset mokshesh:05-seed-schema-4
ALTER TABLE password_policies
  ALTER COLUMN organisations_id SET NOT NULL;

-- changeset mokshesh:05-seed-schema-5
ALTER TABLE organisation_settings
  ADD COLUMN IF NOT EXISTS logo_url VARCHAR(255);

-- changeset mokshesh:05-seed-schema-6
ALTER TABLE organisation_settings
  DROP COLUMN IF EXISTS access_token_expiration;

-- changeset mokshesh:05-seed-schema-7
ALTER TABLE organisation_settings
  DROP COLUMN IF EXISTS refresh_token_expiration;

-- changeset mokshesh:05-seed-schema-8
ALTER TABLE organisation_settings
  DROP COLUMN IF EXISTS password_expiration;

-- changeset mokshesh:05-seed-schema-9
ALTER TABLE organisation_settings
  ALTER COLUMN organisations_id SET NOT NULL;

-- changeset mokshesh:05-seed-schema-10
DROP SEQUENCE IF EXISTS email_templates_id_seq;

-- changeset mokshesh:05-seed-schema-11
ALTER TABLE email_templates
  ALTER COLUMN id TYPE INTEGER;

-- changeset mokshesh:05-seed-schema-12
ALTER TABLE users
  ALTER COLUMN is_password_reset_required SET DEFAULT FALSE;
UPDATE users
SET is_password_reset_required = FALSE
WHERE is_password_reset_required IS NULL;

-- changeset mokshesh:05-seed-schema-13
ALTER TABLE users
  ALTER COLUMN is_password_reset_required SET NOT NULL;

-- changeset mokshesh:05-seed-schema-14
TRUNCATE role_permissions_mapping;
DELETE
FROM permissions
WHERE id > 2;