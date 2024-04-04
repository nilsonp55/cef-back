package com.ath.adminefectivo.repositories;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;

public interface IOperacionesLiquidacionTransporte
		extends CrudRepository<OperacionesLiquidacionTransporteEntity, Integer> {
	@Transactional(readOnly = true)
	@Query(value = " SELECT " + "* FROM " + "v_detalle_liquidacion_transporte  v"
			+ "	WHERE (:entidad is null or v.entidad = cast(:entidad AS text))  "
			+ " AND  (:identificacionCliente is null or v.identificacion_cliente = cast(:identificacionCliente AS text))"
			+ " AND  (:razonSocial is null or v.razon_social = cast(:razonSocial AS text))  "
			+ " AND  (:codigoPuntoCargo is null or v.codigo_punto_cargo = cast(:codigoPuntoCargo AS text)) "
			+ " AND  (:nombrePuntoCargo is null or v.nombre_punto_cargo = cast(:nombrePuntoCargo AS text))  "
			+ " AND  (:ciudadFondo is null or v.ciudad_fondo = cast(:ciudadFondo AS text))  "
			+ " AND  (:nombreTipoServicio is null or v.nombre_tipo_servicio = cast(:nombreTipoServicio AS text))  "
			+ " AND  (:monedaDivisa is null or v.moneda_divisa = cast(:monedaDivisa AS text))  "
			+ " AND  (:estado is null or v.estado = cast(:estado AS text)) " + " AND  (v.modulo =:modulo)  "
			+ " AND  (  v.fecha_servicio_transporte BETWEEN :fechaServicioTransporte AND :fechaServicioTransporteFinal ) ",

			nativeQuery = true)

	Page<OperacionesLiquidacionTransporteEntity> conciliadasLiquidadasTransporte(
			@Param("entidad") String entidad,
			@Param("fechaServicioTransporte") LocalDateTime  fechaServicioTransporte,
			@Param("fechaServicioTransporteFinal") LocalDateTime  fechaServicioTransporteFinal,
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

}
