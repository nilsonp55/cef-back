package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.ath.adminefectivo.entities.ConciliacionServicios;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que contiene la estructura del DetalleOperacionesDTO
 * @author duvan.naranjo
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleOperacionesDTO {

	private Integer idOperacion;
	
	private String denominacion;
	
	private Integer calidad;
	
	private String familia;
	
	private Double valorDetalle;
	
	private String usuarioModificacion;
	
	private String usuarioCreacion;
	
	private Date fechaCreacion;
	
	private Date fechaModificacion;


	/**
	 * Funcion que convierte el archivo DTO ArchivosCargadosDto a Entity ArchivosCargados
	 * @author cesar.castano
	 */
	public static final Function<DetalleOperacionesDTO, DetalleOperacionesProgramadas> CONVERTER_ENTITY = (DetalleOperacionesDTO t) -> {

		DetalleOperacionesProgramadas detalle = new DetalleOperacionesProgramadas();
		UtilsObjects.copiarPropiedades(t, detalle);		

		return detalle;
	};
}
