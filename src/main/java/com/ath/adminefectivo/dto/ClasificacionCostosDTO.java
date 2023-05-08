package com.ath.adminefectivo.dto;


import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.ClasificacionCostos;
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
public class ClasificacionCostosDTO {


	private int idCostosClasificacion;
	
	private int bancoAval;
	
	private String transportadora;
	
	private String mesAnio;
	
	private Date fechaModificacion;
	
	private String usuarioModificacion;
	
	private int fajosEstimados;
	
	private int bolsasEstimadas;
	
	private int cantidadFajos;
	
	private int cantidadRem;
	
	private int cantidadMonedas;
	
	private int valorClasifFajos;
	
	private int valorClasifRem;
	
	private int valorClasifMonedas;
		
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ClasificacionCostosDTO, ClasificacionCostos> CONVERTER_ENTITY = (ClasificacionCostosDTO t) -> {
		var costosClasificacion = new ClasificacionCostos();
		UtilsObjects.copiarPropiedades(t, costosClasificacion);
		return costosClasificacion;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ClasificacionCostos, ClasificacionCostosDTO> CONVERTER_DTO = (ClasificacionCostos t) -> {
		var costosClasificacionDTO = new ClasificacionCostosDTO();
		UtilsObjects.copiarPropiedades(t, costosClasificacionDTO);
		return costosClasificacionDTO;
	};
}
