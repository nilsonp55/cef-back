--liquibase formatted sql
--changeset prv_nparra:0006

ALTER TABLE controlefect.dominio ALTER COLUMN fecha_modificacion TYPE timestamp USING fecha_modificacion::timestamp;
ALTER TABLE controlefect.errores_costos ALTER COLUMN id_errores_costos TYPE int8 USING id_errores_costos::int8;
ALTER TABLE controlefect.estado_conciliacion_parametros_liquidacion ALTER COLUMN id_liquidacion TYPE int8 USING id_liquidacion::int8;
ALTER TABLE controlefect.valores_liquidados ADD IF NOT EXISTS observacion_liquidacion varchar(100) NULL;