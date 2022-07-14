package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del DetalleOperacionesProgramadasDTO
 * @author duvan.naranjo
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleOperacionesProgramadasDTO {
	
	private Integer idDetalleOperacion;

	private OperacionesProgramadas operaciones;
	
	private String denominacion;
	
	private Integer calidad;
	
	private String familia;
	
	private Double valorDetalle;
	
	private String usuarioModificacion;
	
	private String usuarioCreacion;
	
	private Date fechaCreacion;
	
	private Date fechaModificacion;
	
	/**
	 * Funcion que convierte el archivo DTO ProgramadasNoConciliadasDTO a Entity OperacionesProgramadas
	 * @author cesar.castano
	 */
	public static final Function<DetalleOperacionesProgramadasDTO, DetalleOperacionesProgramadas> CONVERTER_ENTITY = (DetalleOperacionesProgramadasDTO t) -> {

		var detalleOperacionesProgramadas = new DetalleOperacionesProgramadas();
		detalleOperacionesProgramadas.setCalidad(t.getCalidad());
		detalleOperacionesProgramadas.setDenominacion(t.getDenominacion());
		detalleOperacionesProgramadas.setFamilia(t.getFamilia());
		detalleOperacionesProgramadas.setFechaCreacion(t.getFechaCreacion());
		detalleOperacionesProgramadas.setFechaModificacion(t.getFechaModificacion());
		detalleOperacionesProgramadas.setIdDetalleOperacion(t.getIdDetalleOperacion());
		detalleOperacionesProgramadas.setOperacionesProgramadas(t.getOperaciones());
		detalleOperacionesProgramadas.setUsuarioCreacion(t.getUsuarioCreacion());
		detalleOperacionesProgramadas.setUsuarioModificacion(t.getUsuarioModificacion());
		detalleOperacionesProgramadas.setValorDetalle(t.getValorDetalle());
		return detalleOperacionesProgramadas;
	};
	
	/**
	 * Funcion que convierte la entity OperacionesProgramadas a archivo DTO ProgramadasNoConciliadasDTO
	 * @author cesar.castano
	 */
	public static final Function<DetalleOperacionesProgramadas, DetalleOperacionesProgramadasDTO> CONVERTER_DTO = (DetalleOperacionesProgramadas t) -> {

		var detalleOperacionesProgramadasDTO = new DetalleOperacionesProgramadasDTO();
		UtilsObjects.copiarPropiedades(t, detalleOperacionesProgramadasDTO);		

		return detalleOperacionesProgramadasDTO;
	};
}
