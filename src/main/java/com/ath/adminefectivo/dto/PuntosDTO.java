package com.ath.adminefectivo.dto;

import java.util.List;
import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la tabla de Puntos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntosDTO {

	private Integer codigoPunto;
	
	private String tipoPunto;
	
	private String nombrePunto;
	
	private String codigoCiudad;
	
	private List<Oficinas> oficinas;

	private List<SitiosClientes> sitiosClientes;

	private List<PuntosCodigoTDV> puntosCodigoTDV;

	private List<Fondos> fondos;

	private List<CajerosATM> cajeroATM;

	private List<Bancos> bancos;

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Puntos, PuntosDTO> CONVERTER_DTO = (Puntos t) -> {
		var puntosDTO = new PuntosDTO();
		UtilsObjects.copiarPropiedades(t, puntosDTO);
		return puntosDTO;
	};
}
