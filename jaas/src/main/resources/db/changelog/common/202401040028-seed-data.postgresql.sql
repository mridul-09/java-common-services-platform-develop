-- liquibase formatted sql

--changeset siba:202401040028-seed-data-1
--comment: addition of global facility

INSERT INTO facilities (id, created_at, created_by, modified_at, modified_by, archived, name, organisations_id,
                        time_format, date_format, date_time_format)
VALUES (-1, 1704308349, 1, 1704308349, 1, false, 'Global Portal', 1, 'HH:mm:ss', 'MMM dd, yyyy', 'MMM dd, yyyy HH:mm:ss');
