package com.ath.adminefectivo.dto;


import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.TarifasOperacion;
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
public class TarifasOperacionDTO {


	private int idTarifasOperacion;

	private int codigoBanco;

	private String codigoTdv; 

	private String tipoPunto; 

	private String tipoOperacion;

	private String tipoServicio;

	private String escala;

	private String billetes; 

	private String monedas; 

	private String fajado;

	private String comisionAplicar;

	private int valorTarifa; 

	private int estado;
	
	private String usuarioCreacion;
	
	private Date fechaCreacion;

	private String usuarioModificacion;
	
	private Date fechaModificacion;

	private Date fechaVigenciaIni;

	private Date fechaVigenciaFin;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TarifasOperacionDTO, TarifasOperacion> CONVERTER_ENTITY = (TarifasOperacionDTO t) -> {
		var tarifasOperacion = new TarifasOperacion();
		UtilsObjects.copiarPropiedades(t, tarifasOperacion);
				
		return tarifasOperacion;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TarifasOperacion, TarifasOperacionDTO> CONVERTER_DTO = (TarifasOperacion t) -> {
		var tarifasOperacionDTO = new TarifasOperacionDTO();
		UtilsObjects.copiarPropiedades(t, tarifasOperacionDTO);
		
		return tarifasOperacionDTO;
	};
}
