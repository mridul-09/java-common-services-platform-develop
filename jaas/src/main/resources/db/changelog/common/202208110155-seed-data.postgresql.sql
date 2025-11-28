-- liquibase formatted sql

--changeset mokshesh:202208110155-seed-data-1
--comment: add bot user
INSERT INTO users(id, created_at, created_by, modified_at, modified_by, archived, locked_at, department, email,
                  employee_id, failed_login_attempts, first_name, is_system_user, last_name, organisations_id,
                  "password", password_updated_at, username, state, failed_additional_verification_attempts,
                  challenge_questions_id, challenge_questions_answer, failed_challenge_question_attempts)
VALUES (2, 0, 1, 0, 1, true, null, null, 'bot@leucinetech.com', '2', 0, 'Leucine Bot', true, null, 1, null, null, 'bot', 'UNREGISTERED', 0,
        null, null, 0);
