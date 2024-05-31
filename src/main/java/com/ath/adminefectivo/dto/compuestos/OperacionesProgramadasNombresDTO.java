package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo de Operaciones Programadas con Nombres
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionesProgramadasNombresDTO {

	@JsonProperty("Banco")
	private String nombreBanco;
	
	@JsonProperty("Transportadora")
	private String nombreTransportadora;
	
	@JsonProperty("ciudadOrigen")
	private String nombreCiudadOrigen;
	
	@JsonProperty("ciudadDestino")
	private String nombreCiudadDestino;
	
	@JsonProperty("tipoOperacion")
	private String tipoOperacion;
	
	@JsonProperty("puntoOrigen")
	private String nombrePuntoOrigen;
	
	@JsonProperty("puntoDestino")
	private String nombrePuntoDestino;
	
	@JsonProperty("valorTotal")
	private Double valorTotal;
	
	@JsonProperty("tipoConciliacion")
	private String tipoConciliacion;
	
	@JsonProperty("fechaEjecucion")
	private Date fechaEjecucion;
	
	@JsonProperty("estadoConciliacion")
	private String estadoConciliacion;
	
	@JsonProperty("idConciliacion")
	private Integer idConciliacion;
	
	private String tdv;

	private String bancoAVAL;
	
	private String tipoPuntoOrigen;
	
	private String tipoPuntoDestino;
	
	private String entradaSalida;
	
	private String nombreFondoTDV;
	
	/**
	 * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity OperacionesProgramadas
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadasNombresDTO, OperacionesProgramadas> CONVERTER_ENTITY = (OperacionesProgramadasNombresDTO t) -> {

		var operacionesProgramadas = new OperacionesProgramadas();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadas);		

		return operacionesProgramadas;
	};
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasDTO
	 * @author cesar.castano
	 */
	public static final Function<OperacionesProgramadas, OperacionesProgramadasNombresDTO> CONVERTER_DTO = (OperacionesProgramadas t) -> {

		var operacionesProgramadasDTO = new OperacionesProgramadasNombresDTO();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadasDTO);		

		return operacionesProgramadasDTO;
	};
}
