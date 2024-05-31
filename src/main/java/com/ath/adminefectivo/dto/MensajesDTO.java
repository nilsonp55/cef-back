package com.ath.adminefectivo.dto;

import java.util.function.Function;

import javax.validation.constraints.NotNull;

import com.ath.adminefectivo.entities.Mensajes;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del Mensajes
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajesDTO {

	@NotNull(message = "(General.idMensaje)" )
	private Integer idMensaje;
	
	@NotNull(message = "(General.mensaje)" )
	private String mensaje;
	
	/**
	 * Funcion que convierte el archivo DTO MensajesDTO a Entity
	 * Mensajes
	 * 
	 * @author duvan.naranjo
	 */
	public static final Function<MensajesDTO, Mensajes> CONVERTER_ENTITY = (
			MensajesDTO t) -> {
		Mensajes mensajes = new Mensajes();
		UtilsObjects.copiarPropiedades(t, mensajes);		
		return mensajes;
	};
	
	/**
	 * Funcion que convierte el archivo Mensajes a MensajesDTO
	 * @author duvan.naranjo
	 */
	public static final Function<Mensajes, MensajesDTO> CONVERTER_DTO = (
			Mensajes t) -> {
		MensajesDTO mensajesDTO = new MensajesDTO();
		UtilsObjects.copiarPropiedades(t, mensajesDTO);		
		return mensajesDTO;
	};
}
