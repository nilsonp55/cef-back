--liquibase formatted sql
--changeset prv_nparra:0004 

DROP VIEW IF EXISTS controlefect.v_tarifas_especiales_cliente;

ALTER TABLE controlefect.tarifas_especiales_cliente ALTER COLUMN fecha_creacion TYPE timestamp USING fecha_creacion::timestamp;
ALTER TABLE controlefect.tarifas_especiales_cliente ALTER COLUMN fecha_modificacion TYPE timestamp USING fecha_modificacion::timestamp;

CREATE OR REPLACE VIEW controlefect.v_tarifas_especiales_cliente
AS SELECT tec.id_tarifa_especial,
    tec.codigo_banco,
    bco.abreviatura,
    bco.nombre_banco,
    tec.codigo_tdv,
    tra.nombre_transportadora,
    tec.codigo_cliente,
    cli.identificacion,
    cli.tipoid,
    cli.nombre_cliente,
        CASE
            WHEN tec.codigo_dane IS NULL THEN 'TODAS'::character varying
            ELSE tec.codigo_dane
        END AS codigo_dane,
        CASE
            WHEN ciu.nombre_ciudad IS NULL THEN 'TODAS'::character varying
            ELSE ciu.nombre_ciudad
        END AS nombre_ciudad,
        CASE
            WHEN tec.codigo_punto IS NULL THEN 0
            ELSE tec.codigo_punto
        END AS codigo_punto,
        CASE
            WHEN pts.nombre_punto IS NULL THEN 'TODOS'::character varying
            ELSE pts.nombre_punto
        END AS nombre_punto,
        CASE
            WHEN pts.tipo_punto IS NULL THEN 'TODOS'::character varying
            ELSE pts.tipo_punto
        END AS tipo_punto,
    tec.tipo_operacion,
    tec.tipo_servicio,
    tec.tipo_comision,
    tec.unidad_cobro,
    tec.escala,
    tec.billetes,
    tec.monedas,
    tec.fajado,
    tec.valor_tarifa,
    tec.fecha_inicio_vigencia,
    tec.fecha_fin_vigencia,
    tec.limite_comision_aplicar,
    tec.valor_comision_adicional,
    tec.usuario_creacion,
    tec.fecha_creacion,
    tec.usuario_modificacion,
    tec.fecha_modificacion,
    tec.estado,
        CASE
            WHEN tec.fecha_inicio_vigencia >= date_trunc('month'::text, now() - '1 mon'::interval) AND tec.fecha_fin_vigencia >= date_trunc('month'::text, now() - '1 mon'::interval) THEN 'EDICION_COMPLETA'::text
            WHEN tec.fecha_inicio_vigencia < date_trunc('month'::text, now() - '1 mon'::interval) AND tec.fecha_fin_vigencia >= date_trunc('month'::text, now() - '1 mon'::interval) THEN 'EDICION_PARCIAL'::text
            WHEN tec.fecha_inicio_vigencia < date_trunc('month'::text, now() - '1 mon'::interval) AND tec.fecha_fin_vigencia < date_trunc('month'::text, now() - '1 mon'::interval) THEN 'NO_EDITABLE'::text
            ELSE 'ESTADO_NO_DEFINIDO'::text
        END AS regla_edicion
   FROM tarifas_especiales_cliente tec
     JOIN bancos bco ON tec.codigo_banco = bco.codigo_punto
     JOIN transportadoras tra ON tec.codigo_tdv::text = tra.codigo::text
     JOIN clientes_corporativos cli ON tec.codigo_cliente = cli.codigo_cliente
     LEFT JOIN ciudades ciu ON tec.codigo_dane::text = ciu.codigo_dane::text
     LEFT JOIN puntos pts ON tec.codigo_punto = pts.codigo_punto;
     