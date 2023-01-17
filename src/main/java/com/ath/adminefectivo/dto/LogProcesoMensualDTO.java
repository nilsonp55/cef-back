package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.LogProcesoMensual;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la estructura de la entidad LogProcesoMensual
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogProcesoMensualDTO {

	private Long idLog;

	private String codigoProceso;

	private String estadoProceso;
	
	private Date mes;

	private Date fechaCierre;

	private Date fechaCreacion;
	
	private Date fechaModificacion;

	private String usuarioCreacion;

	private String usuarioModificacion;
		
	private String estado;
	

	/**
	 * Funcion de conversion de DTO a Entidad
	 * 
	 * @author duvan.naranjo
	 */
	public static final Function<LogProcesoMensualDTO, LogProcesoMensual> CONVERTER_ENTITY = (LogProcesoMensualDTO t) -> {
		var logProcesoMensual = new LogProcesoMensual();
		UtilsObjects.copiarPropiedades(t, logProcesoMensual);
		return logProcesoMensual;
	};

	/**
	 * Funcion de conversion de Entidad a DTO
	 * 
	 * @author duvan.naranjo
	 */
	public static final Function<LogProcesoMensual, LogProcesoMensualDTO> CONVERTER_DTO = (LogProcesoMensual t) -> {
		var logProcesoMensualDTO = new LogProcesoMensualDTO();
		UtilsObjects.copiarPropiedades(t, logProcesoMensualDTO);
		return logProcesoMensualDTO;
	};

}
