package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo de Certificadas Programadas No Conciliadas
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificadasNoConciliadasNombresDTO {

	@JsonProperty("idCertificacion")
	private Integer idCertificacion;
	
	@JsonProperty("codigoFondoTDV")
	private Integer codigoFondoTDV;
	
	private String nombreFondoTDV;
	
	@JsonProperty("codigoBanco")
	private Integer codigoBanco;
	
	@JsonProperty("codigoCiudad")
	private Integer codigoCiudad;
	
	@JsonProperty("codigoPuntoOrigen")
	private Integer codigoPuntoOrigen;
	
	private String nombrePuntoOrigen;
	
	@JsonProperty("codigoPuntoDestino")
	private Integer codigoPuntoDestino;
	
	private String nombrePuntoDestino;
	
	@JsonProperty("tipoPuntoOrigen")
	private String tipoPuntoOrigen;
	
	@JsonProperty("tipoPuntoDestino")
	private String tipoPuntoDestino;
	
	@JsonProperty("fechaEjecucion")
	private Date fechaEjecucion;
	
	@JsonProperty("tipoOperacion")
	private String tipoOperacion;
	
	@JsonProperty("tipoServicio")
	private String tipoServicio;
	
	@JsonProperty("estadoConciliacion")
	private String estadoConciliacion;
	
	@JsonProperty("conciliable")
	private String conciliable;
	
	@JsonProperty("valorTotal")
	private Double valorTotal;
	
	@JsonProperty("valorFaltante")
	private Double valorFaltante;
	
	@JsonProperty("valorSobrante")
	private Double valorSobrante;
	
	@JsonProperty("fallidaOficina")
	private String fallidaOficina;
	
	@JsonProperty("usuarioCreacion")
	private String usuarioCreacion;
	
	@JsonProperty("usuarioModificacion")
	private String usuarioModificacion;
	
	@JsonProperty("fechaCreacion")
	private Date fechaCreacion;
	
	@JsonProperty("fechaModificacion")
	private Date fechaModificacion;
	
	private String tdv;

	private String bancoAVAL;
	
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasNombresDTO
	 * @author cesar.castano
	 */
	public static final Function<CertificadasNoConciliadasDTO, CertificadasNoConciliadasNombresDTO> CONVERTER_NO_PROGRAMADAS_NOMBRES = (CertificadasNoConciliadasDTO t) -> {

		var operacionesCertificadasNombresDto = new CertificadasNoConciliadasNombresDTO();
		UtilsObjects.copiarPropiedades(t, operacionesCertificadasNombresDto);		

		return operacionesCertificadasNombresDto;
	};
}
