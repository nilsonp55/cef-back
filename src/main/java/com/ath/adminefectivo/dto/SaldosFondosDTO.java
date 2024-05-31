package com.ath.adminefectivo.dto;


import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.SaldosFondos;
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
public class SaldosFondosDTO {



	private int idSaldosFondos;

	private int codigoFondo;

	private String transportadora;

	private int bancoAval;

	private String codigoCiudad;

	private Date fecha;

	private long saldoInicialMonedas;

	private long saldoFinalMonedas;

	private long saldoInicialRem;

	private long saldoFinalRem;

	private long saldoInicialBestd;

	private long saldoFinalBestd;

	private long saldoInicialCrlt;

	private long saldoFinalCrlt;

	private long saldoInicialDtro;

	private long saldoFinalDtro;
	
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<SaldosFondosDTO, SaldosFondos> CONVERTER_ENTITY = (SaldosFondosDTO t) -> {
		var saldosFondos = new SaldosFondos();
		return saldosFondos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<SaldosFondos, SaldosFondosDTO> CONVERTER_DTO = (SaldosFondos t) -> {
		var saldosFondosDTO = new SaldosFondosDTO();
		UtilsObjects.copiarPropiedades(t, saldosFondosDTO);

		return saldosFondosDTO;
	};
}
