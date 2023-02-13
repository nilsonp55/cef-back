package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.AuditoriaProcesos;
import com.ath.adminefectivo.entities.id.AuditoriaProcesosPK;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de AuditoriaProcesos
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaProcesosDTO {
	
	private AuditoriaProcesosPK id;
	
	private String estadoProceso;
	
	private String mensaje;
		
	private String usuarioCreacion;
	
	private Date fechaCreacion;

	private String usuarioModificacion;
	
	private Date fechaModificacion;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<AuditoriaProcesosDTO, AuditoriaProcesos> CONVERTER_ENTITY = (AuditoriaProcesosDTO t) -> {
		var auditoriaProcesos = new AuditoriaProcesos();
		UtilsObjects.copiarPropiedades(t, auditoriaProcesos);
		return auditoriaProcesos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<AuditoriaProcesos, AuditoriaProcesosDTO> CONVERTER_DTO = (AuditoriaProcesos t) -> {
		var auditoriaProcesosDTO = new AuditoriaProcesosDTO();
		UtilsObjects.copiarPropiedades(t, auditoriaProcesosDTO);
		return auditoriaProcesosDTO;
	};
}
