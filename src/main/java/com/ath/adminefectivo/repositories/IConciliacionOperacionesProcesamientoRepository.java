package com.ath.adminefectivo.repositories;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;
import com.ath.adminefectivo.service.IDetalleLiquidacionProcesamiento;

public interface IConciliacionOperacionesProcesamientoRepository extends CrudRepository<OperacionesLiquidacionProcesamientoEntity, Integer> {

	@Transactional(readOnly = true)
	@Query(
	    value = "SELECT "
	            + " mc.id_llave AS consecutivo_registro, "
	            + " v.id_archivo_cargado, "
	            + " mc.id_llave AS id_registro, "
	            + " mc.id_llave AS id_liquidacion, "
	            + " v.tipo_transaccion, "
	            + " v.entidad, "
	            + " v.fecha_servicio_transporte, "
	            + " v.identificacion_cliente, "
	            + " v.razon_social, "
	            + " v.codigo_punto_cargo, "
	            + " v.nombre_punto_cargo, "
	            + " v.ciudad_fondo, "
	            + " v.nombre_tipo_servicio, "
	            + " v.moneda_divisa, "
	            + " v.aplicativo, "
	            + " v.tdv, "
	            + " v.valor_procesado_billete, "
	            + " v.valor_procesado_billete_tdv, "
	            + " v.valor_procesado_moneda, "
	            + " v.valor_procesado_moneda_tdv, "
	            + " v.valor_total_procesado, "
	            + " v.valor_total_procesado_tdv, "
	            + " v.subtotal, "
	            + " v.subtotal_tdv, "
	            + " v.iva, "
	            + " v.valor_total, "
	            + " v.clasificacion_fajado, "
	            + " v.clasificacion_fajado_tdv, "
	            + " v.clasificacion_no_fajado, "
	            + " v.clasificacion_no_fajado_tdv, "
	            + " v.costo_paqueteo, "
	            + " v.costo_paqueteo_tdv, "
	            + " v.moneda_residuo, "
	            + " v.moneda_residuo_tdv, "
	            + " v.billete_residuo, "
	            + " v.billete_residuo_tdv, "
	            + " v.valor_almacenamiento_billete, "
	            + " v.valor_almacenamiento_billete_tdv, "
	            + " v.valor_almacenamiento_moneda, "
	            + " v.valor_almacenamiento_moneda_tdv, "
	            + " v.estado, "
	            + " v.modulo "
	            + " FROM v_detalle_liquidacion_procesamiento v "
	            + " LEFT JOIN controlefect.maestro_llaves_costos mc "
	            + " ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) "
	            + " AND mc.estado IN('PENDIENTE','RECHAZADA') "
	            + " WHERE (:entity is null or v.entidad = cast(:entity AS text)) "
	            + " AND  (:clientIdentification is null or v.identificacion_cliente = cast(:clientIdentification AS text))"
	            + " AND  (:businessName is null or v.razon_social = cast(:businessName AS text))  "
	            + " AND  (:cargoPointCode is null or v.codigo_punto_cargo = cast(:cargoPointCode AS text)) "
	            + " AND  (:cargoPointName is null or v.nombre_punto_cargo = cast(:cargoPointName AS text))  "
	            + " AND  (:fundCity is null or v.ciudad_fondo = cast(:fundCity AS text))  "
	            + " AND  (:serviceTypeName is null or v.nombre_tipo_servicio = cast(:serviceTypeName AS text))  "
	            + " AND  (:currencyExchange is null or v.moneda_divisa = cast(:currencyExchange AS text))  "
	            + " AND  (:state is null or v.estado = cast(:state AS text)) "
	            + " AND  (v.modulo = :module)  "
	            + " AND  (v.fecha_servicio_transporte BETWEEN :fechaServicioTransporte AND :fechaServicioTransporteFinal)",
	    countQuery = "SELECT COUNT(*) "
	            + " FROM v_detalle_liquidacion_procesamiento v "
	            + " LEFT JOIN controlefect.maestro_llaves_costos mc "
	            + " ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) "
	            + " AND mc.estado IN('PENDIENTE','RECHAZADA') "
	            + " WHERE (:entity is null or v.entidad = cast(:entity AS text)) "
	            + " AND  (:clientIdentification is null or v.identificacion_cliente = cast(:clientIdentification AS text))"
	            + " AND  (:businessName is null or v.razon_social = cast(:businessName AS text))  "
	            + " AND  (:cargoPointCode is null or v.codigo_punto_cargo = cast(:cargoPointCode AS text)) "
	            + " AND  (:cargoPointName is null or v.nombre_punto_cargo = cast(:cargoPointName AS text))  "
	            + " AND  (:fundCity is null or v.ciudad_fondo = cast(:fundCity AS text))  "
	            + " AND  (:serviceTypeName is null or v.nombre_tipo_servicio = cast(:serviceTypeName AS text))  "
	            + " AND  (:currencyExchange is null or v.moneda_divisa = cast(:currencyExchange AS text))  "
	            + " AND  (:state is null or v.estado = cast(:state AS text)) "
	            + " AND  (v.modulo = :module)  "
	            + " AND  (v.fecha_servicio_transporte BETWEEN :fechaServicioTransporte AND :fechaServicioTransporteFinal)",
	    nativeQuery = true
	)
	Page<OperacionesLiquidacionProcesamientoEntity> conciliadasLiquidadasProcesamiento(
	        @Param("entity") String entidad,
	        @Param("fechaServicioTransporte") LocalDateTime fechaServicioTransporte,
	        @Param("fechaServicioTransporteFinal") LocalDateTime fechaServicioTransporteFinal,
	        @Param("clientIdentification") String identificacionCliente,
	        @Param("businessName") String razonSocial,
	        @Param("cargoPointCode") String codigoPuntoCargo,
	        @Param("cargoPointName") String nombrePuntoCargo,
	        @Param("fundCity") String ciudadFondo,
	        @Param("serviceTypeName") String nombreTipoServicio,
	        @Param("currencyExchange") String monedaDivisa,
	        @Param("state") String estado,
	        @Param("module") String modulo,
	        Pageable pageable
	);
	
