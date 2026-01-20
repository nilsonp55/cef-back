--liquibase formatted sql
--changeset prv_nparra:0004 

DROP VIEW IF EXISTS controlefect.v_tarifas_especiales_cliente;

DROP TABLE IF EXISTS controlefect.tarifas_especiales_cliente;
CREATE TABLE IF NOT EXISTS controlefect.tarifas_especiales_cliente (
	id_tarifa_especial bigserial NOT NULL,
	id_archivo_cargado int4 NULL,
	id_registro int4 NULL,
	codigo_banco int4 NOT NULL,
	codigo_tdv varchar(5) NOT NULL,
	codigo_cliente int4 NOT NULL,
	codigo_dane varchar(5) NULL,
	codigo_punto int4 NULL,
	tipo_operacion varchar(50) NOT NULL,
	tipo_servicio varchar(30) NOT NULL,
	tipo_comision varchar(30) NOT NULL,
	unidad_cobro varchar(30) NOT NULL,
	escala varchar(50) NOT NULL,
	billetes varchar(5) NOT NULL,
	monedas varchar(5) NOT NULL,
	fajado varchar(5) NOT NULL,
	valor_tarifa numeric(16, 6) NOT NULL,
	fecha_inicio_vigencia date NOT NULL,
	fecha_fin_vigencia date NOT NULL,
	limite_comision_aplicar int4 NULL,
	valor_comision_adicional numeric(16, 6) NULL,
	usuario_creacion varchar(50) NOT NULL,
	fecha_creacion timestamp NOT NULL,
	usuario_modificacion varchar(50) NULL,
	fecha_modificacion timestamp NULL,
	estado bool DEFAULT true NULL,
	CONSTRAINT chk_billetes_si_no CHECK (((billetes)::text = ANY ((ARRAY['SI'::character varying, 'NO'::character varying])::text[]))),
	CONSTRAINT chk_fajado_si_no CHECK (((fajado)::text = ANY ((ARRAY['SI'::character varying, 'NO'::character varying])::text[]))),
	CONSTRAINT chk_monedas_si_no CHECK (((monedas)::text = ANY ((ARRAY['SI'::character varying, 'NO'::character varying])::text[]))),
	CONSTRAINT tarifas_especiales_cliente_pkey PRIMARY KEY (id_tarifa_especial),
	CONSTRAINT fk_cod_punto FOREIGN KEY (codigo_punto) REFERENCES controlefect.puntos(codigo_punto),
	CONSTRAINT fk_codigo_banco FOREIGN KEY (codigo_banco) REFERENCES controlefect.bancos(codigo_punto),
	CONSTRAINT fk_codigo_cliente FOREIGN KEY (codigo_cliente) REFERENCES controlefect.clientes_corporativos(codigo_cliente),
	CONSTRAINT fk_codigo_dane FOREIGN KEY (codigo_dane) REFERENCES controlefect.ciudades(codigo_dane),
	CONSTRAINT fk_codigo_tdv FOREIGN KEY (codigo_tdv) REFERENCES controlefect.transportadoras(codigo)
);
CREATE UNIQUE INDEX IF NOT EXISTS tarifas_especiales_cliente_idx ON controlefect.tarifas_especiales_cliente USING btree (codigo_banco, codigo_tdv, codigo_cliente, codigo_dane, codigo_punto, tipo_operacion, tipo_servicio, tipo_comision, escala, fecha_inicio_vigencia, fecha_fin_vigencia);
CREATE UNIQUE INDEX IF NOT EXISTS uq_tarifas_archivo_registro ON controlefect.tarifas_especiales_cliente USING btree (id_archivo_cargado, id_registro);

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
     