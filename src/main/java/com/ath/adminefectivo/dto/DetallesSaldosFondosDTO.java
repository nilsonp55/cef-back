package com.ath.adminefectivo.dto;


import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.DetallesSaldosFondos;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Costos Clasificacion
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetallesSaldosFondosDTO {



	private int idDetallesSaldosFondos;

	private SaldosFondosDTO saldosFondosDTO;

	private String calidad;

	private long denominacion;

	private String tipoMoneda;

	private String familia;

	private String tipoClasifica;

	private Double valor;
	
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<DetallesSaldosFondosDTO, DetallesSaldosFondos> CONVERTER_ENTITY = (DetallesSaldosFondosDTO t) -> {
		var detallesSaldosFondos = new DetallesSaldosFondos();
		UtilsObjects.copiarPropiedades(t, detallesSaldosFondos);
		if(!Objects.isNull(t.getSaldosFondosDTO())) {
			detallesSaldosFondos.setSaldosFondos(SaldosFondosDTO.CONVERTER_ENTITY.apply(t.getSaldosFondosDTO()));
		}

		return detallesSaldosFondos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<DetallesSaldosFondos, DetallesSaldosFondosDTO> CONVERTER_DTO = (DetallesSaldosFondos t) -> {
		var detallesSaldosFondosDTO = new DetallesSaldosFondosDTO();
		UtilsObjects.copiarPropiedades(t, detallesSaldosFondosDTO);
		if(!Objects.isNull(t.getSaldosFondos())) {
			detallesSaldosFondosDTO.setSaldosFondosDTO(SaldosFondosDTO.CONVERTER_DTO.apply(t.getSaldosFondos()));
		}

		return detallesSaldosFondosDTO;
	};
}
