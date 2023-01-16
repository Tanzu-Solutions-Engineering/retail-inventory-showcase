-- liquibase formatted sql
-- changeset liquibaseuser:1


create table pos_transactions(
id text PRIMARY KEY,
data JSONB,
created_ts TIMESTAMP NOT NULL DEFAULT now());
-- rollback drop table pos_transactions;

-- changeset liquibaseuser:2
create table pos_orders(
id text PRIMARY KEY,
store_id text NOT NULL,
product_id text NOT NULL,
created_ts TIMESTAMP NOT NULL DEFAULT now(),
delivered_ts TIMESTAMP
);
-- rollback drop table pos_orders;
