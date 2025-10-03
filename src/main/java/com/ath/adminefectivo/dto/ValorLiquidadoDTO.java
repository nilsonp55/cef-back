package com.ath.adminefectivo.dto;


import java.util.function.Function;

import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Escalas
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValorLiquidadoDTO {

private Long idValoresLiq;
	
	private Double clasificacionFajado;
	
	private Double clasificacionNoFajado;
	
	private Double costoCharter;
	
	private Double costoEmisario;
	
	private Double costoFijoParada;
	
	private Double costoMoneda;
	
	private Double costoPaqueteo;
	
	private Long idLiquidacion;
	
	private Double milajePorRuteo;
	
	private Double milajeVerificacion;
	
	private Double modenaResiduo;
	
	private Double tasaAeroportuaria;
	
	private Integer idSeqGrupo;

	private String observacionLiquidacion;

	private ParametrosLiquidacionCostoDTO parametrosLiquidacionCostoDTO;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ValorLiquidadoDTO, ValoresLiquidados> CONVERTER_ENTITY = (ValorLiquidadoDTO t) -> {
		var valoresLiquidados = new ValoresLiquidados();
		UtilsObjects.copiarPropiedades(t, valoresLiquidados);
		return valoresLiquidados;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ValoresLiquidados, ValorLiquidadoDTO> CONVERTER_DTO = (ValoresLiquidados t) -> {
		var valorLiquidadoDTO = new ValorLiquidadoDTO();
		UtilsObjects.copiarPropiedades(t, valorLiquidadoDTO);
		return valorLiquidadoDTO;
	};
}
