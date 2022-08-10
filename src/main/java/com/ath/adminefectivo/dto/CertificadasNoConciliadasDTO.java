package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.OperacionesCertificadas;
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
public class CertificadasNoConciliadasDTO {

	@JsonProperty("idCertificacion")
	private Integer idCertificacion;
	
	@JsonProperty("codigoFondoTDV")
	private Integer codigoFondoTDV;
	
	@JsonProperty("codigoBanco")
	private Integer codigoBanco;
	
	@JsonProperty("codigoCiudad")
	private Integer codigoCiudad;
	
	@JsonProperty("codigoPuntoOrigen")
	private Integer codigoPuntoOrigen;
	
	@JsonProperty("codigoPuntoDestino")
	private Integer codigoPuntoDestino;
	
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

	private Integer bancoAVAL;
	
	/**
	 * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity OperacionesProgramadas
	 * @author cesar.castano
	 */
	public static final Function<CertificadasNoConciliadasDTO, OperacionesCertificadas> CONVERTER_ENTITY = (CertificadasNoConciliadasDTO t) -> {

		var operacionesCertificadas = new OperacionesCertificadas();
		UtilsObjects.copiarPropiedades(t, operacionesCertificadas);		

		return operacionesCertificadas;
	};
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasDTO
	 * @author cesar.castano
	 */
	public static final Function<OperacionesCertificadas, CertificadasNoConciliadasDTO> CONVERTER_DTO = (OperacionesCertificadas t) -> {

		var certificadasNoConciliadasDTO = new CertificadasNoConciliadasDTO();
		UtilsObjects.copiarPropiedades(t, certificadasNoConciliadasDTO);		

		return certificadasNoConciliadasDTO;
	};
}
