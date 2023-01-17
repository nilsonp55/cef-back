package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la estructura de la entidad LogProcesoDiario
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogProcesoDiarioDTO {

	private Long idLogProceso;

	private String codigoProceso;

	private String estadoProceso;

	private Date fechaFinalizacion;

	private String estado;

	private String usuarioCreacion;

	private Date fechaCreacion;

	private String usuarioModificacion;

	private Date fechaModificacion;
	

	/**
	 * Funcion de conversion de DTO a Entidad
	 * 
	 * @author CamiloBenavides
	 */
	public static final Function<LogProcesoDiarioDTO, LogProcesoDiario> CONVERTER_ENTITY = (LogProcesoDiarioDTO t) -> {
		var logProcesoDiario = new LogProcesoDiario();
		UtilsObjects.copiarPropiedades(t, logProcesoDiario);
		return logProcesoDiario;
	};

	/**
	 * Funcion de conversion de Entidad a DTO
	 * 
	 * @author CamiloBenavides
	 */
	public static final Function<LogProcesoDiario, LogProcesoDiarioDTO> CONVERTER_DTO = (LogProcesoDiario t) -> {
		var logProcesoDiarioDTO = new LogProcesoDiarioDTO();
		UtilsObjects.copiarPropiedades(t, logProcesoDiarioDTO);
		return logProcesoDiarioDTO;
	};

}
