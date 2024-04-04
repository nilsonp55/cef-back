package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IConciliacionCostosTransporteDelegate;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.ConciliacionCostosTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.service.ICostosTransporteService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConciliacionCostosTransporteDelegateImpl implements IConciliacionCostosTransporteDelegate {

	@Autowired
	ICostosTransporteService costosTransporteService;

	@Override
	public List<ConciliacionCostosTransporteDTO> conciliadas(String entidad, String identificacion) {
		return costosTransporteService.conciliadasDto(entidad, identificacion);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {

		return costosTransporteService.getLiquidacionConciliadaTransporte(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {

		return costosTransporteService.getLiquidacionRemitidasNoIdentificadasTransporte(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {

		return costosTransporteService.getLiquidadasNoCobradasTransporte(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(String entidad,
			Date fecha_servicio_transporte, Date fecha_servicio_transporte_final, String identificacion_cliente,
			String razon_social, String codigo_punto_cargo, String nombre_punto_cargo, String ciudad_fondo,
			String nombre_tipo_servicio, String moneda_divisa, String estado, Pageable page) {

		return costosTransporteService.getIdentificadasConDiferenciasTransporte(entidad, fecha_servicio_transporte,
				fecha_servicio_transporte_final, identificacion_cliente, razon_social, codigo_punto_cargo,
				nombre_punto_cargo, ciudad_fondo, nombre_tipo_servicio, moneda_divisa, estado, page);
	}
	
	@Override
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros) {

		return costosTransporteService.desconciliar(registros);
	}
	
	@Override
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros) {

		return costosTransporteService.remitidasAceptarRechazar(registros);
	}

}