	Optional<OperacionesLiquidacionProcesamientoEntity> findByRecordConsecutive(Integer recordConsecutive);
	
	@Query(value = """
			    SELECT
			        mc.id_llave AS idLlave,
			        v.consecutivo_registro AS consecutivoRegistro,
			        v.id_archivo_cargado AS idArchivoCargado,
			        v.id_registro AS idRegistro,
			        v.ids_liquidacion_app AS idsLiquidacionApp,
			        v.ids_liquidacion_tdv AS idsLiquidacionTdv,
			        v.tipo_transaccion AS tipoTransaccion,
			        v.entidad AS entidad,
			        v.fecha_servicio_transporte AS fechaServicioTransporte,
			        v.identificacion_cliente AS identificacionCliente,
			        v.razon_social AS razonSocial,
			        v.codigo_punto_cargo AS codigoPuntoCargo,
			        v.nombre_punto_cargo AS nombrePuntoCargo,
			        v.ciudad_fondo AS ciudadFondo,
			        v.nombre_tipo_servicio AS nombreTipoServicio,
			        v.tipo_operacion AS tipoOperacion,
			        v.moneda_divisa AS monedaDivisa,
			        v.aplicativo AS aplicativo,
			        v.tdv AS tdv,
			        v.valor_procesado_billete AS valorProcesadoBillete,
			        v.valor_procesado_billete_tdv AS valorProcesadoBilleteTdv,
			        v.valor_procesado_moneda AS valorProcesadoMoneda,
			        v.valor_procesado_moneda_tdv AS valorProcesadoMonedaTdv,
			        v.valor_total_procesado AS valorTotalProcesado,
			        v.valor_total_procesado_tdv AS valorTotalProcesadoTdv,
			        v.subtotal AS subtotal,
			        v.subtotal_tdv AS subtotalTdv,
			        v.iva AS iva,
			        v.valor_total AS valorTotal,
			        v.clasificacion_fajado AS clasificacionFajado,
			        v.clasificacion_fajado_tdv AS clasificacionFajadoTdv,
			        v.clasificacion_no_fajado AS clasificacionNoFajado,
			        v.clasificacion_no_fajado_tdv AS clasificacionNoFajadoTdv,
			        v.costo_paqueteo AS costoPaqueteo,
			        v.costo_paqueteo_tdv AS costoPaqueteoTdv,
			        v.moneda_residuo AS monedaResiduo,
			        v.moneda_residuo_tdv AS monedaResiduoTdv,
			        v.billete_residuo AS billeteResiduo,
			        v.billete_residuo_tdv AS billeteResiduoTdv,
			        v.valor_almacenamiento_billete AS valorAlmacenamientoBillete,
			        v.valor_almacenamiento_billete_tdv AS valorAlmacenamientoBilleteTdv,
			        v.valor_almacenamiento_moneda AS valorAlmacenamientoMoneda,
			        v.valor_almacenamiento_moneda_tdv AS valorAlmacenamientoMonedaTdv,
			        v.estado AS estado,
			        v.modulo AS modulo,
			        v.id_llaves_maestro_tdv AS idLlavesMaestroTdv,
			        v.id_llaves_maestro_app AS idLlavesMaestroApp
			    FROM controlefect.v_detalle_liquidacion_procesamiento v
			    LEFT JOIN controlefect.maestro_llaves_costos mc
			        ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			    WHERE v.id_llaves_maestro_tdv = :idLlaveMaestro
			""", nativeQuery = true)
	List<IDetalleLiquidacionProcesamiento> countConciliadasProcesamientoByLlave(
			@Param("idLlaveMaestro") BigInteger idLlaveMaestro);
	
