-- liquibase formatted sql

--changeset siba:202312181806-seed-schema-1
--comment: addition of client credential table

CREATE TABLE IF NOT EXISTS client_credentials
(
  id                 VARCHAR(255) NOT NULL,
  client_secret      VARCHAR(255) NOT NULL,
  client_name        VARCHAR(50)  NOT NULL,
  client_description VARCHAR(255),
  archived           BOOLEAN      NOT NULL DEFAULT false,
  CONSTRAINT client_credentials_pkey PRIMARY KEY (id)
);

--changeset siba:202312181806-seed-schema-2
--comment: addition of client facility mapping table

CREATE TABLE client_facility_mapping
(
  client_id     VARCHAR(255) NOT NULL,
  facilities_id BIGINT       NOT NULL,
  created_by    BIGINT       NOT NULL,
  modified_by   BIGINT       NOT NULL,
  created_at    BIGINT       NOT NULL,
  modified_at   BIGINT       NOT NULL,

  FOREIGN KEY (created_by) REFERENCES users (id),
  FOREIGN KEY (modified_by) REFERENCES users (id),
  PRIMARY KEY (client_id, facilities_id),
  FOREIGN KEY (client_id) REFERENCES client_credentials (id),
  FOREIGN KEY (facilities_id) REFERENCES facilities (id)
);


