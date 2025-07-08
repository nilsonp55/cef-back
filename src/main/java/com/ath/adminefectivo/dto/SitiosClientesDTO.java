package com.ath.adminefectivo.dto;

import java.util.function.Function;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.utils.UtilsObjects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la tabla de sitios clientes
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SitiosClientesDTO {

	private Integer codigoPunto;
	
	private Integer codigoCliente;
	
	private String identificadorCliente;
	
	private Boolean fajado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<SitiosClientesDTO, SitiosClientes> CONVERTER_ENTITY = (SitiosClientesDTO t) -> {
		var sitiosClientes = new SitiosClientes();
		UtilsObjects.copiarPropiedades(t, sitiosClientes);
		return sitiosClientes;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<SitiosClientes, SitiosClientesDTO> CONVERTER_DTO = (SitiosClientes t) -> {
		var sitiosClientesDTO = new SitiosClientesDTO();
		UtilsObjects.copiarPropiedades(t, sitiosClientesDTO);
		return sitiosClientesDTO;
	};
}
