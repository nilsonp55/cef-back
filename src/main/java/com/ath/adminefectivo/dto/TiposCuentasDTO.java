package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.TiposCuentas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de TiposCuentas
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TiposCuentasDTO {

private String tipoCuenta;
	
	private String cuentaAuxiliar;
	
	private String tipoId;
	
	private String identificador;
	
	private String descripcion;
	
	private String referencia1;
	
	private String referencia2;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TiposCuentasDTO, TiposCuentas> CONVERTER_ENTITY = (TiposCuentasDTO t) -> {
		TiposCuentas tiposCuentas = new TiposCuentas();
		UtilsObjects.copiarPropiedades(t, tiposCuentas);
		return tiposCuentas;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TiposCuentas, TiposCuentasDTO> CONVERTER_DTO = (TiposCuentas t) -> {
		TiposCuentasDTO tiposCuentasDTO = new TiposCuentasDTO();
		UtilsObjects.copiarPropiedades(t, tiposCuentasDTO);
		return tiposCuentasDTO;
	};
}
