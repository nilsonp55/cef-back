package com.ath.adminefectivo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.entities.ParametrosFuncionesDinamicas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de FuncionesDinamicas
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FuncionesDinamicasDTO {

	private Integer idFuncion;
	
	private String nombreFuncion;
	
	private String descripcionFuncion;	
	
	private String scriptFuncion;
	
	private int cantidadParametros;
	
	private int estado;
	
	private String usuarioCreacion;
	
	private Date fechaCreacion;

	private String usuarioModificacion;
	
	private Date fechaModificacion;
	
	private List<ParametrosFuncionesDinamicasDTO> parametrosFuncionesDinamicasDTO;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<FuncionesDinamicasDTO, FuncionesDinamicas> CONVERTER_ENTITY = (FuncionesDinamicasDTO t) -> {
		var funcionesDinamicas = new FuncionesDinamicas();
		UtilsObjects.copiarPropiedades(t, funcionesDinamicas);
		List<ParametrosFuncionesDinamicas> parametrosFuncionesDinamicas = new ArrayList();
		if(!t.getParametrosFuncionesDinamicasDTO().isEmpty()) {
			t.getParametrosFuncionesDinamicasDTO().forEach(parametroFuncion ->{
				parametrosFuncionesDinamicas.add(ParametrosFuncionesDinamicasDTO.CONVERTER_ENTITY.apply(parametroFuncion));
			});
		}
		funcionesDinamicas.setParametrosFuncionesDinamicas(parametrosFuncionesDinamicas);
		return funcionesDinamicas;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<FuncionesDinamicas, FuncionesDinamicasDTO> CONVERTER_DTO = (FuncionesDinamicas t) -> {
		var funcionesDinamicasDTO = new FuncionesDinamicasDTO();
		UtilsObjects.copiarPropiedades(t, funcionesDinamicasDTO);
		
		List<ParametrosFuncionesDinamicasDTO> parametrosFuncionesDinamicasDTO = new ArrayList();
		if(!t.getParametrosFuncionesDinamicas().isEmpty()) {
			t.getParametrosFuncionesDinamicas().forEach(parametroFuncionDTO ->{
				parametrosFuncionesDinamicasDTO.add(ParametrosFuncionesDinamicasDTO.CONVERTER_DTO.apply(parametroFuncionDTO));
			});
		}
		funcionesDinamicasDTO.setParametrosFuncionesDinamicasDTO(parametrosFuncionesDinamicasDTO);
		return funcionesDinamicasDTO;
	};
}
