package com.ath.adminefectivo.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.ParametrosFiltroConciliacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesProcesamientoRepository;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConciliacionOperacionesProcesamientoServiceImpl implements IConciliacionOperacionesProcesamientoService {

	@Autowired
	IConciliacionOperacionesProcesamientoRepository operacionesLiquidacion;

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			ParametrosFiltroConciliacionCostoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroConciliacionCostoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }
	
	private Page<OperacionesLiquidacionProcesamientoDTO> liquidacion(
			Page<OperacionesLiquidacionProcesamientoEntity> liquidadas, Pageable page) {

		return new PageImpl<>(
				liquidadas.getContent().stream().map(e -> OperacionesLiquidacionProcesamientoDTO.CONVERTER_DTO.apply(e))
						.collect(Collectors.<OperacionesLiquidacionProcesamientoDTO>toList()),
				page, liquidadas.getTotalElements());
	}

}
