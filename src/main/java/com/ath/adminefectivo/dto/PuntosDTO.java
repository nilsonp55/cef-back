package com.ath.adminefectivo.dto;

import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.Puntos;
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
	
	private OficinasDTO oficinas;

	private SitiosClientesDTO sitiosClientes;

	private PuntosCodigoTdvDTO puntosCodigoTDV;

	private FondosDTO fondos;

	private CajerosATMDTO cajeroATM;

	private BancosDTO bancos;
	
	private String estado;

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Puntos, PuntosDTO> CONVERTER_DTO = (Puntos t) -> {
		var puntosDTO = new PuntosDTO();
		UtilsObjects.copiarPropiedades(t, puntosDTO);
		
		if(!Objects.isNull(t.getOficinas())) {
			puntosDTO.setOficinas(OficinasDTO.CONVERTER_DTO.apply(t.getOficinas()));
		}

		if(!Objects.isNull(t.getSitiosClientes())) {
			puntosDTO.setSitiosClientes(SitiosClientesDTO.CONVERTER_DTO.apply(t.getSitiosClientes()));
		}
			
		if(!Objects.isNull(t.getFondos())) {
			puntosDTO.setFondos(FondosDTO.CONVERTER_DTO.apply(t.getFondos()));
		}
		
		if(!Objects.isNull(t.getCajeroATM())) {
				puntosDTO.setCajeroATM(CajerosATMDTO.CONVERTER_DTO.apply(t.getCajeroATM()));
		}
		
		if(!Objects.isNull(t.getBancos())) {
			puntosDTO.setBancos(BancosDTO.CONVERTER_DTO.apply(t.getBancos()));
		}

		return puntosDTO;
	};
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<PuntosDTO, Puntos> CONVERTER_ENTITY = (PuntosDTO t) -> {
		var puntos = new Puntos();
		UtilsObjects.copiarPropiedades(t, puntos);
		
		if(!Objects.isNull(t.getOficinas())) {
			puntos.setOficinas(OficinasDTO.CONVERTER_ENTITY.apply(t.getOficinas()));
		}

		if(!Objects.isNull(t.getSitiosClientes())) {
			puntos.setSitiosClientes(SitiosClientesDTO.CONVERTER_ENTITY.apply(t.getSitiosClientes()));
		}
			
		if(!Objects.isNull(t.getFondos())) {
			puntos.setFondos(FondosDTO.CONVERTER_ENTITY.apply(t.getFondos()));
		}
		
		if(!Objects.isNull(t.getCajeroATM())) {
			puntos.setCajeroATM(CajerosATMDTO.CONVERTER_ENTITY.apply(t.getCajeroATM()));
		}
		
		if(!Objects.isNull(t.getBancos())) {
			puntos.setBancos(BancosDTO.CONVERTER_ENTITY.apply(t.getBancos()));
		}
		
		
		return puntos;
	};
}