	@Query(value = """
			    SELECT
			        mc.id_llave AS idLlave,
			        v.consecutivo_registro AS consecutivoRegistro,
			        v.id_archivo_cargado AS idArchivoCargado,
			        v.id_registro AS idRegistro,
			        v.ids_liquidacion_app AS idsLiquidacionApp,
			        v.ids_liquidacion_tdv AS idsLiquidacionTdv,
			        v.tipo_transaccion AS tipoTransaccion,
			        v.entidad AS entidad,
			        v.fecha_servicio_transporte AS fechaServicioTransporte,
			        v.identificacion_cliente AS identificacionCliente,
			        v.razon_social AS razonSocial,
			        v.codigo_punto_cargo AS codigoPuntoCargo,
			        v.nombre_punto_cargo AS nombrePuntoCargo,
			        v.ciudad_fondo AS ciudadFondo,
			        v.nombre_tipo_servicio AS nombreTipoServicio,
			        v.tipo_operacion AS tipoOperacion,
			        v.moneda_divisa AS monedaDivisa,
			        v.aplicativo AS aplicativo,
			        v.tdv AS tdv,
			        v.valor_procesado_billete AS valorProcesadoBillete,
			        v.valor_procesado_billete_tdv AS valorProcesadoBilleteTdv,
			        v.valor_procesado_moneda AS valorProcesadoMoneda,
			        v.valor_procesado_moneda_tdv AS valorProcesadoMonedaTdv,
			        v.valor_total_procesado AS valorTotalProcesado,
			        v.valor_total_procesado_tdv AS valorTotalProcesadoTdv,
			        v.subtotal AS subtotal,
			        v.subtotal_tdv AS subtotalTdv,
			        v.iva AS iva,
			        v.valor_total AS valorTotal,
			        v.clasificacion_fajado AS clasificacionFajado,
			        v.clasificacion_fajado_tdv AS clasificacionFajadoTdv,
			        v.clasificacion_no_fajado AS clasificacionNoFajado,
			        v.clasificacion_no_fajado_tdv AS clasificacionNoFajadoTdv,
			        v.costo_paqueteo AS costoPaqueteo,
			        v.costo_paqueteo_tdv AS costoPaqueteoTdv,
			        v.moneda_residuo AS monedaResiduo,
			        v.moneda_residuo_tdv AS monedaResiduoTdv,
			        v.billete_residuo AS billeteResiduo,
			        v.billete_residuo_tdv AS billeteResiduoTdv,
			        v.valor_almacenamiento_billete AS valorAlmacenamientoBillete,
			        v.valor_almacenamiento_billete_tdv AS valorAlmacenamientoBilleteTdv,
			        v.valor_almacenamiento_moneda AS valorAlmacenamientoMoneda,
			        v.valor_almacenamiento_moneda_tdv AS valorAlmacenamientoMonedaTdv,
			        v.estado AS estado,
			        v.modulo AS modulo,
			        v.id_llaves_maestro_tdv AS idLlavesMaestroTdv,
			        v.id_llaves_maestro_app AS idLlavesMaestroApp
			    FROM controlefect.v_detalle_liquidacion_procesamiento v
			    LEFT JOIN controlefect.maestro_llaves_costos mc
			        ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			    WHERE v.id_archivo_cargado = :idArchivo
			""", nativeQuery = true)
	List<IDetalleLiquidacionProcesamiento> obtenerDetallesPorIdArchivoProcesamiento(
			@Param("idArchivo") Integer idArchivo);

