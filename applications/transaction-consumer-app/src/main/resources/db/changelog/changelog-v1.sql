-- liquibase formatted sql
-- changeset liquibaseuser:1


create table pos_transactions(
id text PRIMARY KEY,
data JSONB,
created_ts TIMESTAMP NOT NULL DEFAULT now());
-- rollback drop table pos_transactions;
