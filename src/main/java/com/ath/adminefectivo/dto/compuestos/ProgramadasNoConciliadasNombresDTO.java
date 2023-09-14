package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.dto.ProgramadasNoConciliadasDTO;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo de Operaciones Programadas No Conciliadas
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramadasNoConciliadasNombresDTO {

	@JsonProperty("idOperacion")
	private Integer idOperacion;
	
	@JsonProperty("codigoFondoTDV")
	private Integer codigoFondoTDV;
	
	private String nombreFondoTDV;
	
	@JsonProperty("entradaSalida")
	private String entradaSalida;
	
	@JsonProperty("codigoPuntoOrigen")
	private Integer codigoPuntoOrigen;
	
	private String nombrePuntoOrigen;
	
	@JsonProperty("codigoPuntoDestino")
	private Integer codigoPuntoDestino;
	
	private String nombrePuntoDestino;
	
	@JsonProperty("fechaProgramacion")
	private Date fechaProgramacion;
	
	@JsonProperty("fechaOrigen")
	private Date fechaOrigen;
	
	@JsonProperty("fechaDestino")
	private Date fechaDestino;
	
	@JsonProperty("tipoOperacion")
	private String tipoOperacion;
	
	@JsonProperty("tipoTransporte")
	private String tipoTransporte;
	
	@JsonProperty("valorTotal")
	private Double valorTotal;
	
	@JsonProperty("estadoOperacion")
	private String estadoOperacion;
	
	@JsonProperty("idNegociacion")
	private String idNegociacion;
	
	@JsonProperty("tasaNegociacion")
	private String tasaNegociacion;
	
	@JsonProperty("estadoConciliacion")
	private String estadoConciliacion;
	
	@JsonProperty("idArchivoCargado")
	private Integer idArchivoCargado;
	
	@JsonProperty("idOperacionRelac")
	private Integer idOperacionRelac;
	
	@JsonProperty("tipoServicio")
	private String tipoServicio;
	
	@JsonProperty("usuarioCreacion")
	private String usuarioCreacion;
	
	@JsonProperty("usuarioModificacion")
	private String usuarioModificacion;
	
	@JsonProperty("fechaCreacion")
	private Date fechaCreacion;
	
	@JsonProperty("fechaModificacion")
	private Date fechaModificacion;
	
	@JsonProperty("idServicio")
	private String idServicio;
	
	private String tdv;

	private String bancoAVAL;
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasDTO
	 * @author cesar.castano
	 */
	public static final Function<ProgramadasNoConciliadasDTO, ProgramadasNoConciliadasNombresDTO> CONVERTER_NO_PROGRAMADAS_NOMBRES = (ProgramadasNoConciliadasDTO t) -> {

		var operacionesProgramadasNombresDto = new ProgramadasNoConciliadasNombresDTO();
		UtilsObjects.copiarPropiedades(t, operacionesProgramadasNombresDto);		

		return operacionesProgramadasNombresDto;
	};
}
