package com.ath.adminefectivo.dto;

import java.util.function.Function;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	
	@NotNull
	private Integer codigoBancoAval;
	
	@NotEmpty
	private String nombreCliente;
	
	@NotEmpty
	private String tipoId;
	
	@NotEmpty
	private String identificacion;
	
	@NotNull
	private Double tarifaSeparacion;
	
	@NotNull
	private Boolean amparado;
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ClientesCorporativos, ClientesCorporativosDTO> CONVERTER_DTO = (ClientesCorporativos t) -> {
		var clientesCorporativosDTO = new ClientesCorporativosDTO();
		UtilsObjects.copiarPropiedades(t, clientesCorporativosDTO);
		return clientesCorporativosDTO;
	};
	
	/**
	 * Convierte una instancia DTO en una isntancia Entity para ClientesCorporativos
	 * @author prv_nparra
	 */
	public static final Function<ClientesCorporativosDTO, ClientesCorporativos> CONVERTER_ENTITY = (ClientesCorporativosDTO t) -> {
		var clienteCorporativo = new ClientesCorporativos();
		UtilsObjects.copiarPropiedades(t, clienteCorporativo);
		return clienteCorporativo;
	};
}
