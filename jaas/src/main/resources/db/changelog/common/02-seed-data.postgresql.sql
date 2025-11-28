-- liquibase formatted sql

--changeset mokshesh:02-seed-data-1
--comment: adding organisation: Leucine
INSERT INTO organisations (id, created_at, created_by, modified_at, modified_by, archived, name, is_master)
VALUES (1, 0, 1, 0, 1, false, 'Leucine', true);

--changeset mokshesh:02-seed-data-2
--comment: adding user: system@leucinetech.com for organisation: Leucine
INSERT INTO users (id, created_at, created_by, modified_at, modified_by, archived, blocked, blocked_at, email,
                   employee_id, first_name, is_system_user, last_name,
                   password, password_updated_at, username, verified, organisations_id)
VALUES (1, 0, 1, 0, 1, true, false, null, 'system@leucinetech.com', 'SYSTEM', 'System', true, null, null, null, 'system', false, 1);

--changeset mokshesh:02-seed-data-3
--comment: adding organisation settings for organisation: Leucine
INSERT INTO organisation_settings (id, created_at, created_by, modified_at, modified_by, auto_unblock_after, max_failed_login_attempts,
                                   refresh_token_expiration, registration_token_expiration, password_reset_token_expiration, access_token_expiration,
                                   session_idle_timeout, password_expiration, organisations_id)
VALUES (1, 0, 1, 0, 1, 15, 3, 15, 60, 60, 10, 10, 30, 1);

--changeset mokshesh:02-seed-data-4
--comment: adding password policy for organisation: Leucine
INSERT INTO password_policies (id, created_at, created_by, modified_at, modified_by,
                               allow_password_similar_to_username_or_email, maximum_password_age,
                               minimum_password_history, minimum_password_length, minimum_lowercase_characters,
                               minimum_numeric_characters, minimum_special_characters, minimum_uppercase_characters,
                               organisations_id)
VALUES (1, 0, 1, 0, 1, false, 129600, 3, 8, 1, 1, 1, 1, 1);

--changeset mokshesh:02-seed-data-5
--comment: adding service: cleen-dwi
INSERT INTO services (id, created_at, created_by, modified_at, modified_by, archived, name, login_page, registration_page, reset_password_page)
VALUES ('c6d8285b72a84efb8fbd608c7cada484', 0, 1, 0, 1, false, 'cleen-dwi',
        'https://FQDN/auth/login',
        'https://FQDN/auth/register',
        'https://FQDN/auth/forgot-password/secret-key');

--changeset mokshesh:02-seed-data-6
--comment: adding roles for service: cleen-dwi
INSERT INTO roles (id, created_at, created_by, modified_at, modified_by, archived, name, services_id)
VALUES (1, 0, 1, 0, 1, false, 'ACCOUNT_OWNER', 'c6d8285b72a84efb8fbd608c7cada484'),
       (2, 0, 1, 0, 1, false, 'FACILITY_ADMIN', 'c6d8285b72a84efb8fbd608c7cada484'),
       (3, 0, 1, 0, 1, false, 'SYSTEM_ADMIN', 'c6d8285b72a84efb8fbd608c7cada484'),
       (4, 0, 1, 0, 1, false, 'SUPERVISOR', 'c6d8285b72a84efb8fbd608c7cada484'),
       (5, 0, 1, 0, 1, false, 'OPERATOR', 'c6d8285b72a84efb8fbd608c7cada484');

--changeset mokshesh:02-seed-data-7
--comment: adding permissions service: cleen-dwi
INSERT INTO permissions (id, created_at, created_by, modified_at, modified_by, archived, name, method, path,
                         description, services_id)
VALUES (1, 0, 1, 0, 1, false, 'auth.login', 'POST', 'v?/auth/login', 'Enables user login', 'c6d8285b72a84efb8fbd608c7cada484'),
       (2, 0, 1, 0, 1, false, 'auth.logout', 'POST', 'v?/auth/logout', 'Enables user logout', 'c6d8285b72a84efb8fbd608c7cada484');

--changeset mokshesh:02-seed-data-8
--comment: creating scope groups, scopes & it mapping with roles for service: cleen-dwi
INSERT INTO scope_groups
  (id, created_at, created_by, modified_at, modified_by, archived, name, order_tree, services_id)
VALUES (1, 0, 1, 0, 1, false, 'Administrative Features', 1, 'c6d8285b72a84efb8fbd608c7cada484'),
       (2, 0, 1, 0, 1, false, 'Job Features', 2, 'c6d8285b72a84efb8fbd608c7cada484'),
       (3, 0, 1, 0, 1, false, 'Checklist Feature', 3, 'c6d8285b72a84efb8fbd608c7cada484'),
       (4, 0, 1, 0, 1, false, 'Audit Feature', 4, 'c6d8285b72a84efb8fbd608c7cada484');

INSERT INTO scopes
  (id, created_at, created_by, modified_at, modified_by, archived, name, order_tree, scope_groups_id)
VALUES (1, 0, 1, 0, 1, false, 'Basic Authorization', 1, 1),
       (2, 0, 1, 0, 1, false, 'Users and Permissions', 2, 1),
       (3, 0, 1, 0, 1, false, 'Create and Assign Jobs', 1, 2),
       (4, 0, 1, 0, 1, false, 'Execute and Complete Jobs', 2, 2),
       (5, 0, 1, 0, 1, false, 'Create Prototypes', 1, 3),
       (6, 0, 1, 0, 1, false, 'Review Prototypes', 2, 3),
       (7, 0, 1, 0, 1, false, 'Release Prototypes', 3, 3),
       (8, 0, 1, 0, 1, false, 'User Audits', 1, 4),
       (9, 0, 1, 0, 1, false, 'Jobs Audits', 2, 4),
       (10, 0, 1, 0, 1, false, 'Checklist Audits', 3, 4);

INSERT INTO role_scope_groups_mapping
  (roles_id, scope_groups_id, created_at, created_by)
VALUES (1, 1, 0, 1),
       (1, 2, 0, 1),
       (1, 3, 0, 1);

