-- liquibase formatted sql
-- changeset liquibaseuser:1


create table pos_transactions(
id text PRIMARY KEY,
DATA JSONB);
-- rollback drop table pos_transactions;
