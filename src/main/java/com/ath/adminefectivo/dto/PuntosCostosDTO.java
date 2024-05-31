package com.ath.adminefectivo.dto;


import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.PuntosCostos;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de TarifasOperacion
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntosCostosDTO {


	private int idPuntosCostos;

	private PuntosDTO puntoDTO;

	private String costoPersonalizado;

	private int costoFijo;

	private int costoMilajeRuteo;

	private int costoMoneda;

	private boolean fajado;

	private long idTarifasGenerales;

	private Boolean mixto;
	
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<PuntosCostosDTO, PuntosCostos> CONVERTER_ENTITY = (PuntosCostosDTO t) -> {
		var puntosCostos = new PuntosCostos();
		UtilsObjects.copiarPropiedades(t, puntosCostos);
		if(!Objects.isNull(t.getPuntoDTO())) {
			puntosCostos.setPunto(PuntosDTO.CONVERTER_ENTITY.apply(t.getPuntoDTO()));
		}
		return puntosCostos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<PuntosCostos, PuntosCostosDTO> CONVERTER_DTO = (PuntosCostos t) -> {
		var puntosCostosDTO = new PuntosCostosDTO();
		UtilsObjects.copiarPropiedades(t, puntosCostosDTO);
		if(!Objects.isNull(t.getPunto())) {
			puntosCostosDTO.setPuntoDTO(PuntosDTO.CONVERTER_DTO.apply(t.getPunto()));
		}

		return puntosCostosDTO;
	};
}
