--liquibase formatted sql
--changeset prv_nparra:0003 failOnError:false

ALTER TABLE controlefect.menu ADD COLUMN IF NOT EXISTS codigo_proceso VARCHAR(100);
ALTER TABLE controlefect.menu ADD COLUMN IF NOT EXISTS base_path VARCHAR (255);
ALTER TABLE controlefect.menu ADD COLUMN IF NOT EXISTS es_proceso BOOLEAN;