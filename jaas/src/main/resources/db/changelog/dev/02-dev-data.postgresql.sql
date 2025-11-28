-- liquibase formatted sql

-- changeset mokshesh:02-dev-data-1
-- comment: adding organisation: Leucine II
INSERT INTO organisations (id, created_at, created_by, modified_at, modified_by, archived, name, is_master)
VALUES (1643103402, 1643103402, 1, 1643103402, 1, false, 'Leucine II', false);

-- changeset mokshesh:02-dev-data-2
-- comment: mapping service: cleen-dwi to organisation: Leucine II
INSERT INTO organisation_services_mapping (organisations_id, services_id, fqdn, created_at, created_by)
VALUES (1643103402, 'c6d8285b72a84efb8fbd608c7cada484', 'https://FQDN', 1643103402, 1);

-- changeset mokshesh:02-dev-data-3
-- comment: adding user and assign ACCOUNT_OWNER role to organisation: Leucine II
INSERT INTO users (id, created_at, created_by, modified_at, modified_by, archived, locked_at, department, email,
                   employee_id, failed_login_attempts, first_name, is_system_user,
                   last_name, organisations_id, password, password_updated_at, username, state,
                   failed_additional_verification_attempts, challenge_questions_id,
                   challenge_questions_answer)
VALUES (1643103300, 1643103300, 1, 1643103300, 1, false, NULL, 'SDE-0', 'ao.02@mailinator.com',
        'L-account.owner.02', 0,
        'Mustaq', false, 'L',
        1643103402, '$2a$10$AeGYbHnheoETEl.KBQq89OjJ7m294vT5tAcfTYSBkgqASVRPxX6L.',
        CAST(EXTRACT(EPOCH FROM (SELECT NOW() AT TIME ZONE 'UTC')) AS INTEGER), 'account.owner.02',
        'REGISTERED', 0, 1, 'leucine');

-- changeset mokshesh:02-dev-data-4
-- comment: adding organisation settings for organisation: Leucine II
INSERT INTO organisation_settings (id, created_at, created_by, modified_at, modified_by, auto_unlock_after,
                                   max_failed_login_attempts, registration_token_expiration,
                                   password_reset_token_expiration, session_idle_timeout,
                                   max_failed_additional_verification_attempts, logo_url, organisations_id)
VALUES (1643103301, 1643103301, 1, 1643103301, 1643103300, 15, 3, 60, 60, 10, 3,
        'https://via.placeholder.com/300x300', 1643103402);

-- changeset mokshesh:02-dev-data-5
-- comment: adding password policy for organisation: Leucine II
INSERT INTO password_policies (id, created_at, created_by, modified_at, modified_by,
                               allow_password_similar_to_username_or_email, maximum_password_age,
                               minimum_password_history, minimum_password_length,
                               minimum_lowercase_characters, minimum_numeric_characters, minimum_special_characters,
                               minimum_uppercase_characters,
                               password_expiration, organisations_id)
VALUES (1643103302, 1643103302, 1, 1643103302, 1643103300, false, 129600, 3, 8, 1, 1, 1, 1, 30, 1643103402);

-- changeset mokshesh:02-dev-data-6
-- comment: adding facility: New York, London & Sydney for organisation: Leucine II
INSERT INTO facilities (id, archived, name, organisations_id, created_at, created_by, modified_at, modified_by)
VALUES (1643103501, false, 'New York', 1643103402, 1643103501, 1, 1643103501, 1),
       (1643103502, false, 'London', 1643103402, 1643103502, 1, 1643103502, 1),
       (1643103503, false, 'Sydney', 1643103402, 1643103503, 1, 1643103503, 1);

-- changeset mokshesh:02-dev-data-7
-- comment: mapping users to roles
INSERT INTO user_roles_mapping (roles_id, users_id, created_at, created_by)
VALUES (1, 1643103300, 1643103302, 1);

-- changeset mokshesh:02-dev-data-8
-- comment: mapping user: users to facility
INSERT INTO user_facilities_mapping (facilities_id, users_id, created_at, created_by)
VALUES (1643103501, 1643103300, 1643103501, 1),
       (1643103502, 1643103300, 1643103502, 1),
       (1643103503, 1643103300, 1643103503, 1);