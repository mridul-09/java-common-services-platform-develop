-- liquibase formatted sql

-- changeset sathyam:03-seed-schema-1
CREATE TABLE IF NOT EXISTS email_templates
(
  id      INT                 NOT NULL,
  name    VARCHAR(255) UNIQUE NOT NULL,
  content TEXT                NOT NULL,
  CONSTRAINT email_templates_pkey PRIMARY KEY (id)
);

-- changeset sathyam:03-seed-schema-2
CREATE TABLE IF NOT EXISTS email_audits
(
  id             BIGINT  NOT NULL,
  from_address   TEXT NOT NULL,
  to_addresses   TEXT[],
  body           TEXT,
  subject        TEXT,
  cc             TEXT[],
  bcc            TEXT[],
  retry_attempts SMALLINT,
  max_attempts   SMALLINT,
  created_on     BIGINT NOT NULL,
  CONSTRAINT email_audits_pkey PRIMARY KEY (id)
);
