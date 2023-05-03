package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo de Operaciones Certificadas
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionesCertificadasDTO {

	private Integer codigoFondoTDV;
	
	private Integer codigoPuntoOrigen;
	
	private Integer codigoPuntoDestino;
	
	private Date fechaEjecucion;
	
	private String tipoOperacion;
	
	private String tipoServicio;
	
	private String estadoConciliacion;
	
	private String conciliable;
	
	private Double valorTotal;
	
	private Double valorFaltante;
	
	private Double valorSobrante;
	
	private String fallidaOficina;
	
	private String usuarioCreacion;
	
	private String usuarioModificacion;
	
	private Date fechaCreacion;
	
	private Date fechaModificacion;
	
	private String codigoServicioTdv;
	
	private String entradaSalida;
	
	private Long idArchivoCargado;
	
	private String tdv;

	private String bancoAVAL;
	
	private String tipoPuntoOrigen;
	
	private String tipoPuntoDestino;
	
	private String codigoPropioTDV;
	
	/**
	 * Funcion que convierte el archivo DTO OperacionesCertificadasDTO a Entity OperacionesCertificadas
	 * @author cesar.castano
	 */
	public static final Function<OperacionesCertificadasDTO, OperacionesCertificadas> CONVERTER_ENTITY = (OperacionesCertificadasDTO t) -> {

		var operacionesCertificadas = new OperacionesCertificadas();
		UtilsObjects.copiarPropiedades(t, operacionesCertificadas);		

		return operacionesCertificadas;
	};
	
	/**
	 * Funcion que convierte la entity OperacionesCertificadas a archivo DTO OperacionesCertificadasDTO
	 * @author cesar.castano
	 */
	public static final Function<OperacionesCertificadas, OperacionesCertificadasDTO> CONVERTER_DTO = (OperacionesCertificadas t) -> {

		var operacionesCertificadasDTO = new OperacionesCertificadasDTO();
		UtilsObjects.copiarPropiedades(t, operacionesCertificadasDTO);		

		return operacionesCertificadasDTO;
	};
}
