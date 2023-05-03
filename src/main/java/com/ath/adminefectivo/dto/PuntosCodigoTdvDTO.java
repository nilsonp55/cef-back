package com.ath.adminefectivo.dto;

import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO  de la tabla PuntosCodigoTDV
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntosCodigoTdvDTO {

	private Integer idPuntoCodigoTdv;
	
	private String codigoTDV;
	
	private Integer codigoPunto;
	
	private String codigoPropioTDV;
	
	private PuntosDTO puntosDTO;
	
	private BancosDTO bancosDTO;
	
	private String ciudadFondo;
		
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<PuntosCodigoTdvDTO, PuntosCodigoTDV> CONVERTER_ENTITY = (PuntosCodigoTdvDTO t) -> {
		var puntosCodigoTDV = new PuntosCodigoTDV();
		UtilsObjects.copiarPropiedades(t, puntosCodigoTDV);
		if(!Objects.isNull(t.getPuntosDTO())) {
			puntosCodigoTDV.setPuntos(PuntosDTO.CONVERTER_ENTITY.apply(t.getPuntosDTO()));
		}
		if(!Objects.isNull(t.getBancosDTO())) {
			puntosCodigoTDV.setBancos(BancosDTO.CONVERTER_ENTITY.apply(t.getBancosDTO()));
		}
		return puntosCodigoTDV;
	};
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<PuntosCodigoTDV, PuntosCodigoTdvDTO> CONVERTER_DTO = (PuntosCodigoTDV t) -> {
		var puntosCodigoTdvDTO = new PuntosCodigoTdvDTO();
		UtilsObjects.copiarPropiedades(t, puntosCodigoTdvDTO);
		if(!Objects.isNull(t.getPuntos())){
			puntosCodigoTdvDTO.setPuntosDTO(PuntosDTO.CONVERTER_DTO.apply(t.getPuntos()));
		}
		if(!Objects.isNull(t.getBancos())) {
			puntosCodigoTdvDTO.setBancosDTO(BancosDTO.CONVERTER_DTO.apply(t.getBancos()));
		}
		return puntosCodigoTdvDTO;
	};
}
