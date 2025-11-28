-- liquibase formatted sql

--changeset siba:202310260121-seed-data-1
--comment: update user type values from AZURE_AD to AZURE

update public.users
set type = 'AZURE'
where type = 'AZURE_AD';
