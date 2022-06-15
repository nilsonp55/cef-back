package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de clientes corporativos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientesCorporativosDTO {

	private Integer codigoCliente;
	
	private Integer codigoBancoAval;
	
	private String nombreCliente;
	
	private String tipoId;
	
	private String identificacion;
	
	private Double tarifaSeparacion;
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ClientesCorporativos, ClientesCorporativosDTO> CONVERTER_DTO = (ClientesCorporativos t) -> {
		var clientesCorporativosDTO = new ClientesCorporativosDTO();
		UtilsObjects.copiarPropiedades(t, clientesCorporativosDTO);
		return clientesCorporativosDTO;
	};
}
