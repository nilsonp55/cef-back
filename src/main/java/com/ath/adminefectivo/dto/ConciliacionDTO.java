package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
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
	
	private OperacionesProgramadas operaciones;
	
	private OperacionesCertificadas certificaciones;
	
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
		conciliacionServicios.setFechaConciliacion(t.getFechaConciliacion());
		conciliacionServicios.setFechaModificacion(t.getFechaModificacion());
		conciliacionServicios.setIdConciliacion(t.getIdConciliacion());
		conciliacionServicios.setOperacionesCertificadas(t.getCertificaciones());
		conciliacionServicios.setOperacionesProgramadas(t.getOperaciones());
		conciliacionServicios.setTipoConciliacion(t.getTipoConciliacion());
		conciliacionServicios.setUsuarioCreacion(t.getUsuarioCreacion());
		conciliacionServicios.setUsuarioModificacion(t.getUsuarioModificacion());
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
