package com.ath.adminefectivo.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;

public interface IConciliacionOperacionesProcesamientoRepository extends CrudRepository<OperacionesLiquidacionProcesamientoEntity, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT " + "* FROM " + "v_detalle_liquidacion_procesamiento v "
	        + " WHERE (:entity is null or v.entidad = cast(:entity AS text)) "
			+ " AND  (:clientIdentification is null or v.identificacion_cliente = cast(:clientIdentification AS text))"
			+ " AND  (:businessName is null or v.razon_social = cast(:businessName AS text))  "
			+ " AND  (:cargoPointCode is null or v.codigo_punto_cargo = cast(:cargoPointCode AS text)) "
			+ " AND  (:cargoPointName is null or v.nombre_punto_cargo = cast(:cargoPointName AS text))  "
			+ " AND  (:fundCity is null or v.ciudad_fondo = cast(:fundCity AS text))  "
			+ " AND  (:serviceTypeName is null or v.nombre_tipo_servicio = cast(:serviceTypeName AS text))  "
			+ " AND  (:currencyExchange is null or v.moneda_divisa = cast(:currencyExchange AS text))  "
			+ " AND  (:state is null or v.estado = cast(:state AS text)) " + " AND  (v.modulo =:module)  "
			+ " AND  (  v.fecha_servicio_transporte BETWEEN :fechaServicioTransporte AND :fechaServicioTransporteFinal ) ",
			
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
						Pageable pageable);
	
	Optional<OperacionesLiquidacionProcesamientoEntity> findByRecordConsecutive(Integer recordConsecutive);
	
	
}


