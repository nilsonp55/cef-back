package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.DetallesProcesoAutomatico;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de DetallesProcesoAutomaticoDTO
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetallesProcesoAutomaticoDTO {

	private Long idDetalleProceso;
	
	private long idArchivo;
	
	private String nombreArchivo;
	
	private String resultado;
	
	private String mensajeError;

	private BitacoraAutomaticosDTO bitacoraAutomaticosDTO;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<DetallesProcesoAutomaticoDTO, DetallesProcesoAutomatico> CONVERTER_ENTITY = (DetallesProcesoAutomaticoDTO t) -> {
		var detallesProcesoAutomatico = new DetallesProcesoAutomatico();
		UtilsObjects.copiarPropiedades(t, detallesProcesoAutomatico);
		return detallesProcesoAutomatico;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<DetallesProcesoAutomatico, DetallesProcesoAutomaticoDTO> CONVERTER_DTO = (DetallesProcesoAutomatico t) -> {
		var detallesProcesoAutomaticoDTO = new DetallesProcesoAutomaticoDTO();
		UtilsObjects.copiarPropiedades(t, detallesProcesoAutomaticoDTO);
		return detallesProcesoAutomaticoDTO;
	};
}
