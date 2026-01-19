--liquibase formatted sql
--changeset prv_nparra:0005
 
ALTER TABLE controlefect.centros_ciudad_ppal ALTER COLUMN banco_aval TYPE int4 USING banco_aval::int4;

DROP VIEW IF EXISTS controlefect.v_detalle_liquidacion_procesamiento;
ALTER TABLE controlefect.costos_procesamiento ALTER COLUMN id_archivo_cargado TYPE int8 USING id_archivo_cargado::int8;
ALTER TABLE controlefect.costos_procesamiento ALTER COLUMN id_liquidacion TYPE int4 USING id_liquidacion::int4;
ALTER TABLE controlefect.costos_procesamiento ALTER COLUMN porcentaje_aiu TYPE int4 USING porcentaje_aiu::int4;
ALTER TABLE controlefect.costos_procesamiento ALTER COLUMN porcentaje_iva TYPE int4 USING porcentaje_iva::int4;

DROP VIEW IF EXISTS controlefect.v_detalle_liquidacion_transporte;
ALTER TABLE controlefect.costos_transporte ALTER COLUMN id_archivo_cargado TYPE int8 USING id_archivo_cargado::int8;
ALTER TABLE controlefect.costos_transporte ALTER COLUMN id_liquidacion TYPE int4 USING id_liquidacion::int4;

ALTER TABLE controlefect.detalles_saldos_fondos ALTER COLUMN denominacion TYPE int4 USING denominacion::int4;
ALTER TABLE controlefect.detalles_saldos_fondos ALTER COLUMN id_saldos_fondos TYPE int8 USING id_saldos_fondos::int8;

ALTER TABLE controlefect.tdv_denomin_cantidad ALTER COLUMN denominacion TYPE int4 USING denominacion::int4;
ALTER TABLE controlefect.tdv_denomin_cantidad ALTER COLUMN cantidad_por_denom TYPE int4 USING cantidad_por_denom::int4;

---------------------------------------------------------------------------------------------------
---------------------------------------  procesamiento --------------------------------------------
---------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW controlefect.v_detalle_liquidacion_procesamiento AS
WITH tbl_parametros_eliminados AS (
  SELECT datos_parametros_liquidacion_costos::JSONB AS parametros_jsonb
  FROM controlefect.estado_conciliacion_parametros_liquidacion
  WHERE estado = 1
    AND datos_parametros_liquidacion_costos IS NOT NULL
    AND TRIM(datos_parametros_liquidacion_costos) <> ''
    AND LEFT(TRIM(datos_parametros_liquidacion_costos), 1) = '{'
),

tbl_valores_liquidados_eliminados AS 
  (SELECT datos_valores_liquidados_proc::jsonb AS valores_jsonb
  FROM controlefect.estado_conciliacion_parametros_liquidacion
  WHERE estado = 1
    AND datos_valores_liquidados_proc IS NOT NULL
    AND TRIM(datos_valores_liquidados_proc) <> ''
    AND LEFT(TRIM(datos_valores_liquidados_proc), 1) = '{'),
   
tbl_parametros_liquidacion_costo AS
  (SELECT (parametros_jsonb->>'idLiquidacionFlat')::integer AS id_liquidacion,
          parametros_jsonb->>'billetesFlat' AS billetes,
          (parametros_jsonb->>'codigoBancoFlat')::integer AS codigo_banco,
          parametros_jsonb->>'codigoTdvFlat' AS codigo_tdv,
          parametros_jsonb->>'escalaFlat' AS escala,
          parametros_jsonb->>'fajadoFlat' AS fajado,
          to_timestamp((parametros_jsonb->>'fechaEjecucionFlat')::bigint / 1000) AT TIME ZONE 'UTC' AS fecha_ejecucion,
          parametros_jsonb->>'monedasFlat' AS monedas,
          (parametros_jsonb->>'numeroBolsasFlat')::integer AS numero_bolsas,
          (parametros_jsonb->>'numeroFajosFlat')::integer AS numero_fajos,
          (parametros_jsonb->>'numeroParadasFlat')::integer AS numero_paradas,
          (parametros_jsonb->>'puntoDestinoFlat')::integer AS punto_destino,
          (parametros_jsonb->>'puntoOrigenFlat')::integer AS punto_origen,
          (parametros_jsonb->>'residuoBilletesFlat')::integer AS residuo_billetes,
          (parametros_jsonb->>'residuoMonedasFlat')::integer AS residuo_monedas,
          (parametros_jsonb->>'seqGrupoFlat')::integer AS seq_grupo,
          parametros_jsonb->>'tipoOperacionFlat' AS tipo_operacion,
          parametros_jsonb->>'tipoPuntoFlat' AS tipo_punto,
          parametros_jsonb->>'tipoServicioFlat' AS tipo_servicio,
          (parametros_jsonb->>'valorBilletesFlat')::double precision AS valor_billetes,
          (parametros_jsonb->>'valorMonedasFlat')::double precision AS valor_monedas,
          (parametros_jsonb->>'valorTotalFlat')::double precision AS valor_total,
          to_timestamp((parametros_jsonb ->> 'fechaConcilia')::bigint / 1000)::date AS fecha_concilia,
          parametros_jsonb->>'entradaSalidaFlat' AS entrada_salida,
          parametros_jsonb->>'codigoPropioTdv' AS codigo_propio_tdv,
          parametros_jsonb->>'nombreCliente' AS nombre_cliente
   FROM tbl_parametros_eliminados),
   
tbl_valores_liquidados AS
  (SELECT (valores_jsonb->>'idValoresLiqFlat')::bigint AS id_valores_liq,
          (valores_jsonb->>'clasificacionFajadoFlat')::double precision AS clasificacion_fajado,
          (valores_jsonb->>'clasificacionNoFajadoFlat')::double precision AS clasificacion_no_fajado,
          (valores_jsonb->>'costoCharterFlat')::double precision AS costo_charter,
          (valores_jsonb->>'costoEmisarioFlat')::double precision AS costo_emisario,
          (valores_jsonb->>'costoFijoParadaFlat')::double precision AS costo_fijo_parada,
          (valores_jsonb->>'costoMonedaFlat')::double precision AS costo_moneda,
          (valores_jsonb->>'costoPaqueteoFlat')::double precision AS costo_paqueteo,
          (valores_jsonb->>'idLiquidacionFlat')::bigint AS id_liquidacion,
          (valores_jsonb->>'milajePorRuteoFlat')::double precision AS milaje_por_ruteo,
          (valores_jsonb->>'milajeVerificacionFlat')::double precision AS milaje_verificacion,
          (valores_jsonb->>'modenaResiduoFlat')::double precision AS moneda_residuo,
          (valores_jsonb->>'tasaAeroportuariaFlat')::double precision AS tasa_aeroportuaria,
          (valores_jsonb->>'idSeqGrupoFlat')::integer AS id_seq_grupo,
          (valores_jsonb->>'billeteResiduoFlat')::double precision AS billete_residuo,
          (valores_jsonb->>'clasificacionMonedaFlat')::double precision AS clasificacion_moneda
   FROM tbl_valores_liquidados_eliminados),
   