	@Query(value = """
			    SELECT
			        mc.id_llave AS idLlave,
			        v.consecutivo_registro AS consecutivoRegistro,
			        v.id_archivo_cargado AS idArchivoCargado,
			        v.id_registro AS idRegistro,
			        v.ids_liquidacion_app AS idsLiquidacionApp,
			        v.ids_liquidacion_tdv AS idsLiquidacionTdv,
			        v.tipo_transaccion AS tipoTransaccion,
			        v.entidad AS entidad,
			        v.fecha_servicio_transporte AS fechaServicioTransporte,
			        v.identificacion_cliente AS identificacionCliente,
			        v.razon_social AS razonSocial,
			        v.codigo_punto_cargo AS codigoPuntoCargo,
			        v.nombre_punto_cargo AS nombrePuntoCargo,
			        v.ciudad_fondo AS ciudadFondo,
			        v.nombre_tipo_servicio AS nombreTipoServicio,
			        v.tipo_operacion AS tipoOperacion,
			        v.moneda_divisa AS monedaDivisa,
			        v.aplicativo AS aplicativo,
			        v.tdv AS tdv,
			        v.valor_procesado_billete AS valorProcesadoBillete,
			        v.valor_procesado_billete_tdv AS valorProcesadoBilleteTdv,
			        v.valor_procesado_moneda AS valorProcesadoMoneda,
			        v.valor_procesado_moneda_tdv AS valorProcesadoMonedaTdv,
			        v.valor_total_procesado AS valorTotalProcesado,
			        v.valor_total_procesado_tdv AS valorTotalProcesadoTdv,
			        v.subtotal AS subtotal,
			        v.subtotal_tdv AS subtotalTdv,
			        v.iva AS iva,
			        v.valor_total AS valorTotal,
			        v.clasificacion_fajado AS clasificacionFajado,
			        v.clasificacion_fajado_tdv AS clasificacionFajadoTdv,
			        v.clasificacion_no_fajado AS clasificacionNoFajado,
			        v.clasificacion_no_fajado_tdv AS clasificacionNoFajadoTdv,
			        v.costo_paqueteo AS costoPaqueteo,
			        v.costo_paqueteo_tdv AS costoPaqueteoTdv,
			        v.moneda_residuo AS monedaResiduo,
			        v.moneda_residuo_tdv AS monedaResiduoTdv,
			        v.billete_residuo AS billeteResiduo,
			        v.billete_residuo_tdv AS billeteResiduoTdv,
			        v.valor_almacenamiento_billete AS valorAlmacenamientoBillete,
			        v.valor_almacenamiento_billete_tdv AS valorAlmacenamientoBilleteTdv,
			        v.valor_almacenamiento_moneda AS valorAlmacenamientoMoneda,
			        v.valor_almacenamiento_moneda_tdv AS valorAlmacenamientoMonedaTdv,
			        v.estado AS estado,
			        v.modulo AS modulo,
			        v.id_llaves_maestro_tdv AS idLlavesMaestroTdv,
			        v.id_llaves_maestro_app AS idLlavesMaestroApp
			    FROM controlefect.v_detalle_liquidacion_procesamiento v
			    LEFT JOIN controlefect.maestro_llaves_costos mc
			        ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) AND mc.estado IN('PENDIENTE','RECHAZADA')
			    WHERE v.modulo = :modulo
			      AND mc.id_llave = :idLlave			  
			""", nativeQuery = true)
	List<IDetalleLiquidacionProcesamiento> obtenerDetallesPorModuloProcesamiento(@Param("modulo") String modulo,
			@Param("idLlave") Long idLlave);
	
	
	@Transactional(readOnly = true)
	@Query(value = """
	        SELECT
	            mc.id_llave AS consecutivo_registro,
	            v.id_archivo_cargado,
	            mc.id_llave AS id_registro,
	            mc.id_llave AS id_liquidacion,
	            v.tipo_transaccion,
	            v.entidad,
	            v.fecha_servicio_transporte,
	            v.identificacion_cliente,
	            v.razon_social,
	            v.codigo_punto_cargo,
	            v.nombre_punto_cargo,
	            v.ciudad_fondo,
	            v.nombre_tipo_servicio,
	            v.moneda_divisa,
	            v.aplicativo,
	            v.tdv,
	            v.valor_procesado_billete,
	            v.valor_procesado_billete_tdv,
	            v.valor_procesado_moneda,
	            v.valor_procesado_moneda_tdv,
	            v.valor_total_procesado,
	            v.valor_total_procesado_tdv,
	            v.subtotal,
	            v.subtotal_tdv,
	            v.iva,
	            v.valor_total,
	            v.clasificacion_fajado,
	            v.clasificacion_fajado_tdv,
	            v.clasificacion_no_fajado,
	            v.clasificacion_no_fajado_tdv,
	            v.costo_paqueteo,
	            v.costo_paqueteo_tdv,
	            v.moneda_residuo,
	            v.moneda_residuo_tdv,
	            v.billete_residuo,
	            v.billete_residuo_tdv,
	            v.valor_almacenamiento_billete, 
				v.valor_almacenamiento_billete_tdv, 
				v.valor_almacenamiento_moneda, 
				v.valor_almacenamiento_moneda_tdv,
	            v.estado,
	            v.modulo
	        FROM controlefect.v_detalle_liquidacion_procesamiento v
	        LEFT JOIN controlefect.maestro_llaves_costos mc
	            ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) AND mc.estado IN('PENDIENTE','RECHAZADA')
	        WHERE mc.id_llave = :consecutivoRegistro
	        """, nativeQuery = true)
	Optional<OperacionesLiquidacionProcesamientoEntity> consultarConsecutivoRegistroProc(
	        @Param("consecutivoRegistro") Integer consecutivoRegistro);

}


