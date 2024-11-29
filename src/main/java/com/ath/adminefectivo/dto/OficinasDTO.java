package com.ath.adminefectivo.dto;

import java.util.function.Function;
import org.springframework.lang.Nullable;

import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Oficinas
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OficinasDTO {

	private Integer codigoPunto;
	
	private Integer codigoOficina;
	
	private Integer bancoAVAL;
	
	private Boolean fajado;
	
	private Boolean refajillado;
	
	private Double tarifaRuteo;
	
	private Double tarifaVerificacion;
	
	private String nombreOficina;
	
	@Nullable
    private Boolean programaTransporte;
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Oficinas, OficinasDTO> CONVERTER_DTO = (Oficinas t) -> {
		var oficinasDTO = new OficinasDTO();
		UtilsObjects.copiarPropiedades(t, oficinasDTO);
		return oficinasDTO;
	};
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<OficinasDTO, Oficinas> CONVERTER_ENTITY = (OficinasDTO t) -> {
		var oficinas = new Oficinas();
		UtilsObjects.copiarPropiedades(t, oficinas);
		return oficinas;
	};
}
