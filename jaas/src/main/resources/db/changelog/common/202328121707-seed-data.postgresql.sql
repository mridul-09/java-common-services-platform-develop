-- liquibase formatted sql

--changeset siba:202328121707-seed-data-1
--comment: conversion of user type for bot user

update users
set type = 'SYSTEM'
where id = 2;

