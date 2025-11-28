-- liquibase formatted sql

-- changeset mokshesh:03-dev-data-1
-- comment: adding license for Bangalore facility
INSERT INTO licenses
(id, organisations_id, facilities_id, product, "type", subscription_start_date, subscription_period,
 subscription_renewal_date, payment_done, intimate_before, grace_period, workflow, archived, created_by, created_at,
 modified_by, modified_at)
VALUES (1639998661, 1617625401, 1616367801, 'dwi', 'full', '2021-03-21', 365, '2022-03-21', true, 30, 30,
        'NOTIFICATION_UNBLOCKED', false, 1, 1639855643, 1, 1639855643);

-- changeset mokshesh:03-dev-data-2
-- comment: adding license for Mumbai facility
INSERT INTO licenses
(id, organisations_id, facilities_id, product, "type", subscription_start_date, subscription_period,
 subscription_renewal_date, payment_done, intimate_before, grace_period, workflow, archived, created_by, created_at,
 modified_by, modified_at)
VALUES (1639998662, 1617625401, 1616367802, 'dwi', 'full', '2021-12-21', 365, '2022-12-21', true, 30, 30,
        'NOTIFICATION_UNBLOCKED', false, 1, 1639855643, 1, 1639855643);

-- changeset mokshesh:03-dev-data-3
-- comment: adding license for Delhi facility
INSERT INTO licenses
(id, organisations_id, facilities_id, product, "type", subscription_start_date, subscription_period,
 subscription_renewal_date, payment_done, intimate_before, grace_period, workflow, archived, created_by, created_at,
 modified_by, modified_at)
VALUES (1639998663, 1617625401, 1616367803, 'dwi', 'full', '2021-12-19', 365, '2022-12-19', false, 30, 30,
        'NOTIFICATION_UNBLOCKED', false, 1, 1639855643, 1, 1639855643);
