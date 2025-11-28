-- liquibase formatted sql

-- changeset mokshesh:06-seed-schema-1
ALTER TABLE users
  ADD COLUMN state VARCHAR(50);

UPDATE users
SET state = 'REGISTERED'
WHERE verified = true;

UPDATE users
SET state = 'UNREGISTERED'
WHERE verified = false;

UPDATE users
SET state = 'REGISTERED_LOCKED'
WHERE verified = true
  AND blocked = true;

UPDATE users
SET state = 'UNREGISTERED_LOCKED'
WHERE verified = false
  AND blocked = true;

UPDATE users
SET state = 'PASSWORD_EXPIRED'
WHERE verified = true
  AND is_password_reset_required = true;

-- changeset mokshesh:06-seed-schema-2
CREATE INDEX id1KNy5wcThRgIO949VsWZJSOfu9 ON users (state, archived);

-- changeset mokshesh:06-seed-schema-3
ALTER TABLE users
  DROP COLUMN is_password_reset_required;

-- changeset mokshesh:06-seed-schema-4
ALTER TABLE users
  DROP COLUMN blocked;

-- changeset mokshesh:06-seed-schema-5
ALTER TABLE users
  DROP COLUMN verified;

-- changeset mokshesh:06-seed-schema-6
ALTER TABLE users
  ALTER COLUMN failed_login_attempts TYPE INTEGER;

-- changeset mokshesh:06-seed-schema-7
ALTER TABLE users
  ALTER COLUMN failed_login_attempts SET DEFAULT 0;

-- changeset mokshesh:06-seed-schema-8
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS failed_additional_verification_attempts INTEGER DEFAULT 0;

-- changeset mokshesh:06-seed-schema-9
ALTER TABLE organisation_settings
  ADD COLUMN IF NOT EXISTS max_failed_additional_verification_attempts INTEGER DEFAULT 3;

-- changeset mokshesh:06-seed-schema-10
ALTER TABLE organisation_settings
  ADD COLUMN IF NOT EXISTS max_failed_additional_verification_attempts INTEGER DEFAULT 3;

-- changeset mokshesh:06-seed-schema-11
CREATE TABLE IF NOT EXISTS challenge_questions
(
  id          INTEGER      NOT NULL,
  question    VARCHAR(512) NOT NULL,
  archived    BOOLEAN      NOT NULL DEFAULT FALSE,
  created_at  BIGINT       NOT NULL,
  created_by  BIGINT,
  modified_at BIGINT       NOT NULL,
  modified_by BIGINT,
  CONSTRAINT challenge_questions_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:06-seed-schema-12
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS challenge_questions_id INTEGER;

-- changeset mokshesh:06-seed-schema-13
ALTER TABLE users
  ADD CONSTRAINT fkyec8zrg17g5a49v1p817t9br7 FOREIGN KEY (challenge_questions_id) REFERENCES challenge_questions (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:06-seed-schema-14
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS challenge_questions_answer VARCHAR(255);

-- changeset mokshesh:06-seed-schema-15
ALTER TABLE users
  RENAME COLUMN blocked_at TO locked_at;

-- changeset mokshesh:06-seed-schema-16
ALTER TABLE organisation_settings
  RENAME COLUMN auto_unblock_after TO auto_unlock_after;

-- changeset mokshesh:06-seed-schema-17
ALTER TABLE user_audits
  ADD COLUMN IF NOT EXISTS facilities_id BIGINT;

-- changeset mokshesh:06-seed-schema-18
ALTER TABLE user_audits
  DROP COLUMN old_data;

-- changeset mokshesh:06-seed-schema-19
ALTER TABLE user_audits
  DROP COLUMN new_data;

-- changeset mokshesh:06-seed-schema-20
ALTER TABLE user_audits
  DROP COLUMN diff_data;

-- changeset mokshesh:06-seed-schema-21
ALTER TABLE user_audits
  DROP COLUMN facilities_id;

-- changeset mokshesh:06-seed-schema-22
ALTER TABLE user_audits
  ADD COLUMN facility_ids TEXT[];

-- changeset mokshesh:06-seed-schema-23
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS failed_challenge_question_attempts INTEGER DEFAULT 0;

-- changeset mokshesh:06-seed-schema-24
ALTER TABLE organisation_settings
  ADD COLUMN IF NOT EXISTS max_failed_challenge_question_attempts INTEGER DEFAULT 3;

-- changeset mokshesh:06-seed-schema-25
ALTER TABLE password_history
  ADD CONSTRAINT fkcbc78f28bf7a4e3286b1299546792bbb FOREIGN KEY (users_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:06-seed-schema-26
ALTER TABLE users
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-27
ALTER TABLE users
  ALTER COLUMN is_system_user SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-28
ALTER TABLE permissions
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-29
ALTER TABLE organisations
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-30
ALTER TABLE organisations
  ALTER COLUMN is_master SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-31
ALTER TABLE services
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-32
ALTER TABLE scopes
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-33
ALTER TABLE scope_groups
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-34
ALTER TABLE password_policies
  ALTER COLUMN allow_password_similar_to_username_or_email SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-35
ALTER TABLE roles
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-36
ALTER TABLE facilities
  ALTER COLUMN archived SET DEFAULT false;

-- changeset mokshesh:06-seed-schema-37
ALTER TABLE facilities
  ALTER COLUMN archived SET DEFAULT false;

