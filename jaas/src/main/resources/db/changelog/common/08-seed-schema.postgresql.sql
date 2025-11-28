-- liquibase formatted sql

-- changeset mokshesh:08-seed-schema-1
-- comment create license table
CREATE TABLE IF NOT EXISTS licenses
(
  id                        BIGINT       NOT NULL,
  organisations_id          BIGINT       NOT NULL,
  facilities_id             BIGINT       NOT NULL,
  product                   VARCHAR(150) NULL,
  "type"                    VARCHAR(150) NULL,
  subscription_start_date   DATE         NULL,
  subscription_period       INTEGER      NULL,
  subscription_renewal_date DATE         NULL,
  payment_done              BOOLEAN      NULL,
  intimate_before           INTEGER      NULL,
  grace_period              INTEGER      NULL,
  workflow                  VARCHAR(150) NULL,
  archived                  BOOLEAN      NOT NULL DEFAULT FALSE,
  created_by                BIGINT       NULL,
  created_at                BIGINT       NOT NULL,
  modified_by               BIGINT       NULL,
  modified_at               BIGINT       NOT NULL,
  CONSTRAINT licenses_pkey PRIMARY KEY (id)
);

ALTER TABLE licenses
  ADD CONSTRAINT fkjf3j5j0qhv6tp11kmjxqr768r FOREIGN KEY (organisations_id) REFERENCES organisations (id);
ALTER TABLE licenses
  ADD CONSTRAINT fkf2x3f12xs70xmjpiennvk2lnb FOREIGN KEY (facilities_id) REFERENCES facilities (id);
ALTER TABLE licenses
  ADD CONSTRAINT fkenetfi3frnisc5c30ylvxakmg FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE licenses
  ADD CONSTRAINT fkqgywisinopig7xdsg02epdy2m FOREIGN KEY (modified_by) REFERENCES users (id);
