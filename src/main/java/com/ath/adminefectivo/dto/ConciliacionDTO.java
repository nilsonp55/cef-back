package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la tabla de Conciliacion Servicios
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConciliacionDTO {

	private Integer idConciliacion;
	
	private Integer idOperacion;
	
	private Integer idCertificacion;
	
	private Date fechaConciliacion;
	
	private String tipoConciliacion;
	
	private String usuarioModificacion;
	
	private Date fechaModificacion;
	
	private String usuarioCreacion;

	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ConciliacionDTO, ConciliacionServicios> CONVERTER_ENTITY = (ConciliacionDTO t) -> {
		var conciliacionServicios = new ConciliacionServicios();
		UtilsObjects.copiarPropiedades(t, conciliacionServicios);
		return conciliacionServicios;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ConciliacionServicios, ConciliacionDTO> CONVERTER_DTO = (ConciliacionServicios t) -> {
		var conciliacionDTO = new ConciliacionDTO();
		UtilsObjects.copiarPropiedades(t, conciliacionDTO);
		return conciliacionDTO;
	};
}