pre_factor_subtotal AS 
	(SELECT 
        cp.factor_de_liquidacion,
		cp.codigo_punto_fondo,
        cp.fecha_procesamiento,
        SUM(COALESCE(cp.costo_subtotal, 0)) AS suma_costo_subtotal,
        (ABS(('x' || substring(MD5(CONCAT_WS('|', 
            cp.entidad,
            to_char(cp.fecha_servicio_transporte, 'YYYY-MM-DD'),
            cp.codigo_ciiu_fondo,
            cp.codigo_tdv,
            CASE 
                WHEN cp.tipo_operacion IN ('RECOLECCION', 'RETIRO') 
                THEN CONCAT(cp.codigo_punto_interno, '|', cp.codigo_punto_fondo)
                ELSE CONCAT(cp.codigo_punto_fondo, '|', cp.codigo_punto_interno)
            END,
            cp.entrada_salida
        )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_tdv
        
    FROM controlefect.costos_procesamiento cp
    WHERE cp.tipo_registro != 'INSUMO' AND
	cp.nombre_tipo_servicio = 'ALMACENAMIENTO' AND
    cp.factor_de_liquidacion IN('BILLETE', 'MONEDA')
    GROUP BY 
        cp.entidad,
        cp.fecha_servicio_transporte,
        cp.fecha_procesamiento,
        cp.codigo_ciiu_fondo,
        cp.codigo_tdv,
        cp.entrada_salida,
        cp.codigo_punto_fondo,
        cp.codigo_punto_interno,
        cp.tipo_operacion,
        cp.id_archivo_cargado,
        cp.factor_de_liquidacion),

costos_almacenamiento_tdv AS
	(SELECT 
	    id_llaves_maestro_tdv,
	    codigo_punto_fondo,
	    fecha_procesamiento,
	    COALESCE(MAX(CASE WHEN factor_de_liquidacion = 'BILLETE' THEN suma_costo_subtotal END),0) AS valor_liquidado_billete,
	    COALESCE(MAX(CASE WHEN factor_de_liquidacion = 'MONEDA' THEN suma_costo_subtotal END),0) AS valor_liquidado_moneda
	FROM pre_factor_subtotal
	GROUP BY id_llaves_maestro_tdv,
			 fecha_procesamiento,
			 codigo_punto_fondo),
   
costos_almacenamiento_app AS
	(SELECT 
	    codigo_punto_fondo, 
	    fecha_saldo,
	    COALESCE(MAX(CASE WHEN concepto = 'ALMACENAMIENTO BILLETE' THEN valor_liquidado END),0) AS valor_liquidado_billete,
	    COALESCE(MAX(CASE WHEN concepto = 'ALMACENAMIENTO MONEDA' THEN valor_liquidado END),0) AS valor_liquidado_moneda
	FROM 
	    controlefect.otros_costos_fondo
	GROUP BY 
	    codigo_punto_fondo, 
	    fecha_saldo),

tbl_ctos_proc_consolidado AS 
	(SELECT
	  array_to_string(array_agg(DISTINCT cp.consecutivo_registro), ',') AS consecutivo_registro,
	  COALESCE(cp.id_archivo_cargado,0) AS id_archivo_cargado,
	  array_to_string(array_agg(DISTINCT cp.id_registro), ',') AS id_registro,
	  cp.entidad,
	  cp.fecha_servicio_transporte,
	  cp.fecha_procesamiento,
	  STRING_AGG(DISTINCT cp.identificacion_cliente, ', ') AS identificacion_cliente,
	  STRING_AGG(DISTINCT cp.razon_social, ', ') AS razon_social,
	  STRING_AGG(DISTINCT cp.codigo_punto_cargo, ', ') AS codigo_punto_cargo,
	  STRING_AGG(DISTINCT cp.nombre_punto_cargo, ', ') AS nombre_punto_cargo,
	  cp.codigo_ciiu_fondo,
	  STRING_AGG(DISTINCT cp.ciudad_fondo, ', ') AS ciudad_fondo,
	  STRING_AGG(DISTINCT cp.nombre_tipo_servicio, ', ') AS nombre_tipo_servicio,
	  STRING_AGG(DISTINCT cp.moneda_divisa, ', ') AS moneda_divisa,
	  CAST(SUM(COALESCE(cp.valor_procesado_billete, 0)) AS NUMERIC(20, 2)) AS valor_procesado_billete,
	  CAST(SUM(COALESCE(cp.valor_procesado_moneda, 0)) AS NUMERIC(20, 2)) AS valor_procesado_moneda,
	  CAST(SUM(COALESCE(cp.valor_total_procesado, 0)) AS NUMERIC(20, 2)) AS valor_total_procesado,
	  CAST(SUM(COALESCE(cp.costo_subtotal, 0)) AS NUMERIC(22, 4)) AS costo_subtotal,
	  array_to_string(array_agg(DISTINCT COALESCE(cp.iva,0)), ',') AS iva,
	  SUM(COALESCE(cp.valor_total, 0)) AS valor_total,
	  STRING_AGG(DISTINCT cp.estado_conciliacion, ', ') AS estado_conciliacion,
	  STRING_AGG(DISTINCT cp.estado, ', ') AS estado,
	  STRING_AGG(DISTINCT COALESCE(cp.tipo_transaccion,0)::text, '')::bigint AS tipo_transaccion,
	  cp.codigo_tdv,
	  cp.codigo_punto_interno,
	  cp.codigo_punto_fondo,
	  cp.entrada_salida,
	  cp.tipo_operacion,
	  SUM(COALESCE(cp.clasificacion_fajado,0)) AS clasificacion_fajado,
	  SUM(COALESCE(cp.clasificacion_no_fajado,0)) AS clasificacion_no_fajado,
	  SUM(COALESCE(cp.costo_paqueteo,0)) AS costo_paqueteo,
	  SUM(COALESCE(cp.moneda_residuo,0)) AS moneda_residuo,
	  SUM(COALESCE(cp.billete_residuo,0)) AS billete_residuo,
	  
	  (ABS(('x' || substring(MD5(CONCAT_WS('|', 
		cp.entidad,
		to_char(cp.fecha_servicio_transporte, 'YYYY-MM-DD'),
		cp.codigo_ciiu_fondo,
		cp.codigo_tdv,
		
		CASE 
           WHEN cp.tipo_operacion IN ('RECOLECCION', 'RETIRO') 
           THEN CONCAT(cp.codigo_punto_interno, '|', cp.codigo_punto_fondo)
           ELSE CONCAT(cp.codigo_punto_fondo, '|', cp.codigo_punto_interno)
        END,
		
		cp.entrada_salida
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_tdv,
	  
	  array_to_string(array_agg(DISTINCT cp.id_liquidacion), ',') AS ids_liquidacion

	FROM controlefect.costos_procesamiento cp
	WHERE cp.tipo_registro != 'INSUMO'

	GROUP BY
	  cp.entidad,
	  cp.fecha_servicio_transporte,
	  cp.fecha_procesamiento,
	  cp.codigo_ciiu_fondo,
	  cp.codigo_tdv,
	  cp.entrada_salida,
	  cp.codigo_punto_fondo,
	  cp.codigo_punto_interno,  
	  cp.tipo_operacion,
	  cp.id_archivo_cargado),
	  
tbl_ctos_proc_consolidado_almacenamiento AS 
  (SELECT tcc. *, 
  	COALESCE(ca.valor_liquidado_billete,0) AS valor_liquidado_billete, 
  	COALESCE(ca.valor_liquidado_moneda,0) AS valor_liquidado_moneda,
  	
  	 (ABS((
	    'x' || substring(MD5(CONCAT_WS('|', 
	      TO_CHAR(COALESCE(tcc.clasificacion_fajado, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(tcc.clasificacion_no_fajado, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(tcc.costo_paqueteo, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(tcc.moneda_residuo, 0), 'FM99999999999990.0000'),
	      TO_CHAR(COALESCE(tcc.billete_residuo, 0), 'FM99999999999990.0000'),	      
	      TO_CHAR(COALESCE(tcc.codigo_punto_fondo, 0), 'FM99999999999990'),
	      TO_CHAR(tcc.fecha_procesamiento, 'YYYY-MM-DD'),
	      TO_CHAR(COALESCE(ca.valor_liquidado_billete, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ca.valor_liquidado_moneda, 0), 'FM99999999999990.00')
	      
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_comparador_tdv
	    
	FROM tbl_ctos_proc_consolidado tcc
	LEFT JOIN costos_almacenamiento_tdv ca
    ON tcc.id_llaves_maestro_tdv = ca.id_llaves_maestro_tdv
	AND tcc.codigo_punto_fondo = ca.codigo_punto_fondo
	AND date_trunc('day', tcc.fecha_procesamiento) = date_trunc('day', ca.fecha_procesamiento)),

tbl_aplicativo AS 
	(SELECT DISTINCT
	  b.abreviatura AS entidad,
	  plc.fecha_ejecucion AS fecha_servicio_transporte,
	  plc.tipo_operacion AS tipo_pedido,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_servicio AS nombre_tipo_servicio, -- ESPECIAL/PROGRAMADA
	  p.codigo_ciudad AS codigo_ciudad_fondo,
	  c.nombre_ciudad AS ciudad_fondo,
	  SUM(COALESCE(plc.numero_fajos,0)) AS numero_fajos,
	  STRING_AGG(DISTINCT plc.escala, ', ') AS escala,
	  CAST(SUM(COALESCE(plc.valor_billetes,0)) AS NUMERIC(20, 2)) AS valor_procesado_billete,
	  CAST(SUM(COALESCE(plc.valor_monedas,0)) AS NUMERIC(20, 2)) AS valor_procesado_moneda,
	  CAST(SUM(COALESCE(plc.valor_total,0)) AS NUMERIC(20, 2)) AS valor_total_procesado,
	  SUM(COALESCE(plc.numero_bolsas,0)) AS numero_de_bolsas_moneda,
	  SUM(COALESCE(vl.costo_fijo_parada,0)) AS costo_fijo,
	  SUM(COALESCE(vl.milaje_por_ruteo,0) + COALESCE(vl.milaje_verificacion,0)) AS costo_milaje,
	  SUM(COALESCE(vl.costo_moneda,0)) AS costo_bolsa,
	  SUM(COALESCE(vl.costo_charter,0) + COALESCE(vl.tasa_aeroportuaria,0)) AS costo_flete,
	  SUM(vl.costo_emisario) AS costo_emisario,
	  
	  CAST(SUM(COALESCE(vl.costo_fijo_parada,0) + 
	      COALESCE(vl.milaje_por_ruteo,0) + 
		  COALESCE(vl.milaje_verificacion,0) +
		  COALESCE(vl.costo_moneda,0) + 
		  COALESCE(vl.costo_charter,0) + 
		  COALESCE(vl.tasa_aeroportuaria,0) + 
		  COALESCE(vl.costo_emisario,0)
		 ) AS NUMERIC(22, 4)) AS subtotal,
		 
	  SUM(vl.clasificacion_fajado) AS clasificacion_fajado,
	  SUM(vl.clasificacion_no_fajado) AS clasificacion_no_fajado,
	  SUM(vl.costo_paqueteo) AS costo_paqueteo,
	  SUM(vl.moneda_residuo) AS moneda_residuo,
	  SUM(vl.billete_residuo) AS billete_residuo,
	  STRING_AGG(DISTINCT ca.codigo_punto_fondo::text, ', ') AS codigo_punto_fondo,
	  STRING_AGG(DISTINCT TO_CHAR(ca.fecha_saldo, 'YYYY-MM-DD')::text, ', ') AS fechas_saldo,
	  CAST(SUM(COALESCE(ca.valor_liquidado_billete,0)) AS NUMERIC(28, 4)) AS valor_liquidado_billete,
	  CAST(SUM(COALESCE(ca.valor_liquidado_moneda,0)) AS NUMERIC(28, 4)) AS valor_liquidado_moneda,
	  
	  
	  (ABS(('x' || substring(MD5(CONCAT_WS('|', 
		  b.abreviatura,
		  to_char(plc.fecha_ejecucion, 'YYYY-MM-DD'),
		  p.codigo_ciudad,
		  plc.codigo_tdv,
		  plc.punto_origen,
		  plc.punto_destino,
		  plc.entrada_salida
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_app,
	  
	  array_to_string(array_agg(DISTINCT plc.id_liquidacion), ',') AS ids_liquidacion,
	  
	  (CASE WHEN b.es_aval = true THEN CONCAT('BANCO ',b.nombre_banco) ELSE b.nombre_banco END) AS nombre_banco
	FROM controlefect.parametros_liquidacion_costo plc
	INNER JOIN controlefect.bancos b ON b.codigo_punto = plc.codigo_banco
	LEFT JOIN controlefect.valores_liquidados vl ON vl.id_liquidacion = plc.id_liquidacion

	/* Nuevos joins para obtener la ciudad fondo
    * ***************************** REGLA *********************************************
    * Si tipo_operacion es 'RECOLECCIÓN' o 'RETIRO' el punto del fondo es punto_destino
    * Si tipo_operacion es 'TRASLADO' o 'VENTA' el punto del fondo es punto_origen
    */ 
	INNER JOIN controlefect.fondos f ON f.tdv = plc.codigo_tdv
	INNER JOIN controlefect.puntos p ON f.codigo_punto = p.codigo_punto AND p.tipo_punto = 'FONDO'
	INNER JOIN controlefect.ciudades c ON c.codigo_dane = p.codigo_ciudad
	INNER JOIN controlefect.bancos b2 ON b2.codigo_punto = f.banco_aval AND b2.abreviatura = b.abreviatura
	INNER JOIN controlefect.puntos punto_relacionado ON punto_relacionado.codigo_punto = 
		CASE 
			WHEN plc.tipo_operacion IN ('RECOLECCION', 'RETIRO') THEN plc.punto_destino
			WHEN plc.tipo_operacion IN ('TRASLADO', 'VENTA') THEN plc.punto_origen
		END
	LEFT JOIN costos_almacenamiento_app ca ON ca.codigo_punto_fondo = f.codigo_punto
	WHERE p.codigo_ciudad = punto_relacionado.codigo_ciudad

	GROUP BY
	  b.abreviatura,
	  plc.fecha_ejecucion,
	  plc.tipo_servicio,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_operacion,
	  b.es_aval,
	  b.nombre_banco,
	  p.codigo_ciudad,
	  c.nombre_ciudad
	HAVING 
	  SUM(COALESCE(vl.clasificacion_fajado, 0) +
	      COALESCE(vl.clasificacion_no_fajado, 0) +
	      COALESCE(vl.costo_paqueteo, 0) +
	      COALESCE(vl.moneda_residuo, 0) +
	      COALESCE(vl.billete_residuo, 0)) > 0),
		  
tbl_aplicativo_eliminadas AS 
	 (SELECT DISTINCT
	  b.abreviatura AS entidad,
	  plc.fecha_ejecucion AS fecha_servicio_transporte,
	  plc.tipo_operacion AS tipo_pedido,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_servicio AS nombre_tipo_servicio, -- ESPECIAL/PROGRAMADA
	  p.codigo_ciudad AS codigo_ciudad_fondo,
	  c.nombre_ciudad AS ciudad_fondo,
	  SUM(COALESCE(plc.numero_fajos,0)) AS numero_fajos,
	  STRING_AGG(DISTINCT plc.escala, ', ') AS escala,
	  SUM(COALESCE(plc.valor_billetes,0)) AS valor_procesado_billete,
	  SUM(COALESCE(plc.valor_monedas,0)) AS valor_procesado_moneda,
	  SUM(COALESCE(plc.valor_total,0)) AS valor_total_procesado,
	  SUM(COALESCE(plc.numero_bolsas,0)) AS numero_de_bolsas_moneda,
	  SUM(COALESCE(vl.costo_fijo_parada,0)) AS costo_fijo,
	  SUM(COALESCE(vl.milaje_por_ruteo,0) + COALESCE(vl.milaje_verificacion,0)) AS costo_milaje,
	  SUM(COALESCE(vl.costo_moneda,0)) AS costo_bolsa,
	  SUM(COALESCE(vl.costo_charter,0) + COALESCE(vl.tasa_aeroportuaria,0)) AS costo_flete,
	  SUM(vl.costo_emisario) AS costo_emisario,
	  
	  CAST(SUM(COALESCE(vl.costo_fijo_parada,0) + 
	      COALESCE(vl.milaje_por_ruteo,0) + 
		  COALESCE(vl.milaje_verificacion,0) +
		  COALESCE(vl.costo_moneda,0) + 
		  COALESCE(vl.costo_charter,0) + 
		  COALESCE(vl.tasa_aeroportuaria,0) + 
		  COALESCE(vl.costo_emisario,0)
		 ) AS NUMERIC(22, 4)) AS subtotal,
		
	  SUM(vl.clasificacion_fajado) AS clasificacion_fajado,
	  SUM(vl.clasificacion_no_fajado) AS clasificacion_no_fajado,
	  SUM(vl.costo_paqueteo) AS costo_paqueteo,
	  SUM(vl.moneda_residuo) AS moneda_residuo,
	  SUM(vl.billete_residuo) AS billete_residuo,
	  STRING_AGG(DISTINCT ca.codigo_punto_fondo::text, ', ') AS codigo_punto_fondo,
	  STRING_AGG(DISTINCT TO_CHAR(ca.fecha_saldo, 'YYYY-MM-DD')::text, ', ') AS fechas_saldo,
	  CAST(SUM(COALESCE(ca.valor_liquidado_billete,0)) AS NUMERIC(28, 4)) AS valor_liquidado_billete,
	  CAST(SUM(COALESCE(ca.valor_liquidado_moneda,0)) AS NUMERIC(28, 4)) AS valor_liquidado_moneda,
	  
	  
	  (ABS(('x' || substring(MD5(CONCAT_WS('|', 
		  b.abreviatura,
		  to_char(plc.fecha_ejecucion, 'YYYY-MM-DD'),
		  p.codigo_ciudad,
		  plc.codigo_tdv,
		  plc.punto_origen,
		  plc.punto_destino,
		  plc.entrada_salida
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_app,
	  
	  array_to_string(array_agg(DISTINCT plc.id_liquidacion), ',') AS ids_liquidacion,
	  
	  (CASE WHEN b.es_aval = true THEN CONCAT('BANCO ',b.nombre_banco) ELSE b.nombre_banco END) AS nombre_banco
	  
	FROM tbl_parametros_liquidacion_costo plc
	INNER JOIN controlefect.bancos b ON b.codigo_punto = plc.codigo_banco
	LEFT JOIN tbl_valores_liquidados vl ON vl.id_liquidacion = plc.id_liquidacion

   /* Nuevos joins para obtener la ciudad fondo
    * ***************************** REGLA *********************************************
    * Si tipo_operacion es 'RECOLECCIÓN' o 'RETIRO' el punto del fondo es punto_destino
    * Si tipo_operacion es 'TRASLADO' o 'VENTA' el punto del fondo es punto_origen
    */ 
	INNER JOIN controlefect.fondos f ON f.tdv = plc.codigo_tdv
	INNER JOIN controlefect.puntos p ON f.codigo_punto = p.codigo_punto AND p.tipo_punto = 'FONDO'
	INNER JOIN controlefect.ciudades c ON c.codigo_dane = p.codigo_ciudad
	INNER JOIN controlefect.bancos b2 ON b2.codigo_punto = f.banco_aval AND b2.abreviatura = b.abreviatura
	INNER JOIN controlefect.puntos punto_relacionado ON punto_relacionado.codigo_punto = 
		CASE 
			WHEN plc.tipo_operacion IN ('RECOLECCION', 'RETIRO') THEN plc.punto_destino
			WHEN plc.tipo_operacion IN ('TRASLADO', 'VENTA') THEN plc.punto_origen
		END
	LEFT JOIN costos_almacenamiento_app ca ON ca.codigo_punto_fondo = f.codigo_punto
	WHERE p.codigo_ciudad = punto_relacionado.codigo_ciudad

	GROUP BY
	  b.abreviatura,
	  plc.fecha_ejecucion,
	  plc.tipo_servicio,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_operacion,
	  b.es_aval,
	  b.nombre_banco,
	  p.codigo_ciudad,
	  c.nombre_ciudad
	HAVING 
	  SUM(COALESCE(vl.clasificacion_fajado, 0) +
	      COALESCE(vl.clasificacion_no_fajado, 0) +
	      COALESCE(vl.costo_paqueteo, 0) +
	      COALESCE(vl.moneda_residuo, 0) +
	      COALESCE(vl.billete_residuo, 0)) > 0),

tbl_aplicativo_almacenamiento AS 
(SELECT *,

	(ABS((
	    'x' || substring(MD5(CONCAT_WS('|', 
	      TO_CHAR(COALESCE(ta.clasificacion_fajado, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.clasificacion_no_fajado, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.costo_paqueteo, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.moneda_residuo, 0), 'FM99999999999990.0000'),
	      TO_CHAR(COALESCE(ta.billete_residuo, 0), 'FM99999999999990.0000'),	      
	      TO_CHAR(COALESCE(ta.codigo_punto_fondo, '0')::numeric, 'FM99999999999990'),
	      ta.fechas_saldo,
	      TO_CHAR(COALESCE(ta.valor_liquidado_billete, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.valor_liquidado_moneda, 0), 'FM99999999999990.00')
	      
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_comparador_app
	  
	FROM tbl_aplicativo ta
),

tbl_aplicativo_eliminadas_almacenamiento AS 
(SELECT *,

	(ABS((
	    'x' || substring(MD5(CONCAT_WS('|', 
	      TO_CHAR(COALESCE(ta.clasificacion_fajado, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.clasificacion_no_fajado, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.costo_paqueteo, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.moneda_residuo, 0), 'FM99999999999990.0000'),
	      TO_CHAR(COALESCE(ta.billete_residuo, 0), 'FM99999999999990.0000'),	      
	      TO_CHAR(COALESCE(ta.codigo_punto_fondo, '0')::numeric, 'FM99999999999990'),
	      ta.fechas_saldo,
	      TO_CHAR(COALESCE(ta.valor_liquidado_billete, 0), 'FM99999999999990.00'),
	      TO_CHAR(COALESCE(ta.valor_liquidado_moneda, 0), 'FM99999999999990.00')
	      
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_comparador_app
	  
	FROM tbl_aplicativo_eliminadas ta
),

bl_prestacion_servicio_procesamiento AS
  (SELECT id_archivo_cargado,
          MIN(fecha_servicio_transporte) AS fecha_minima,
          MAX(fecha_servicio_transporte) AS fecha_maxima
   FROM tbl_ctos_proc_consolidado_almacenamiento
   GROUP BY id_archivo_cargado),
  
tbl_costos_procesamiento AS
  (SELECT cp. *,
          psp.fecha_minima,
          psp.fecha_maxima
   FROM bl_prestacion_servicio_procesamiento psp
   INNER JOIN tbl_ctos_proc_consolidado_almacenamiento cp ON cp.id_archivo_cargado = psp.id_archivo_cargado),

tdl_aplicativo_rango_archivo AS
	  (SELECT ta.*
	   FROM tbl_aplicativo_almacenamiento ta
	   WHERE EXISTS
		   (SELECT 1
			FROM tbl_costos_procesamiento cp
			WHERE date_trunc('day', ta.fecha_servicio_transporte) 
			BETWEEN date_trunc('day', cp.fecha_minima) AND date_trunc('day', cp.fecha_maxima))),
			
tdl_aplicativo_rango_archivo_eliminadas AS
	  (SELECT ta.*
	   FROM tbl_aplicativo_eliminadas_almacenamiento ta
	   WHERE EXISTS
		   (SELECT 1
			FROM tbl_costos_procesamiento cp
			WHERE date_trunc('day', ta.fecha_servicio_transporte) 
			BETWEEN date_trunc('day', cp.fecha_minima) AND date_trunc('day', cp.fecha_maxima))),
			
max_consecutivo AS (
    SELECT MAX(cp.consecutivo_registro) AS max_consecutivo_registro
    FROM controlefect.costos_procesamiento cp
),

modulo_conciliadas AS
  (SELECT DISTINCT cp.consecutivo_registro,
                   cp.id_archivo_cargado,
                   cp.id_registro,
                   ta.ids_liquidacion AS ids_liquidacion_app,
				   cp.ids_liquidacion AS ids_liquidacion_tdv,
                   COALESCE(cp.tipo_transaccion,0) AS tipo_transaccion,
				   COALESCE(ta.nombre_banco, CONCAT('BANCO ', ba.nombre_banco)) AS entidad,
				   cp.fecha_servicio_transporte,
				   cp.identificacion_cliente,
				   cp.razon_social,
				   cp.codigo_punto_cargo,
				   cp.nombre_punto_cargo,
				   cp.ciudad_fondo,
				   cp.nombre_tipo_servicio,
				   cp.tipo_operacion,
				   cp.moneda_divisa,
				 
				  (CASE
					WHEN ta.id_llaves_comparador_app = cp.id_llaves_comparador_tdv THEN 1
					ELSE 0
				  END) AS aplicativo,
				 
				  (CASE
					WHEN ta.id_llaves_comparador_app = cp.id_llaves_comparador_tdv THEN 1
					ELSE 0
				  END) AS tdv,
						   
				   ta.valor_procesado_billete,
				   cp.valor_procesado_billete AS valor_procesado_billete_tdv,
				   ta.valor_procesado_moneda,
				   cp.valor_procesado_moneda AS valor_procesado_moneda_tdv,
				   ta.valor_total_procesado,
				   cp.valor_total_procesado AS valor_total_procesado_tdv,
				   ta.subtotal,
				   cp.costo_subtotal AS subtotal_tdv,
				   cp.iva,
				   cp.valor_total,
				   ta.clasificacion_fajado,
				   cp.clasificacion_fajado AS clasificacion_fajado_tdv,
				   ta.clasificacion_no_fajado,
				   cp.clasificacion_no_fajado AS clasificacion_no_fajado_tdv,
				   ta.costo_paqueteo,
				   cp.costo_paqueteo AS costo_paqueteo_tdv,
				   ta.moneda_residuo,
				   cp.moneda_residuo AS moneda_residuo_tdv,
				   ta.billete_residuo,
				   cp.billete_residuo AS billete_residuo_tdv,
				   ta.valor_liquidado_billete AS valor_almacenamiento_billete,
				   cp.valor_liquidado_billete AS valor_almacenamiento_billete_tdv,
				   ta.valor_liquidado_moneda AS valor_almacenamiento_moneda,
				   cp.valor_liquidado_moneda AS valor_almacenamiento_moneda_tdv,
				 
				  CASE
					WHEN cp.estado_conciliacion IS NULL OR cp.estado_conciliacion = 'PENDIENTE' THEN
					   CASE 
						  WHEN ta.id_llaves_comparador_app = cp.id_llaves_comparador_tdv 
						  THEN 'AUTOMATICO'
						  ELSE 'EN_CONCILIACION'
						END
					ELSE cp.estado_conciliacion
				  END AS estado,
				 
				  CASE
					WHEN ta.id_llaves_comparador_app = cp.id_llaves_comparador_tdv THEN'CONCILIADAS'
					ELSE'IDENTIFICADAS_CON_DIFERENCIAS'
				  END AS modulo,
				  cp.id_llaves_maestro_tdv,
				  ta.id_llaves_maestro_app
           
   FROM tbl_aplicativo_almacenamiento ta
   INNER JOIN tbl_ctos_proc_consolidado_almacenamiento cp ON ta.id_llaves_maestro_app = cp.id_llaves_maestro_tdv
   INNER JOIN controlefect.bancos ba ON ba.abreviatura = cp.entidad
   AND ba.es_aval = TRUE),
   
modulo_remitidas_no_identificadas AS 
   (SELECT DISTINCT cp.consecutivo_registro,
	                cp.id_archivo_cargado,
	                cp.id_registro,
	                ta.ids_liquidacion AS ids_liquidacion_app,
				    cp.ids_liquidacion AS ids_liquidacion_tdv,
	                COALESCE(cp.tipo_transaccion,0) AS tipo_transaccion,
	                COALESCE(ta.nombre_banco, CONCAT('BANCO ', ba.nombre_banco)) AS entidad,
	                cp.fecha_servicio_transporte,
	                cp.identificacion_cliente,
	                cp.razon_social,
	                cp.codigo_punto_cargo,
	                cp.nombre_punto_cargo,
	                cp.ciudad_fondo,
	                cp.nombre_tipo_servicio,
	                cp.tipo_operacion,
	                cp.moneda_divisa,
	                0 AS aplicativo,
	                1 AS tdv,
	                COALESCE(ta.valor_procesado_billete, 0) AS valor_procesado_billete,
	                cp.valor_procesado_billete AS valor_procesado_billete_tdv,
	                COALESCE(ta.valor_procesado_moneda, 0) AS valor_procesado_moneda,
	                cp.valor_procesado_moneda AS valor_procesado_moneda_tdv,
	                COALESCE(ta.valor_total_procesado, 0) AS valor_total_procesado,
	                cp.valor_total_procesado AS valor_total_procesado_tdv,
	                COALESCE(ta.subtotal, 0) AS subtotal,
	                cp.costo_subtotal AS subtotal_tdv,
	                cp.iva,
	                cp.valor_total,
	                COALESCE(ta.clasificacion_fajado, 0) AS clasificacion_fajado,
	                COALESCE(cp.clasificacion_fajado,0) AS clasificacion_fajado_tdv,
	                COALESCE(ta.clasificacion_no_fajado, 0) AS clasificacion_no_fajado,
	                COALESCE(cp.clasificacion_no_fajado,0) AS clasificacion_no_fajado_tdv,
	                COALESCE(ta.costo_paqueteo, 0) AS costo_paqueteo,
	                COALESCE(cp.costo_paqueteo,0) AS costo_paqueteo_tdv,
	                COALESCE(ta.moneda_residuo, 0) AS moneda_residuo,
	                COALESCE(cp.moneda_residuo,0) AS moneda_residuo_tdv,				
					COALESCE(ta.billete_residuo,0) AS billete_residuo,
				    COALESCE(cp.billete_residuo,0) AS billete_residuo_tdv,
				    COALESCE(ta.valor_liquidado_billete,0) AS valor_almacenamiento_billete,
				    COALESCE(cp.valor_liquidado_billete,0) AS valor_almacenamiento_billete_tdv,
				    COALESCE(ta.valor_liquidado_moneda,0) AS valor_almacenamiento_moneda,
				    COALESCE(cp.valor_liquidado_moneda,0) AS valor_almacenamiento_moneda_tdv,
				   
	                CASE
	                    WHEN cp.estado_conciliacion = 'PENDIENTE'
	                         OR cp.estado_conciliacion IS NULL THEN 'EN_CONCILIACION'
	                    ELSE cp.estado_conciliacion
	                END AS estado,
	                'REMITIDAS_NO_IDENTIFICADAS' AS modulo,
					cp.id_llaves_maestro_tdv,
					NULL::BIGINT AS id_llaves_maestro_app
	FROM tbl_aplicativo_almacenamiento ta
	RIGHT JOIN tbl_ctos_proc_consolidado_almacenamiento cp ON ta.id_llaves_maestro_app = cp.id_llaves_maestro_tdv
    INNER JOIN controlefect.bancos ba ON ba.abreviatura = cp.entidad AND ba.es_aval = TRUE
   
	WHERE ta.entidad IS NULL
	  AND (cp.tipo_transaccion IS NULL
	       OR cp.tipo_transaccion != 3)),

-- El resultado de esta consulta depende del rango de fechas (mínima y máxima)
-- definidas en la tabla tbl_costos_transporte. Se consideran únicamente los
-- registros de tbl_aplicativo cuyo campo fecha_servicio_transporte se encuentra
-- dentro de ese rango, lo cual se define en el CTE tdl_aplicativo_rango_archivo.
-- Además, solo se tienen en cuenta los archivos en estado 'EN CONCILIACION'.
modulo_liquidadas_no_cobradas AS (
    SELECT DISTINCT
    	   CAST((ROW_NUMBER() OVER () + mc.max_consecutivo_registro)::TEXT AS VARCHAR) AS consecutivo_registro,
    	   COALESCE(cp.id_archivo_cargado,0) AS id_archivo_cargado,
           cp.id_registro,
           ta.ids_liquidacion AS ids_liquidacion_app,
		   cp.ids_liquidacion AS ids_liquidacion_tdv,
           COALESCE(cp.tipo_transaccion,0) AS tipo_transaccion,
           ta.nombre_banco AS entidad,
           ta.fecha_servicio_transporte::DATE AS fecha_servicio_transporte,
           NULL AS identificacion_cliente,
           NULL AS razon_social,
           NULL AS codigo_punto_cargo,
           NULL AS nombre_punto_cargo,
           ta.ciudad_fondo,
           ta.tipo_pedido AS nombre_tipo_servicio,
           ta.tipo_pedido AS tipo_operacion,
           NULL AS moneda_divisa,
           1 AS aplicativo,
           0 AS tdv,
           CAST(COALESCE(ta.valor_procesado_billete, 0) AS NUMERIC(20, 2)) AS valor_procesado_billete,
           COALESCE(cp.valor_procesado_billete, 0) AS valor_procesado_billete_tdv,
           CAST(COALESCE(ta.valor_procesado_moneda, 0) AS NUMERIC(20, 2)) AS valor_procesado_moneda,
           COALESCE(cp.valor_procesado_moneda, 0) AS valor_procesado_moneda_tdv,
           CAST(COALESCE(ta.valor_total_procesado, 0) AS NUMERIC(20, 2)) AS valor_total_procesado,
           COALESCE(cp.valor_total_procesado, 0) AS valor_total_procesado_tdv,
           COALESCE(ta.subtotal, 0) AS subtotal,
           COALESCE(cp.costo_subtotal,0) AS subtotal_tdv,
           cp.iva,
           COALESCE(ta.valor_total_procesado, 0) AS valor_total,
           COALESCE(ta.clasificacion_fajado, 0) AS clasificacion_fajado,
	       COALESCE(cp.clasificacion_fajado,0) AS clasificacion_fajado_tdv,
	       COALESCE(ta.clasificacion_no_fajado, 0) AS clasificacion_no_fajado,
	       COALESCE(cp.clasificacion_no_fajado,0) AS clasificacion_no_fajado_tdv,
	       COALESCE(ta.costo_paqueteo, 0) AS costo_paqueteo,
	       COALESCE(cp.costo_paqueteo,0) AS costo_paqueteo_tdv,
	       COALESCE(ta.moneda_residuo, 0) AS moneda_residuo,
	       COALESCE(cp.moneda_residuo,0) AS moneda_residuo_tdv,
	       COALESCE(ta.billete_residuo, 0) AS billete_residuo,
	       COALESCE(cp.billete_residuo,0) AS billete_residuo_tdv,
		   COALESCE(ta.valor_liquidado_billete,0) AS valor_almacenamiento_billete,
		   COALESCE(cp.valor_liquidado_billete,0) AS valor_almacenamiento_billete_tdv,
		   COALESCE(ta.valor_liquidado_moneda,0) AS valor_almacenamiento_moneda,
		   COALESCE(cp.valor_liquidado_moneda,0) AS valor_almacenamiento_moneda_tdv,
		   
           'EN_CONCILIACION' AS estado,
           'LIQUIDADAS_NO_COBRADAS' AS modulo,
		   NULL::BIGINT AS id_llaves_maestro_tdv,
		   ta.id_llaves_maestro_app
    FROM tdl_aplicativo_rango_archivo ta
	LEFT JOIN tbl_ctos_proc_consolidado_almacenamiento cp ON ta.id_llaves_maestro_app = cp.id_llaves_maestro_tdv
	CROSS JOIN max_consecutivo mc
    WHERE cp.entidad IS NULL),

max_cons_liq_no_cobradas AS (   
	SELECT MAX(CAST(consecutivo_registro AS INTEGER)) AS max_consecutivo_registro
	FROM (
	    SELECT
	        CAST((ROW_NUMBER() OVER () + mc.max_consecutivo_registro)::TEXT AS VARCHAR) AS consecutivo_registro
	    FROM
	        modulo_liquidadas_no_cobradas
	        CROSS JOIN max_consecutivo mc
	) AS subquery),

modulo_liquidadas_no_cobradas_eliminadas AS
	(SELECT
	  CAST((ROW_NUMBER() OVER () + mclc.max_consecutivo_registro)::TEXT AS VARCHAR) AS consecutivo_registro,
	  subquery.*
	FROM (
	  SELECT DISTINCT 
	    COALESCE(cp.id_archivo_cargado,0) AS id_archivo_cargado,
        cp.id_registro,
        ta.ids_liquidacion AS ids_liquidacion_app,
		cp.ids_liquidacion AS ids_liquidacion_tdv,
        COALESCE(cp.tipo_transaccion,0) AS tipo_transaccion,
        ta.nombre_banco AS entidad,
        ta.fecha_servicio_transporte::DATE AS fecha_servicio_transporte,
        NULL AS identificacion_cliente,
        NULL AS razon_social,
        NULL AS codigo_punto_cargo,
        NULL AS nombre_punto_cargo,
        ta.ciudad_fondo,
        ta.tipo_pedido AS nombre_tipo_servicio,
        ta.tipo_pedido AS tipo_operacion,
        NULL AS moneda_divisa,
        1 AS aplicativo,
        0 AS tdv,
        CAST(COALESCE(ta.valor_procesado_billete, 0) AS NUMERIC(20, 2)) AS valor_procesado_billete,
        COALESCE(cp.valor_procesado_billete, 0) AS valor_procesado_billete_tdv,
        CAST(COALESCE(ta.valor_procesado_moneda, 0) AS NUMERIC(20, 2)) AS valor_procesado_moneda,
        COALESCE(cp.valor_procesado_moneda, 0) AS valor_procesado_moneda_tdv,
        CAST(COALESCE(ta.valor_total_procesado, 0) AS NUMERIC(20, 2)) AS valor_total_procesado,
        COALESCE(cp.valor_total_procesado, 0) AS valor_total_procesado_tdv,
        COALESCE(ta.subtotal, 0) AS subtotal,
        COALESCE(cp.costo_subtotal,0) AS subtotal_tdv,
        cp.iva,
        COALESCE(ta.valor_total_procesado, 0) AS valor_total,
        COALESCE(ta.clasificacion_fajado, 0) AS clasificacion_fajado,
	    COALESCE(cp.clasificacion_fajado,0) AS clasificacion_fajado_tdv,
	    COALESCE(ta.clasificacion_no_fajado, 0) AS clasificacion_no_fajado,
	    COALESCE(cp.clasificacion_no_fajado,0) AS clasificacion_no_fajado_tdv,
	    COALESCE(ta.costo_paqueteo, 0) AS costo_paqueteo,
	    COALESCE(cp.costo_paqueteo,0) AS costo_paqueteo_tdv,
	    COALESCE(ta.moneda_residuo, 0) AS moneda_residuo,
	    COALESCE(cp.moneda_residuo,0) AS moneda_residuo_tdv,
	    COALESCE(ta.billete_residuo, 0) AS billete_residuo,
	    COALESCE(cp.billete_residuo,0) AS billete_residuo_tdv,
		COALESCE(ta.valor_liquidado_billete,0) AS valor_almacenamiento_billete,
		COALESCE(cp.valor_liquidado_billete,0) AS valor_almacenamiento_billete_tdv,
		COALESCE(ta.valor_liquidado_moneda,0) AS valor_almacenamiento_moneda,
		COALESCE(cp.valor_liquidado_moneda,0) AS valor_almacenamiento_moneda_tdv,
		
	    'ELIMINADA' AS estado,
	    'LIQUIDADAS_NO_COBRADAS_ELIMINADAS' AS modulo,
		NULL::BIGINT AS id_llaves_maestro_tdv,
		ta.id_llaves_maestro_app
	  FROM tdl_aplicativo_rango_archivo_eliminadas ta
	  LEFT JOIN tbl_ctos_proc_consolidado_almacenamiento cp ON ta.id_llaves_maestro_app = cp.id_llaves_maestro_tdv
	  WHERE cp.entidad IS NULL
	) AS subquery
	CROSS JOIN max_cons_liq_no_cobradas mclc)

SELECT * FROM modulo_conciliadas
UNION
SELECT * FROM modulo_remitidas_no_identificadas
UNION
SELECT * FROM modulo_liquidadas_no_cobradas
UNION
SELECT * FROM modulo_liquidadas_no_cobradas_eliminadas;



---------------------------------------------------------------------------------------------------
------------------------------------------  transporte --------------------------------------------
---------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW controlefect.v_detalle_liquidacion_transporte AS
WITH tbl_parametros_eliminados AS (
  SELECT datos_parametros_liquidacion_costos::JSONB AS parametros_jsonb
  FROM controlefect.estado_conciliacion_parametros_liquidacion
  WHERE estado = 1
    AND datos_parametros_liquidacion_costos IS NOT NULL
    AND TRIM(datos_parametros_liquidacion_costos) <> ''
    AND LEFT(TRIM(datos_parametros_liquidacion_costos), 1) = '{'
),

tbl_valores_liquidados_eliminados AS 
  (SELECT datos_valores_liquidados::jsonb AS valores_jsonb
  FROM controlefect.estado_conciliacion_parametros_liquidacion
  WHERE estado = 1
    AND datos_valores_liquidados IS NOT NULL
    AND TRIM(datos_valores_liquidados) <> ''
    AND LEFT(TRIM(datos_valores_liquidados), 1) = '{'),
   
tbl_parametros_liquidacion_costo AS
  (SELECT (parametros_jsonb->>'idLiquidacionFlat')::integer AS id_liquidacion,
          parametros_jsonb->>'billetesFlat' AS billetes,
          (parametros_jsonb->>'codigoBancoFlat')::integer AS codigo_banco,
          parametros_jsonb->>'codigoTdvFlat' AS codigo_tdv,
          parametros_jsonb->>'escalaFlat' AS escala,
          parametros_jsonb->>'fajadoFlat' AS fajado,
          to_timestamp((parametros_jsonb->>'fechaEjecucionFlat')::bigint / 1000) AT TIME ZONE 'UTC' AS fecha_ejecucion,
          parametros_jsonb->>'monedasFlat' AS monedas,
          (parametros_jsonb->>'numeroBolsasFlat')::integer AS numero_bolsas,
          (parametros_jsonb->>'numeroFajosFlat')::integer AS numero_fajos,
          (parametros_jsonb->>'numeroParadasFlat')::integer AS numero_paradas,
          (parametros_jsonb->>'puntoDestinoFlat')::integer AS punto_destino,
          (parametros_jsonb->>'puntoOrigenFlat')::integer AS punto_origen,
          (parametros_jsonb->>'residuoBilletesFlat')::integer AS residuo_billetes,
          (parametros_jsonb->>'residuoMonedasFlat')::integer AS residuo_monedas,
          (parametros_jsonb->>'seqGrupoFlat')::integer AS seq_grupo,
          parametros_jsonb->>'tipoOperacionFlat' AS tipo_operacion,
          parametros_jsonb->>'tipoPuntoFlat' AS tipo_punto,
          parametros_jsonb->>'tipoServicioFlat' AS tipo_servicio,
          (parametros_jsonb->>'valorBilletesFlat')::double precision AS valor_billetes,
          (parametros_jsonb->>'valorMonedasFlat')::double precision AS valor_monedas,
          (parametros_jsonb->>'valorTotalFlat')::double precision AS valor_total,
          to_timestamp((parametros_jsonb ->> 'fechaConcilia')::bigint / 1000)::date AS fecha_concilia,
          parametros_jsonb->>'entradaSalidaFlat' AS entrada_salida,
          parametros_jsonb->>'codigoPropioTdv' AS codigo_propio_tdv,
          parametros_jsonb->>'nombreCliente' AS nombre_cliente,
		  (parametros_jsonb->>'totalFajosFlat')::double precision AS total_fajos,
		  (parametros_jsonb->>'totalBolsasFlat')::double precision AS total_bolsas
   FROM tbl_parametros_eliminados),
   
tbl_valores_liquidados AS
  (SELECT (valores_jsonb->>'idValoresLiqFlat')::bigint AS id_valores_liq,
          (valores_jsonb->>'clasificacionFajadoFlat')::double precision AS clasificacion_fajado,
          (valores_jsonb->>'clasificacionNoFajadoFlat')::double precision AS clasificacion_no_fajado,
          (valores_jsonb->>'costoCharterFlat')::double precision AS costo_charter,
          (valores_jsonb->>'costoEmisarioFlat')::double precision AS costo_emisario,
          (valores_jsonb->>'costoFijoParadaFlat')::double precision AS costo_fijo_parada,
          (valores_jsonb->>'costoMonedaFlat')::double precision AS costo_moneda,
          (valores_jsonb->>'costoPaqueteoFlat')::double precision AS costo_paqueteo,
          (valores_jsonb->>'idLiquidacionFlat')::bigint AS id_liquidacion,
          (valores_jsonb->>'milajePorRuteoFlat')::double precision AS milaje_por_ruteo,
          (valores_jsonb->>'milajeVerificacionFlat')::double precision AS milaje_verificacion,
          (valores_jsonb->>'modenaResiduoFlat')::double precision AS moneda_residuo,
          (valores_jsonb->>'tasaAeroportuariaFlat')::double precision AS tasa_aeroportuaria,
          (valores_jsonb->>'idSeqGrupoFlat')::integer AS id_seq_grupo,
          (valores_jsonb->>'billeteResiduoFlat')::double precision AS billete_residuo,
          (valores_jsonb->>'clasificacionMonedaFlat')::double precision AS clasificacion_moneda
   FROM tbl_valores_liquidados_eliminados),
   
tbl_ctos_transp_consolidado AS 
	(SELECT
	  array_to_string(array_agg(DISTINCT ct.consecutivo_registro), ',') AS consecutivo_registro,
	  ct.id_archivo_cargado,
	  array_to_string(array_agg(DISTINCT ct.id_registro), ',') AS id_registro,
	  ct.entidad,
	  ct.fecha_servicio_transporte,
	  STRING_AGG(DISTINCT ct.identificacion_cliente, ', ') AS identificacion_cliente,
	  STRING_AGG(DISTINCT ct.razon_social, ', ') AS razon_social,
	  STRING_AGG(DISTINCT ct.codigo_punto_cargo, ', ') AS codigo_punto_cargo,
	  STRING_AGG(DISTINCT ct.nombre_punto_cargo, ', ') AS nombre_punto_cargo,
	  ct.codigo_ciiu_fondo,
	  STRING_AGG(DISTINCT ct.ciudad_fondo, ', ') AS ciudad_fondo,
	  ct.nombre_tipo_servicio,
	  ct.tipo_pedido,
	  STRING_AGG(DISTINCT ct.escala, ', ') AS escala,
	  STRING_AGG(DISTINCT ct.moneda_divisa, ', ') AS moneda_divisa,
	  CAST(SUM(COALESCE(ct.valor_transportado_billete, 0)) AS NUMERIC(20, 2)) AS valor_transportado_billete,
	  CAST(SUM(COALESCE(ct.valor_transportado_moneda, 0)) AS NUMERIC(20, 2)) AS valor_transportado_moneda,
	  CAST(SUM(COALESCE(ct.valor_total_transportado, 0)) AS NUMERIC(20, 2)) AS valor_total_transportado,
	  CAST(SUM(COALESCE(ct.numero_fajos, 0)) AS NUMERIC(20, 2)) AS numero_fajos,
	  CAST(SUM(COALESCE(ct.numero_de_bolsas_moneda, 0)) AS NUMERIC(20, 2)) AS numero_de_bolsas_moneda,
	  CAST(SUM(COALESCE(ct.costo_fijo, 0)) AS INTEGER) AS costo_fijo,
	  CAST(SUM(COALESCE(ct.costo_por_milaje, 0)) AS NUMERIC(22, 4)) AS costo_por_milaje,
	  CAST(SUM(COALESCE(ct.costo_por_bolsa, 0)) AS NUMERIC(22, 4)) AS costo_por_bolsa,
	  CAST(SUM(COALESCE(ct.costo_fletes, 0)) AS INTEGER) AS costo_fletes,
	  CAST(SUM(COALESCE(ct.costo_emisarios, 0)) AS INTEGER) AS costo_emisarios,
	  CAST(SUM(COALESCE(ct.otros_1, 0)) AS INTEGER) AS otros_1,
	  CAST(SUM(COALESCE(ct.otros_2, 0)) AS INTEGER) AS otros_2,
	  CAST(SUM(COALESCE(ct.otros_3, 0)) AS INTEGER) AS otros_3,
	  CAST(SUM(COALESCE(ct.otros_4, 0)) AS INTEGER) AS otros_4,
	  CAST(SUM(COALESCE(ct.otros_5, 0)) AS INTEGER) AS otros_5,
	  CAST(SUM(COALESCE(ct.subtotal, 0)) AS NUMERIC(22, 4)) AS subtotal, 
	  array_to_string(array_agg(DISTINCT ct.iva), ',') AS iva,	  
	  SUM(COALESCE(ct.valor_total, 0)) AS valor_total,
	  STRING_AGG(DISTINCT ct.estado_conciliacion, ', ') AS estado_conciliacion,
	  STRING_AGG(DISTINCT ct.estado, ', ') AS estado,
	  STRING_AGG(DISTINCT ct.tipo_transaccion::text, '')::bigint AS tipo_transaccion,
	  ct.codigo_tdv,
	  ct.codigo_punto_interno,
	  ct.codigo_punto_fondo,
	  ct.entrada_salida,
	  array_to_string(array_agg(DISTINCT ct.id_liquidacion), ',') AS ids_liquidacion,
	  
	  (ABS(('x' || substring(MD5(CONCAT_WS('|', 
		ct.entidad,
		to_char(ct.fecha_servicio_transporte, 'YYYY-MM-DD'),
		ct.codigo_ciiu_fondo,
		ct.codigo_tdv,
		
		CASE 
		  WHEN ct.entrada_salida = 'SALIDA' THEN CONCAT(ct.codigo_punto_fondo, '|', ct.codigo_punto_interno)
		  ELSE CONCAT(ct.codigo_punto_interno, '|', ct.codigo_punto_fondo)
		END,
		
		ct.entrada_salida
	  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_tdv,
	  
	  (ABS((
	    'x' || substring(MD5(CONCAT_WS('|', 
	      ct.tipo_pedido,
	      TO_CHAR(SUM(COALESCE(ct.numero_fajos, 0)), 'FM99999999999990.00'),
	      TO_CHAR(SUM(COALESCE(ct.valor_total_transportado, 0)), 'FM99999999999990.00'),
	      TO_CHAR(SUM(COALESCE(ct.numero_de_bolsas_moneda, 0)), 'FM99999999999990.00'),
	      TO_CHAR(SUM(COALESCE(ct.costo_fijo, 0)), 'FM99999999999990'),
	      TO_CHAR(SUM(COALESCE(ct.costo_por_milaje, 0)), 'FM99999999999990.0000'),
	      TO_CHAR(SUM(COALESCE(ct.costo_por_bolsa, 0)), 'FM99999999999990.00'),
	      TO_CHAR(SUM(COALESCE(ct.costo_fletes, 0)), 'FM99999999999990'),
	      TO_CHAR(SUM(COALESCE(ct.costo_emisarios, 0)), 'FM99999999999990')
	    )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_comparador_tdv

	FROM controlefect.costos_transporte ct

	GROUP BY
	  ct.entidad,
	  ct.fecha_servicio_transporte,
	  ct.codigo_ciiu_fondo,
	  ct.nombre_tipo_servicio,
	  ct.codigo_tdv,
	  ct.entrada_salida,
	  ct.codigo_punto_fondo,
	  ct.codigo_punto_interno,  
	  ct.tipo_pedido,
	  ct.id_archivo_cargado),
	  
tbl_aplicativo AS 
	(SELECT
	  b.abreviatura AS entidad,
	  plc.fecha_ejecucion AS fecha_servicio_transporte,
	  plc.tipo_operacion AS tipo_pedido,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_servicio AS nombre_tipo_servicio, -- ESPECIAL/PROGRAMADA
	  p.codigo_ciudad AS codigo_ciudad_fondo,
	  c.nombre_ciudad AS ciudad_fondo,
	  CAST(SUM(COALESCE(plc.numero_fajos, 0)) AS NUMERIC(20, 2)) AS numero_fajos,
		STRING_AGG(DISTINCT plc.escala, ', ') AS escala,
		CAST(SUM(COALESCE(plc.valor_billetes, 0)) AS NUMERIC(20, 2)) AS valor_transportado_billetes,
		CAST(SUM(COALESCE(plc.valor_monedas, 0)) AS NUMERIC(20, 2)) AS valor_transportado_monedas,
		CAST(SUM(COALESCE(plc.valor_total, 0)) AS NUMERIC(20, 2)) AS valor_total_transportado,
		CAST(SUM(COALESCE(plc.numero_bolsas, 0)) AS NUMERIC(20, 2)) AS numero_de_bolsas_moneda,
		CAST(SUM(COALESCE(vl.costo_fijo_parada, 0)) AS INTEGER) AS costo_fijo,
		
		CAST(SUM(
		    COALESCE(vl.milaje_por_ruteo, 0) + 
		    COALESCE(vl.milaje_verificacion, 0)
		) AS NUMERIC(22, 4)) AS costo_milaje,
		
		CAST(SUM(COALESCE(vl.costo_moneda, 0)) AS NUMERIC(22, 4)) AS costo_bolsa,
		
		CAST(SUM(
		    COALESCE(vl.costo_charter, 0) + 
		    COALESCE(vl.tasa_aeroportuaria, 0)
		) AS INTEGER) AS costo_flete,
		
		CAST(SUM(COALESCE(vl.costo_emisario, 0)) AS INTEGER) AS costo_emisario,
		
		CAST(SUM(
		  COALESCE(vl.costo_fijo_parada, 0) +
		  COALESCE(vl.milaje_por_ruteo, 0) +
		  COALESCE(vl.milaje_verificacion, 0) +
		  COALESCE(vl.costo_moneda, 0) +
		  COALESCE(vl.costo_charter, 0) +
		  COALESCE(vl.tasa_aeroportuaria, 0) +
		  COALESCE(vl.costo_emisario, 0)
		) AS NUMERIC(22, 4)) AS subtotal,
		
		CAST(SUM(COALESCE(plc.total_fajos, 0)) AS NUMERIC(20, 2)) AS total_fajos,
		CAST(SUM(COALESCE(plc.total_bolsas, 0)) AS NUMERIC(20, 2)) AS total_bolsas,
		
		(ABS(('x' || substring(MD5(CONCAT_WS('|', 
		    b.abreviatura,
		    to_char(plc.fecha_ejecucion, 'YYYY-MM-DD'),
		    p.codigo_ciudad,
		    plc.codigo_tdv,
		    plc.punto_origen,
		    plc.punto_destino,
		    plc.entrada_salida
		)) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_app,
		
		(ABS((
		  'x' || substring(MD5(CONCAT_WS('|', 
		    plc.tipo_servicio,
		    TO_CHAR(SUM(COALESCE(plc.total_fajos, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(plc.valor_total, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(plc.total_bolsas, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(vl.costo_fijo_parada, 0)), 'FM99999999999990'),
		    TO_CHAR(SUM(COALESCE(vl.milaje_por_ruteo, 0) + COALESCE(vl.milaje_verificacion, 0)), 'FM99999999999990.0000'),
		    TO_CHAR(SUM(COALESCE(vl.costo_moneda, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(vl.costo_charter, 0) + COALESCE(vl.tasa_aeroportuaria, 0)), 'FM99999999999990'),
		    TO_CHAR(SUM(COALESCE(vl.costo_emisario, 0)), 'FM99999999999990')
		  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_comparador_app,
	  
	  array_to_string(array_agg(DISTINCT plc.id_liquidacion), ',') AS ids_liquidacion,
	  (CASE WHEN b.es_aval = true THEN CONCAT('BANCO ',b.nombre_banco) ELSE b.nombre_banco END) AS nombre_banco
	
	FROM controlefect.parametros_liquidacion_costo plc
	INNER JOIN controlefect.bancos b ON b.codigo_punto = plc.codigo_banco
	LEFT JOIN controlefect.valores_liquidados vl ON vl.id_liquidacion = plc.id_liquidacion

	-- Nuevos joins para obtener la ciudad fondo
	INNER JOIN controlefect.fondos f ON f.tdv = plc.codigo_tdv
	INNER JOIN controlefect.puntos p ON f.codigo_punto = p.codigo_punto AND p.tipo_punto = 'FONDO'
	INNER JOIN controlefect.ciudades c ON c.codigo_dane = p.codigo_ciudad
	INNER JOIN controlefect.bancos b2 ON b2.codigo_punto = f.banco_aval AND b2.abreviatura = b.abreviatura
	AND p.codigo_ciudad = (
		SELECT codigo_ciudad
		FROM controlefect.puntos
		WHERE codigo_punto = 
			CASE 
				WHEN plc.entrada_salida = 'SALIDA' THEN plc.punto_origen
				WHEN plc.entrada_salida = 'ENTRADA' THEN plc.punto_destino
			END
	)

	GROUP BY
	  b.abreviatura,
	  plc.fecha_ejecucion,
	  plc.tipo_servicio,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_operacion,
	  b.es_aval,
	  b.nombre_banco,
	  p.codigo_ciudad,
	  c.nombre_ciudad
	HAVING 
	  SUM(COALESCE(vl.costo_fijo_parada, 0) +
		  COALESCE(vl.milaje_por_ruteo, 0) +
		  COALESCE(vl.costo_moneda, 0) +
		  COALESCE(vl.costo_charter, 0) +
		  COALESCE(vl.costo_emisario, 0)) > 0),		  

tbl_aplicativo_eliminadas AS 
	(SELECT
	  b.abreviatura AS entidad,
	  plc.fecha_ejecucion AS fecha_servicio_transporte,
	  plc.tipo_operacion AS tipo_pedido,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,	  
	  plc.tipo_servicio AS nombre_tipo_servicio, -- ESPECIAL/PROGRAMADA
	  p.codigo_ciudad AS codigo_ciudad_fondo,
	  c.nombre_ciudad AS ciudad_fondo,
	  CAST(SUM(COALESCE(plc.numero_fajos, 0)) AS NUMERIC(20, 2)) AS numero_fajos,
		STRING_AGG(DISTINCT plc.escala, ', ') AS escala,
		CAST(SUM(COALESCE(plc.valor_billetes, 0)) AS NUMERIC(20, 2)) AS valor_transportado_billetes,
		CAST(SUM(COALESCE(plc.valor_monedas, 0)) AS NUMERIC(20, 2)) AS valor_transportado_monedas,
		CAST(SUM(COALESCE(plc.valor_total, 0)) AS NUMERIC(20, 2)) AS valor_total_transportado,
		CAST(SUM(COALESCE(plc.numero_bolsas, 0)) AS NUMERIC(20, 2)) AS numero_de_bolsas_moneda,
		CAST(SUM(COALESCE(vl.costo_fijo_parada, 0)) AS INTEGER) AS costo_fijo,
		
		CAST(SUM(
		    COALESCE(vl.milaje_por_ruteo, 0) + 
		    COALESCE(vl.milaje_verificacion, 0)
		) AS NUMERIC(22, 4)) AS costo_milaje,
		
		CAST(SUM(COALESCE(vl.costo_moneda, 0)) AS NUMERIC(22, 4)) AS costo_bolsa,
		
		CAST(SUM(
		    COALESCE(vl.costo_charter, 0) + 
		    COALESCE(vl.tasa_aeroportuaria, 0)
		) AS INTEGER) AS costo_flete,
		
		CAST(SUM(COALESCE(vl.costo_emisario, 0)) AS INTEGER) AS costo_emisario,
		
		CAST(SUM(
		  COALESCE(vl.costo_fijo_parada, 0) +
		  COALESCE(vl.milaje_por_ruteo, 0) +
		  COALESCE(vl.milaje_verificacion, 0) +
		  COALESCE(vl.costo_moneda, 0) +
		  COALESCE(vl.costo_charter, 0) +
		  COALESCE(vl.tasa_aeroportuaria, 0) +
		  COALESCE(vl.costo_emisario, 0)
		) AS NUMERIC(22, 4)) AS subtotal,
		
		CAST(SUM(COALESCE(plc.total_fajos, 0)) AS NUMERIC(20, 2)) AS total_fajos,
		CAST(SUM(COALESCE(plc.total_bolsas, 0)) AS NUMERIC(20, 2)) AS total_bolsas,
		
		(ABS(('x' || substring(MD5(CONCAT_WS('|', 
		    b.abreviatura,
		    to_char(plc.fecha_ejecucion, 'YYYY-MM-DD'),
		    p.codigo_ciudad,
		    plc.codigo_tdv,
		    plc.punto_origen,
		    plc.punto_destino,
		    plc.entrada_salida
		)) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_maestro_app,
		
		(ABS((
		  'x' || substring(MD5(CONCAT_WS('|', 
		    plc.tipo_servicio,
		    TO_CHAR(SUM(COALESCE(plc.total_fajos, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(plc.valor_total, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(plc.total_bolsas, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(vl.costo_fijo_parada, 0)), 'FM99999999999990'),
		    TO_CHAR(SUM(COALESCE(vl.milaje_por_ruteo, 0) + COALESCE(vl.milaje_verificacion, 0)), 'FM99999999999990.0000'),
		    TO_CHAR(SUM(COALESCE(vl.costo_moneda, 0)), 'FM99999999999990.00'),
		    TO_CHAR(SUM(COALESCE(vl.costo_charter, 0) + COALESCE(vl.tasa_aeroportuaria, 0)), 'FM99999999999990'),
		    TO_CHAR(SUM(COALESCE(vl.costo_emisario, 0)), 'FM99999999999990')
		  )) FROM 1 FOR 15))::bit(60)::bigint)) AS id_llaves_comparador_app,
	    
	  array_to_string(array_agg(DISTINCT plc.id_liquidacion), ',') AS ids_liquidacion,
	  (CASE WHEN b.es_aval = true THEN CONCAT('BANCO ',b.nombre_banco) ELSE b.nombre_banco END) AS nombre_banco
	FROM tbl_parametros_liquidacion_costo plc
	INNER JOIN controlefect.bancos b ON b.codigo_punto = plc.codigo_banco
	LEFT JOIN tbl_valores_liquidados vl ON vl.id_liquidacion = plc.id_liquidacion

	-- Nuevos joins para obtener la ciudad fondo
	INNER JOIN controlefect.fondos f ON f.tdv = plc.codigo_tdv
	INNER JOIN controlefect.puntos p ON f.codigo_punto = p.codigo_punto AND p.tipo_punto = 'FONDO'
	INNER JOIN controlefect.ciudades c ON c.codigo_dane = p.codigo_ciudad
	INNER JOIN controlefect.bancos b2 ON b2.codigo_punto = f.banco_aval AND b2.abreviatura = b.abreviatura
	AND p.codigo_ciudad = (
		SELECT codigo_ciudad
		FROM controlefect.puntos
		WHERE codigo_punto = 
			CASE 
				WHEN plc.entrada_salida = 'SALIDA' THEN plc.punto_origen
				WHEN plc.entrada_salida = 'ENTRADA' THEN plc.punto_destino
			END
	)

	GROUP BY
	  b.abreviatura,
	  plc.fecha_ejecucion,
	  plc.tipo_servicio,
	  plc.codigo_tdv,
	  plc.punto_origen,
	  plc.punto_destino,
	  plc.entrada_salida,
	  plc.tipo_operacion,
	  b.es_aval,
	  b.nombre_banco,
	  p.codigo_ciudad,
	  c.nombre_ciudad
	HAVING 
	  SUM(COALESCE(vl.costo_fijo_parada, 0) +
		  COALESCE(vl.milaje_por_ruteo, 0) +
		  COALESCE(vl.costo_moneda, 0) +
		  COALESCE(vl.costo_charter, 0) +
		  COALESCE(vl.costo_emisario, 0)) > 0),
  
tbl_prestacion_servicio_transporte AS
	  (SELECT id_archivo_cargado,
			  MIN(fecha_servicio_transporte) AS fecha_minima,
			  MAX(fecha_servicio_transporte) AS fecha_maxima
	   FROM tbl_ctos_transp_consolidado
	   GROUP BY id_archivo_cargado),
   
tbl_costos_transporte AS
	  (SELECT ct. *,
			  pst.fecha_minima,
			  pst.fecha_maxima
	   FROM tbl_prestacion_servicio_transporte pst
	   INNER JOIN tbl_ctos_transp_consolidado ct ON ct.id_archivo_cargado = pst.id_archivo_cargado),
   
tdl_aplicativo_rango_archivo AS
	  (SELECT ta.*
	   FROM tbl_aplicativo ta
	   WHERE EXISTS
		   (SELECT 1
			FROM tbl_costos_transporte ct
			WHERE date_trunc('day', ta.fecha_servicio_transporte) 
			BETWEEN date_trunc('day', ct.fecha_minima) 
			AND date_trunc('day', ct.fecha_maxima))),

tdl_aplicativo_rango_archivo_eliminadas AS
	  (SELECT ta.*
	   FROM tbl_aplicativo_eliminadas ta
	   WHERE EXISTS
		   (SELECT 1
			FROM tbl_costos_transporte ct
			WHERE date_trunc('day', ta.fecha_servicio_transporte) 
			BETWEEN date_trunc('day', ct.fecha_minima) 
			AND date_trunc('day', ct.fecha_maxima))),
			
max_consecutivo AS (
    SELECT MAX(ct.consecutivo_registro) AS max_consecutivo_registro
    FROM controlefect.costos_transporte ct
),

modulo_conciliadas AS
  (SELECT DISTINCT ct.consecutivo_registro,
                   ct.id_archivo_cargado,
                   ct.id_registro,
                   ta.ids_liquidacion AS ids_liquidacion_app,
                   ct.ids_liquidacion AS ids_liquidacion_tdv,
                   COALESCE(ct.tipo_transaccion, 0) AS tipo_transaccion,
                   COALESCE(ta.nombre_banco, CONCAT('BANCO ', ba.nombre_banco)) AS entidad,
                   ct.fecha_servicio_transporte,
                   ct.identificacion_cliente,
                   ct.razon_social,
                   ct.codigo_punto_cargo,
                   ct.nombre_punto_cargo,
                   ct.ciudad_fondo,
                   ct.tipo_pedido AS nombre_tipo_servicio,
                   ct.moneda_divisa,
                   (CASE
                        WHEN ta.id_llaves_comparador_app = ct.id_llaves_comparador_tdv THEN 1
                        ELSE 0
                    END) AS aplicativo,
                   (CASE
                        WHEN ta.id_llaves_comparador_app = ct.id_llaves_comparador_tdv THEN 1
                        ELSE 0
                    END) AS tdv,
                   ta.tipo_pedido,
                   ct.nombre_tipo_servicio AS tipo_pedido_tdv,
                   ta.escala,
                   ct.escala AS escala_tdv,
                   ta.valor_transportado_billetes,
                   ct.valor_transportado_billete AS valor_transportado_billetes_tdv,
                   ta.valor_transportado_monedas,
                   ct.valor_transportado_moneda AS valor_transportado_monedas_tdv,
                   ta.valor_total_transportado,
                   ct.valor_total_transportado AS valor_total_transportado_tdv,
                   ta.total_fajos AS numero_fajos,
                   ct.numero_fajos AS numero_fajos_tdv,
                   ta.total_bolsas AS numero_bolsas,
                   ct.numero_de_bolsas_moneda AS numero_bolsas_tdv,
                   ta.costo_fijo,
                   ct.costo_fijo AS costo_fijo_tdv,
                   ta.costo_milaje,
                   ct.costo_por_milaje AS costo_milaje_tdv,
                   ta.costo_bolsa,
                   ct.costo_por_bolsa AS costo_bolsa_tdv,
                   ta.costo_flete,
                   ct.costo_fletes AS costo_flete_tdv,
                   ta.costo_emisario,
                   ct.costo_emisarios AS costo_emisario_tdv,
                   ct.otros_1 AS otros1,
                   ct.otros_2 AS otros2,
                   ct.otros_3 AS otros3,
                   ct.otros_4 AS otros4,
                   ct.otros_5 AS otros5,
                   ta.subtotal,
                   ct.subtotal AS subtotal_tdv,
                   ct.iva,
                   ct.valor_total,
                   CASE
                       WHEN ct.estado_conciliacion IS NULL OR ct.estado_conciliacion = 'PENDIENTE' THEN
                       		CASE 
                       			WHEN ta.id_llaves_comparador_app = ct.id_llaves_comparador_tdv 
	                       		THEN 'AUTOMATICO'
	                            ELSE 'EN_CONCILIACION'
	                        END
                       ELSE ct.estado_conciliacion
                   END AS estado,
                   CASE
                       WHEN ta.id_llaves_comparador_app = ct.id_llaves_comparador_tdv THEN'CONCILIADAS'
                       ELSE'IDENTIFICADAS_CON_DIFERENCIAS'
                   END AS modulo,
                   ct.id_llaves_maestro_tdv,
                   ta.id_llaves_maestro_app
   FROM tbl_aplicativo ta
   INNER JOIN tbl_ctos_transp_consolidado ct ON ta.id_llaves_maestro_app = ct.id_llaves_maestro_tdv
   INNER JOIN controlefect.bancos ba ON ba.abreviatura = ct.entidad
   AND ba.es_aval = TRUE),

modulo_remitidas_no_identificadas AS
  (SELECT DISTINCT ct.consecutivo_registro,
                   ct.id_archivo_cargado,
                   ct.id_registro,
                   ta.ids_liquidacion AS ids_liquidacion_app,
				   ct.ids_liquidacion AS ids_liquidacion_tdv,
                   COALESCE(ct.tipo_transaccion,0) AS tipo_transaccion,
                   COALESCE(ta.nombre_banco, CONCAT('BANCO ', ba.nombre_banco)) AS entidad,
                   ct.fecha_servicio_transporte AS fecha_servicio_transporte,
                   ct.identificacion_cliente,
                   ct.razon_social,
                   ct.codigo_punto_cargo,
                   ct.nombre_punto_cargo,
                   ct.ciudad_fondo,
                   ct.tipo_pedido AS nombre_tipo_servicio,
                   ct.moneda_divisa,
                   0 AS aplicativo,
                   1 AS tdv,
                   ta.tipo_pedido,
                   ct.nombre_tipo_servicio AS tipo_pedido_tdv,
                   ta.escala,
                   ct.escala AS escala_tdv,
                   COALESCE(ta.valor_transportado_billetes,0) AS valor_transportado_billetes,
                   ct.valor_transportado_billete AS valor_transportado_billetes_tdv,
                   COALESCE(ta.valor_transportado_monedas,0) AS valor_transportado_monedas,
                   ct.valor_transportado_moneda AS valor_transportado_monedas_tdv,
                   COALESCE(ta.valor_total_transportado,0) AS valor_total_transportado,
                   ct.valor_total_transportado AS valor_total_transportado_tdv,
                   COALESCE(ta.total_fajos,0) AS numero_fajos,
                   ct.numero_fajos AS numero_fajos_tdv,
                   COALESCE(ta.total_bolsas,0) AS numero_bolsas,
                   ct.numero_de_bolsas_moneda AS numero_bolsas_tdv,
                   COALESCE(ta.costo_fijo,0) AS costo_fijo,
                   ct.costo_fijo AS costo_fijo_tdv,
                   COALESCE(ta.costo_milaje,0) AS costo_milaje,
                   ct.costo_por_milaje AS costo_milaje_tdv,
                   COALESCE(ta.costo_bolsa,0) AS costo_bolsa,
                   ct.costo_por_bolsa AS costo_bolsa_tdv,
                   COALESCE(ta.costo_flete,0) AS costo_flete,
                   ct.costo_fletes AS costo_flete_tdv,
                   COALESCE(ta.costo_emisario,0) AS costo_emisario,
                   ct.costo_emisarios AS costo_emisario_tdv,
                   ct.otros_1 AS otros1,
                   ct.otros_2 AS otros2,
                   ct.otros_3 AS otros3,
                   ct.otros_4 AS otros4,
                   ct.otros_5 AS otros5,
                   COALESCE(ta.subtotal,0) AS subtotal,
                   ct.subtotal AS subtotal_tdv,
                   ct.iva,
                   ct.valor_total,
                   CASE
                       WHEN ct.estado_conciliacion = 'PENDIENTE'
                            OR ct.estado_conciliacion IS NULL THEN 'EN_CONCILIACION'
                       ELSE ct.estado_conciliacion
                   END AS estado,
                   'REMITIDAS_NO_IDENTIFICADAS' AS modulo,
				   ct.id_llaves_maestro_tdv,
				   NULL::BIGINT AS id_llaves_maestro_app
   FROM tbl_aplicativo ta
   RIGHT JOIN tbl_ctos_transp_consolidado ct ON ta.id_llaves_maestro_app = ct.id_llaves_maestro_tdv
   INNER JOIN controlefect.bancos ba ON ba.abreviatura = ct.entidad
   AND ba.es_aval = TRUE

   WHERE ta.entidad IS NULL
     AND (ct.tipo_transaccion IS NULL
          OR ct.tipo_transaccion != 1)),
          
modulo_liquidadas_no_cobradas AS (
SELECT DISTINCT 
		   CAST((ROW_NUMBER() OVER () + mc.max_consecutivo_registro)::TEXT AS VARCHAR) AS consecutivo_registro,
		   COALESCE(ct.id_archivo_cargado,0) AS id_archivo_cargado,
           ct.id_registro,            
		   ta.ids_liquidacion AS ids_liquidacion_app,
		   ct.ids_liquidacion AS ids_liquidacion_tdv,
           COALESCE(ct.tipo_transaccion,0) AS tipo_transaccion,
		   ta.nombre_banco AS entidad,
           ta.fecha_servicio_transporte AS fecha_servicio_transporte,
           NULL AS identificacion_cliente,
           NULL AS razon_social,
           NULL AS codigo_punto_cargo,
           NULL AS nombre_punto_cargo,
           ta.ciudad_fondo,
           ta.nombre_tipo_servicio AS nombre_tipo_servicio,
           NULL AS moneda_divisa,
           1 AS aplicativo,
           0 AS tdv,
           ta.tipo_pedido,
           NULL AS tipo_pedido_tdv,
           ta.escala,
           NULL AS escala_tdv,
           COALESCE(ta.valor_transportado_billetes, 0) AS valor_transportado_billetes,
			COALESCE(ct.valor_transportado_billete, 0) AS valor_transportado_billetes_tdv,
			COALESCE(ta.valor_transportado_monedas, 0) AS valor_transportado_monedas,
			COALESCE(ct.valor_transportado_moneda, 0) AS valor_transportado_monedas_tdv,
			COALESCE(ta.valor_total_transportado, 0) AS valor_total_transportado,
			COALESCE(ct.valor_total_transportado, 0) AS valor_total_transportado_tdv,
			COALESCE(ta.total_fajos, 0) AS numero_fajos,
			COALESCE(ct.numero_fajos, 0) AS numero_fajos_tdv,
			COALESCE(ta.total_bolsas, 0) AS numero_bolsas,
			COALESCE(ct.numero_de_bolsas_moneda, 0) AS numero_bolsas_tdv,
			COALESCE(ta.costo_fijo, 0) AS costo_fijo,
			COALESCE(ct.costo_fijo, 0) AS costo_fijo_tdv,
			COALESCE(ta.costo_milaje, 0) AS costo_milaje,
			COALESCE(ct.costo_por_milaje, 0) AS costo_milaje_tdv,
			COALESCE(ta.costo_bolsa, 0) AS costo_bolsa,
			COALESCE(ct.costo_por_bolsa, 0) AS costo_bolsa_tdv,
			COALESCE(ta.costo_flete, 0) AS costo_flete,
			COALESCE(ct.costo_fletes, 0) AS costo_flete_tdv,
			COALESCE(ta.costo_emisario, 0) AS costo_emisario,
			COALESCE(ct.costo_emisarios, 0) AS costo_emisario_tdv,
           0 AS otros1,
           0 AS otros2,
           0 AS otros3,
           0 AS otros4,
           0 AS otros5,
		   COALESCE(ta.subtotal, 0) AS subtotal,
           COALESCE(ct.subtotal,0) AS subtotal_tdv,
           ct.iva,
           COALESCE(ta.valor_total_transportado,0) AS valor_total,
           'EN_CONCILIACION' AS estado,
           'LIQUIDADAS_NO_COBRADAS' AS modulo,
		   NULL::BIGINT AS id_llaves_maestro_tdv,
		   ta.id_llaves_maestro_app
   FROM tdl_aplicativo_rango_archivo ta
   LEFT JOIN tbl_ctos_transp_consolidado ct ON ta.id_llaves_maestro_app = ct.id_llaves_maestro_tdv
   CROSS JOIN max_consecutivo mc
   WHERE ct.entidad IS NULL),

max_cons_liq_no_cobradas AS (   
SELECT MAX(CAST(consecutivo_registro AS INTEGER)) AS max_consecutivo_registro
FROM (
    SELECT
        CAST((ROW_NUMBER() OVER () + mc.max_consecutivo_registro)::TEXT AS VARCHAR) AS consecutivo_registro
    FROM
        modulo_liquidadas_no_cobradas
        CROSS JOIN max_consecutivo mc
) AS subquery),

modulo_liquidadas_no_cobradas_eliminadas AS
	(SELECT
	  CAST((ROW_NUMBER() OVER () + mclc.max_consecutivo_registro)::TEXT AS VARCHAR) AS consecutivo_registro,
	  subquery.*
	FROM (
	  SELECT DISTINCT 
	       ct.id_archivo_cargado,
           ct.id_registro,            
		   ta.ids_liquidacion AS ids_liquidacion_app,
		   ct.ids_liquidacion AS ids_liquidacion_tdv,
           COALESCE(ct.tipo_transaccion,0) AS tipo_transaccion,
		   ta.nombre_banco AS entidad,
           ta.fecha_servicio_transporte AS fecha_servicio_transporte,
           NULL AS identificacion_cliente,
           NULL AS razon_social,
           NULL AS codigo_punto_cargo,
           NULL AS nombre_punto_cargo,
           ta.ciudad_fondo,
           ta.nombre_tipo_servicio AS nombre_tipo_servicio,
           NULL AS moneda_divisa,
           1 AS aplicativo,
           0 AS tdv,
           ta.tipo_pedido,
           NULL AS tipo_pedido_tdv,
           ta.escala,
           NULL AS escala_tdv,
           COALESCE(ta.valor_transportado_billetes, 0) AS valor_transportado_billetes,
			COALESCE(ta.valor_transportado_billetes, 0) AS valor_transportado_billetes_tdv,
			COALESCE(ta.valor_transportado_monedas, 0) AS valor_transportado_monedas,
			COALESCE(ta.valor_transportado_monedas, 0) AS valor_transportado_monedas_tdv,
			COALESCE(ta.valor_total_transportado, 0) AS valor_total_transportado,
			COALESCE(ta.valor_total_transportado, 0) AS valor_total_transportado_tdv,
			COALESCE(ta.total_fajos, 0) AS numero_fajos,
			COALESCE(ta.numero_fajos, 0) AS numero_fajos_tdv,
			COALESCE(ta.total_bolsas, 0) AS numero_bolsas,
			COALESCE(ta.numero_de_bolsas_moneda, 0) AS numero_bolsas_tdv,
			COALESCE(ta.costo_fijo, 0) AS costo_fijo,
			COALESCE(ta.costo_fijo, 0) AS costo_fijo_tdv,
			COALESCE(ta.costo_milaje, 0) AS costo_milaje,
			COALESCE(ta.costo_milaje, 0) AS costo_milaje_tdv,
			COALESCE(ta.costo_bolsa, 0) AS costo_bolsa,
			COALESCE(ta.costo_bolsa, 0) AS costo_bolsa_tdv,
			COALESCE(ta.costo_flete, 0) AS costo_flete,
			COALESCE(ta.costo_flete, 0) AS costo_flete_tdv,
			COALESCE(ta.costo_emisario, 0) AS costo_emisario,
			COALESCE(ta.costo_emisario, 0) AS costo_emisario_tdv,
           0 AS otros1,
           0 AS otros2,
           0 AS otros3,
           0 AS otros4,
           0 AS otros5,
		   COALESCE(ta.subtotal, 0) AS subtotal,
           COALESCE(ta.subtotal,0) AS subtotal_tdv,
           ct.iva,
           COALESCE(ta.valor_total_transportado,0) AS valor_total,
	    'ELIMINADA' AS estado,
	    'LIQUIDADAS_NO_COBRADAS_ELIMINADAS' AS modulo,
		NULL::BIGINT AS id_llaves_maestro_tdv,
		ta.id_llaves_maestro_app
	  FROM tdl_aplicativo_rango_archivo_eliminadas ta
	  LEFT JOIN tbl_ctos_transp_consolidado ct ON ta.id_llaves_maestro_app = ct.id_llaves_maestro_tdv
	  WHERE ct.entidad IS NULL
	) AS subquery
	CROSS JOIN max_cons_liq_no_cobradas mclc)

SELECT * FROM modulo_conciliadas
UNION
SELECT * FROM modulo_remitidas_no_identificadas
UNION
SELECT * FROM modulo_liquidadas_no_cobradas
UNION
SELECT * FROM modulo_liquidadas_no_cobradas_eliminadas;



