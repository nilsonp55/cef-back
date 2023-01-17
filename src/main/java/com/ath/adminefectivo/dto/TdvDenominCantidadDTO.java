package com.ath.adminefectivo.dto;

import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.TdvDenominCantidad;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de TdvDenominCantidad
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TdvDenominCantidadDTO {


	private Integer idTdvDenCant;

	private TransportadorasDTO transportadoraDTO; 

	private String moneda;

	private int denominacion;

	private String familia;

	private int cantidad_por_denom;

	private int estado;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TdvDenominCantidadDTO, TdvDenominCantidad> CONVERTER_ENTITY = (TdvDenominCantidadDTO t) -> {
		var tdvDenominCantidad = new TdvDenominCantidad();
		UtilsObjects.copiarPropiedades(t, tdvDenominCantidad);
		if(!Objects.isNull(t.getTransportadoraDTO())) {
			tdvDenominCantidad.setTransportadora(TransportadorasDTO.CONVERTER_ENTITY.apply(t.getTransportadoraDTO()));
		}
		return tdvDenominCantidad;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TdvDenominCantidad, TdvDenominCantidadDTO> CONVERTER_DTO = (TdvDenominCantidad t) -> {
		var tdvDenominCantidadDTO = new TdvDenominCantidadDTO();
		UtilsObjects.copiarPropiedades(t, tdvDenominCantidadDTO);

		if(!Objects.isNull(t.getTransportadora())) {
			tdvDenominCantidadDTO.setTransportadoraDTO(TransportadorasDTO.CONVERTER_DTO.apply(t.getTransportadora()));
		}
		return tdvDenominCantidadDTO;
	};
}
