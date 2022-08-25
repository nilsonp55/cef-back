package com.ath.adminefectivo.dto;

import java.util.function.Function;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.ConfContableEntidades;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad ConfContableEntidadesDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfContableEntidadesDTO {

	private Long consecutivo;
	
	private Bancos bancoAval;
	
	private Integer tipoTransaccion;
	
	private String tipoOperacion;
	
	private Integer codigoComision;
	
	private Integer tipoImpuesto;
	
	private String medioPago;
	
	private Puntos codigoPuntoBancoExt;
	
	private Transportadoras transportadora;
	
	private Boolean esCambio;

	private String naturaleza;
	
	private String cuentaContable;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ConfContableEntidadesDTO, ConfContableEntidades> CONVERTER_ENTITY = (ConfContableEntidadesDTO t) -> {
		ConfContableEntidades confContableEntidades = new ConfContableEntidades();
		UtilsObjects.copiarPropiedades(t, confContableEntidades);
		return confContableEntidades;
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ConfContableEntidades, ConfContableEntidadesDTO> CONVERTER_DTO = (ConfContableEntidades t) -> {
		ConfContableEntidadesDTO confContableEntidadesDTO = new ConfContableEntidadesDTO();
		UtilsObjects.copiarPropiedades(t, confContableEntidadesDTO);
		return confContableEntidadesDTO;
	};
	
}
