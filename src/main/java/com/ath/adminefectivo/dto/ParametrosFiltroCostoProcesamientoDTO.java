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
public class ParametrosFiltroCostoProcesamientoDTO {

	private String entity; 
	private Date processServiceDate; 
	private Date finalProcessServiceDate; 
	private String clientIdentification; 
	private String socialReason; 
	private String cargoPointCode; 
	private String cargoPointName; 
	private String cityBackground; 
	private String serviceTypeName; 
	private String currency; 
	private String status; 
	private Pageable page;
	
	public static ParametrosFiltroCostoProcesamientoDTO create(String entity, Date processServiceDate, Date finalProcessServiceDate, 
			String clientIdentification, String socialReason, String cargoPointCode, String cargoPointName, 
			String cityBackground, String serviceTypeName, String currency, String status, Pageable page) {
		ParametrosFiltroCostoProcesamientoDTO filtros = new ParametrosFiltroCostoProcesamientoDTO();
		filtros.setEntity(entity);
		filtros.setProcessServiceDate(processServiceDate);
		filtros.setFinalProcessServiceDate(finalProcessServiceDate);
		filtros.setClientIdentification(clientIdentification);
		filtros.setSocialReason(socialReason);
		filtros.setCargoPointCode(cargoPointCode);
		filtros.setCargoPointName(cargoPointName);
		filtros.setCityBackground(cityBackground);
		filtros.setServiceTypeName(serviceTypeName);
		filtros.setCurrency(currency);
		filtros.setStatus(status);
		filtros.setPage(page);
		return filtros;
	}
	
}
