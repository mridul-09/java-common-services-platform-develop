-- liquibase formatted sql

--changeset mokshesh:09-seed-schema-1
--comment: adding fqdn column to organisation_services_mapping table
ALTER TABLE organisation_services_mapping
  ADD COLUMN fqdn TEXT;