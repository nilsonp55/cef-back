--liquibase formatted sql
--changeset prv_nparra:0002 failOnError:false

ALTER TABLE controlefect.oficinas ADD COLUMN IF NOT EXISTS programa_transporte BOOLEAN DEFAULT TRUE;

COMMENT ON COLUMN controlefect.oficinas.programa_transporte IS
    'Determina las oficinas que no programan, es decir que no hacen la programación de los servicios, false=no programan';
