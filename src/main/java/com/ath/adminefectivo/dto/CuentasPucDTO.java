package com.ath.adminefectivo.dto;

import java.util.function.Function;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CuentasPuc;
import com.ath.adminefectivo.entities.TiposCuentas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad CuentasPucDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuentasPucDTO {

private Long idCuentasPuc;
	
	private String cuentaContable;
	
	private Bancos bancoAval;
	
	private String nombreCuenta;
	
	private String identificador;
	
	private CuentasPuc tiposCentrosCostos;
	
	private TiposCuentas tiposCuentas;
	
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CuentasPucDTO, CuentasPuc> CONVERTER_ENTITY = (CuentasPucDTO t) -> {
		CuentasPuc cuentasPuc = new CuentasPuc();
		UtilsObjects.copiarPropiedades(t, cuentasPuc);
		return cuentasPuc;
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<CuentasPuc, CuentasPucDTO> CONVERTER_DTO = (CuentasPuc t) -> {
		CuentasPucDTO cuentasPucDTO = new CuentasPucDTO();
		UtilsObjects.copiarPropiedades(t, cuentasPucDTO);
		return cuentasPucDTO;
	};
	
}
