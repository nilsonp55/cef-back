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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;
import com.ath.adminefectivo.service.IDetalleLiquidacionProcesamiento;
import com.ath.adminefectivo.service.IDetalleLiquidacionTransporte;

@Repository
public interface IOperacionesLiquidacionTransporte
		extends CrudRepository<OperacionesLiquidacionTransporteEntity, Integer> {
	
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
		    v.tipo_pedido,
		    v.tipo_pedido_tdv,
		    v.escala,
		    v.escala_tdv,
		    v.valor_transportado_billetes,
		    v.valor_transportado_billetes_tdv,
		    v.valor_transportado_monedas,
		    v.valor_transportado_monedas_tdv,
		    v.valor_total_transportado,
		    v.valor_total_transportado_tdv,
		    v.numero_fajos,
		    v.numero_fajos_tdv,
		    v.numero_bolsas,
		    v.numero_bolsas_tdv,
		    v.costo_fijo,
		    v.costo_fijo_tdv,
		    v.costo_milaje,
		    v.costo_milaje_tdv,
		    v.costo_bolsa,
		    v.costo_bolsa_tdv,
		    v.costo_flete,
		    v.costo_flete_tdv,
		    v.costo_emisario,
		    v.costo_emisario_tdv,
		    v.otros1,
		    v.otros2,
		    v.otros3,
		    v.otros4,
		    v.otros5,
		    v.subtotal,
		    v.subtotal_tdv,
		    v.iva,
		    v.valor_total,
		    v.estado,
		    v.modulo
	    FROM controlefect.v_detalle_liquidacion_transporte v
	    LEFT JOIN controlefect.maestro_llaves_costos mc 
	        ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) AND mc.estado IN('PENDIENTE','RECHAZADA')
	    WHERE 
	        (:entidad IS NULL OR v.entidad = CAST(:entidad AS TEXT))
	        AND (:identificacionCliente IS NULL OR v.identificacion_cliente = CAST(:identificacionCliente AS TEXT))
	        AND (:razonSocial IS NULL OR v.razon_social = CAST(:razonSocial AS TEXT))
	        AND (:codigoPuntoCargo IS NULL OR v.codigo_punto_cargo = CAST(:codigoPuntoCargo AS TEXT))
	        AND (:nombrePuntoCargo IS NULL OR v.nombre_punto_cargo = CAST(:nombrePuntoCargo AS TEXT))
	        AND (:ciudadFondo IS NULL OR v.ciudad_fondo = CAST(:ciudadFondo AS TEXT))
	        AND (:nombreTipoServicio IS NULL OR v.nombre_tipo_servicio = CAST(:nombreTipoServicio AS TEXT))
	        AND (:monedaDivisa IS NULL OR v.moneda_divisa = CAST(:monedaDivisa AS TEXT))
	        AND (:estado IS NULL OR v.estado = CAST(:estado AS TEXT))
	        AND (v.modulo = :modulo)
	        AND (v.fecha_servicio_transporte BETWEEN :fechaServicioTransporte AND :fechaServicioTransporteFinal)
	    """, nativeQuery = true)
	
	Page<OperacionesLiquidacionTransporteEntity> conciliadasLiquidadasTransporte(
			@Param("entidad") String entidad,
			@Param("fechaServicioTransporte") LocalDateTime fechaServicioTransporte,
			@Param("fechaServicioTransporteFinal") LocalDateTime fechaServicioTransporteFinal,
			@Param("identificacionCliente") String identificacionCliente,
			@Param("razonSocial") String razonSocial,
			@Param("codigoPuntoCargo") String codigoPuntoCargo,
			@Param("nombrePuntoCargo") String nombrePuntocargo, 
			@Param("ciudadFondo") String ciudadFondo,
			@Param("nombreTipoServicio") String nombreTipoServicio, 
			@Param("monedaDivisa") String monedaDivisa,
			@Param("estado") String estado, 
			@Param("modulo") String modulo, 
			Pageable pageable);

	@Transactional(readOnly = true)
	@Query(value = "SELECT " + "* FROM " + "v_detalle_liquidacion_transporte v"
			+ " WHERE (:entidad is null or v.entidad = cast(:entidad AS text))"
			+ " AND (:identificacionCliente is null or v.identificacion_cliente = cast(:identificacionCliente AS text))"
			+ " AND (:razonSocial is null or v.razon_social = cast(:razonSocial AS text))"
			+ " AND (:codigoPuntoCargo is null or v.codigo_punto_cargo = cast(:codigoPuntoCargo AS text))"
			+ " AND (:nombrePuntoCargo is null or v.nombre_punto_cargo = cast(:nombrePuntoCargo AS text))"
			+ " AND (:ciudadFondo is null or v.ciudad_fondo = cast(:ciudadFondo AS text))"
			+ " AND (:nombreTipoServicio is null or v.nombre_tipo_servicio = cast(:nombreTipoServicio AS text))"
			+ " AND (:monedaDivisa is null or v.moneda_divisa = cast(:monedaDivisa AS text))"
			+ " AND (:estado is null or v.estado = cast(:estado AS text))"
			+ " AND (v.fecha_servicio_transporte BETWEEN :fechaServicioTransporte AND :fechaServicioTransporteFinal)",
			nativeQuery = true)

	Page<OperacionesLiquidacionTransporteEntity> registrosNoModuloTransporte(
			@Param("entidad") String entidad,
			@Param("fechaServicioTransporte") LocalDateTime fechaServicioTransporte,
			@Param("fechaServicioTransporteFinal") LocalDateTime fechaServicioTransporteFinal,
			@Param("identificacionCliente") String identificacionCliente,
			@Param("razonSocial") String razonSocial,
			@Param("codigoPuntoCargo") String codigoPuntoCargo,
			@Param("nombrePuntoCargo") String nombrePuntocargo, 
			@Param("ciudadFondo") String ciudadFondo,
			@Param("nombreTipoServicio") String nombreTipoServicio, 
			@Param("monedaDivisa") String monedaDivisa,
			@Param("estado") String estado, 
			Pageable pageable);
	
	Optional<OperacionesLiquidacionTransporteEntity> findByConsecutivoRegistro(Integer consecutivoRegistro);
	
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
			    v.moneda_divisa AS monedaDivisa,
			    v.aplicativo AS aplicativo,
			    v.tdv AS tdv,
			    v.tipo_pedido AS tipoPedido,
			    v.tipo_pedido_tdv AS tipoPedidoTdv,
			    v.escala AS escala,
			    v.escala_tdv AS escalaTdv,
			    v.valor_transportado_billetes AS valorTransportadoBilletes,
			    v.valor_transportado_billetes_tdv AS valorTransportadoBilletesTdv,
			    v.valor_transportado_monedas AS valorTransportadoMonedas,
			    v.valor_transportado_monedas_tdv AS valorTransportadoMonedasTdv,
			    v.valor_total_transportado AS valorTotalTransportado,
			    v.valor_total_transportado_tdv AS valorTotalTransportadoTdv,
			    v.numero_fajos AS numeroFajos,
			    v.numero_fajos_tdv AS numeroFajosTdv,
			    v.numero_bolsas AS numeroBolsas,
			    v.numero_bolsas_tdv AS numeroBolsasTdv,
			    v.costo_fijo AS costoFijo,
			    v.costo_fijo_tdv AS costoFijoTdv,
			    v.costo_milaje AS costoMilaje,
			    v.costo_milaje_tdv AS costoMilajeTdv,
			    v.costo_bolsa AS costoBolsa,
			    v.costo_bolsa_tdv AS costoBolsaTdv,
			    v.costo_flete AS costoFlete,
			    v.costo_flete_tdv AS costoFleteTdv,
			    v.costo_emisario AS costoEmisario,
			    v.costo_emisario_tdv AS costoEmisarioTdv,
			    v.otros1 AS otros1,
			    v.otros2 AS otros2,
			    v.otros3 AS otros3,
			    v.otros4 AS otros4,
			    v.otros5 AS otros5,
			    v.subtotal AS subtotal,
			    v.subtotal_tdv AS subtotalTdv,
			    v.iva AS iva,
			    v.valor_total AS valorTotal,
			    v.estado AS estado,
			    v.modulo AS modulo,
			    v.id_llaves_maestro_tdv AS idLlavesMaestroTdv,
			    v.id_llaves_maestro_app AS idLlavesMaestroApp
			FROM controlefect.v_detalle_liquidacion_transporte v
			LEFT JOIN controlefect.maestro_llaves_costos mc
			    ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			WHERE v.id_llaves_maestro_tdv = :idLlaveMaestro
			""", nativeQuery = true)
	List<IDetalleLiquidacionTransporte> countConciliadasTransporteByLlave(
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
			    v.moneda_divisa AS monedaDivisa,
			    v.aplicativo AS aplicativo,
			    v.tdv AS tdv,
			    v.tipo_pedido AS tipoPedido,
			    v.tipo_pedido_tdv AS tipoPedidoTdv,
			    v.escala AS escala,
			    v.escala_tdv AS escalaTdv,
			    v.valor_transportado_billetes AS valorTransportadoBilletes,
			    v.valor_transportado_billetes_tdv AS valorTransportadoBilletesTdv,
			    v.valor_transportado_monedas AS valorTransportadoMonedas,
			    v.valor_transportado_monedas_tdv AS valorTransportadoMonedasTdv,
			    v.valor_total_transportado AS valorTotalTransportado,
			    v.valor_total_transportado_tdv AS valorTotalTransportadoTdv,
			    v.numero_fajos AS numeroFajos,
			    v.numero_fajos_tdv AS numeroFajosTdv,
			    v.numero_bolsas AS numeroBolsas,
			    v.numero_bolsas_tdv AS numeroBolsasTdv,
			    v.costo_fijo AS costoFijo,
			    v.costo_fijo_tdv AS costoFijoTdv,
			    v.costo_milaje AS costoMilaje,
			    v.costo_milaje_tdv AS costoMilajeTdv,
			    v.costo_bolsa AS costoBolsa,
			    v.costo_bolsa_tdv AS costoBolsaTdv,
			    v.costo_flete AS costoFlete,
			    v.costo_flete_tdv AS costoFleteTdv,
			    v.costo_emisario AS costoEmisario,
			    v.costo_emisario_tdv AS costoEmisarioTdv,
			    v.otros1 AS otros1,
			    v.otros2 AS otros2,
			    v.otros3 AS otros3,
			    v.otros4 AS otros4,
			    v.otros5 AS otros5,
			    v.subtotal AS subtotal,
			    v.subtotal_tdv AS subtotalTdv,
			    v.iva AS iva,
			    v.valor_total AS valorTotal,
			    v.estado AS estado,
			    v.modulo AS modulo,
			    v.id_llaves_maestro_tdv AS idLlavesMaestroTdv,
			    v.id_llaves_maestro_app AS idLlavesMaestroApp
			FROM controlefect.v_detalle_liquidacion_transporte v
			LEFT JOIN controlefect.maestro_llaves_costos mc
			    ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv)
			WHERE v.id_archivo_cargado = :idArchivo
			  AND mc.id_llave = :idLlave
			""", nativeQuery = true)
	List<IDetalleLiquidacionTransporte> obtenerDetallesPorIdArchivo(@Param("idArchivo") Integer idArchivo);
	
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
			    v.moneda_divisa AS monedaDivisa,
			    v.aplicativo AS aplicativo,
			    v.tdv AS tdv,
			    v.tipo_pedido AS tipoPedido,
			    v.tipo_pedido_tdv AS tipoPedidoTdv,
			    v.escala AS escala,
			    v.escala_tdv AS escalaTdv,
			    v.valor_transportado_billetes AS valorTransportadoBilletes,
			    v.valor_transportado_billetes_tdv AS valorTransportadoBilletesTdv,
			    v.valor_transportado_monedas AS valorTransportadoMonedas,
			    v.valor_transportado_monedas_tdv AS valorTransportadoMonedasTdv,
			    v.valor_total_transportado AS valorTotalTransportado,
			    v.valor_total_transportado_tdv AS valorTotalTransportadoTdv,
			    v.numero_fajos AS numeroFajos,
			    v.numero_fajos_tdv AS numeroFajosTdv,
			    v.numero_bolsas AS numeroBolsas,
			    v.numero_bolsas_tdv AS numeroBolsasTdv,
			    v.costo_fijo AS costoFijo,
			    v.costo_fijo_tdv AS costoFijoTdv,
			    v.costo_milaje AS costoMilaje,
			    v.costo_milaje_tdv AS costoMilajeTdv,
			    v.costo_bolsa AS costoBolsa,
			    v.costo_bolsa_tdv AS costoBolsaTdv,
			    v.costo_flete AS costoFlete,
			    v.costo_flete_tdv AS costoFleteTdv,
			    v.costo_emisario AS costoEmisario,
			    v.costo_emisario_tdv AS costoEmisarioTdv,
			    v.otros1 AS otros1,
			    v.otros2 AS otros2,
			    v.otros3 AS otros3,
			    v.otros4 AS otros4,
			    v.otros5 AS otros5,
			    v.subtotal AS subtotal,
			    v.subtotal_tdv AS subtotalTdv,
			    v.iva AS iva,
			    v.valor_total AS valorTotal,
			    v.estado AS estado,
			    v.modulo AS modulo,
			    v.id_llaves_maestro_tdv AS idLlavesMaestroTdv,
			    v.id_llaves_maestro_app AS idLlavesMaestroApp
			FROM controlefect.v_detalle_liquidacion_transporte v
			LEFT JOIN controlefect.maestro_llaves_costos mc
			    ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) AND mc.estado IN('PENDIENTE','RECHAZADA')
			WHERE v.modulo = :modulo
			  AND mc.id_llave = :idLlave
			""", nativeQuery = true)
	List<IDetalleLiquidacionTransporte> obtenerDetallesPorModulo(@Param("modulo") String modulo,
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
			    v.tipo_pedido,
			    v.tipo_pedido_tdv,
			    v.escala,
			    v.escala_tdv,
			    v.valor_transportado_billetes,
			    v.valor_transportado_billetes_tdv,
			    v.valor_transportado_monedas,
			    v.valor_transportado_monedas_tdv,
			    v.valor_total_transportado,
			    v.valor_total_transportado_tdv,
			    v.numero_fajos,
			    v.numero_fajos_tdv,
			    v.numero_bolsas,
			    v.numero_bolsas_tdv,
			    v.costo_fijo,
			    v.costo_fijo_tdv,
			    v.costo_milaje,
			    v.costo_milaje_tdv,
			    v.costo_bolsa,
			    v.costo_bolsa_tdv,
			    v.costo_flete,
			    v.costo_flete_tdv,
			    v.costo_emisario,
			    v.costo_emisario_tdv,
			    v.otros1,
			    v.otros2,
			    v.otros3,
			    v.otros4,
			    v.otros5,
			    v.subtotal,
			    v.subtotal_tdv,
			    v.iva,
			    v.valor_total,
			    v.estado,
			    v.modulo
			FROM controlefect.v_detalle_liquidacion_transporte v
			LEFT JOIN controlefect.maestro_llaves_costos mc
			    ON mc.id_maestro_llave = COALESCE(v.id_llaves_maestro_app, v.id_llaves_maestro_tdv) AND mc.estado IN('PENDIENTE','RECHAZADA')
			WHERE mc.id_llave = :consecutivoRegistro
			""", nativeQuery = true)
	Optional<OperacionesLiquidacionTransporteEntity> consultarConsecutivoRegistro(
			@Param("consecutivoRegistro") Integer consecutivoRegistro);
	
	@Query(value = """
			SELECT v.id_llaves_maestro_app AS idLlavesMaestroApp
			FROM controlefect.v_detalle_liquidacion_transporte v
			WHERE v.ids_liquidacion_app LIKE %:idLiquidacionApp%
			LIMIT 1
			""", nativeQuery = true)
	BigInteger obtenerIdLlavesMaestroAppPorIdsLiquidacionApp(@Param("idLiquidacionApp") String idLiquidacionApp);

}
