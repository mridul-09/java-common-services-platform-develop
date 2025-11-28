-- liquibase formatted sql

--changeset siba:202309181336-seed-schema-1
--comment: added feature flags key in organisation_settings table

ALTER TABLE organisation_settings
  ADD COLUMN feature_flags jsonb DEFAULT '{
    "metabaseReports": false
  }'::jsonb;
