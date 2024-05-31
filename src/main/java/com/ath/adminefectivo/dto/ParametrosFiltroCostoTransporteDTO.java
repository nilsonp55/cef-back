package com.ath.adminefectivo.dto;

import java.util.Date;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de de los filtros pasados por parametros para la 
 * consulta de las conciliacion de costos
 *
 * @author hector.mercado
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosFiltroCostoTransporteDTO {
	
	private String entidad;
	private Date fechaServicioTransporte;
	private Date fechaServicioTransporteFinal;
	private String identificacionCliente; 
	private String razonSocial; 
	private String codigoPuntoCargo; 
	private String nombrePuntoCargo;
	private String ciudadFondo; 
	private String nombreTipoServicio; 
	private String monedaDivisa; 
	private String estado; 
	private Pageable page;
	
	public static ParametrosFiltroCostoTransporteDTO create(String entidad, Date fechaServicioTransporte, Date fechaServicioTransporteFinal, 
			String identificacionCliente, String razonSocial, String codigoPuntoCargo, String nombrePuntoCargo, 
			String ciudadFondo, String nombreTipoServicio, String monedaDivisa, String estado, Pageable page) {
		ParametrosFiltroCostoTransporteDTO filtros = new ParametrosFiltroCostoTransporteDTO();
		filtros.setEntidad(entidad);
		filtros.setFechaServicioTransporte(fechaServicioTransporte);
		filtros.setFechaServicioTransporteFinal(fechaServicioTransporteFinal);
		filtros.setIdentificacionCliente(identificacionCliente);
		filtros.setRazonSocial(razonSocial);
		filtros.setCodigoPuntoCargo(codigoPuntoCargo);
		filtros.setNombrePuntoCargo(nombrePuntoCargo);
		filtros.setCiudadFondo(ciudadFondo);
		filtros.setNombreTipoServicio(nombreTipoServicio);
		filtros.setMonedaDivisa(monedaDivisa);
		filtros.setEstado(estado);
		filtros.setPage(page);
		return filtros;
	}
}
