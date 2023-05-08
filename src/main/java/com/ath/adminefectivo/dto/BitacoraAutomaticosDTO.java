package com.ath.adminefectivo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.ath.adminefectivo.entities.BitacoraAutomaticos;
import com.ath.adminefectivo.entities.DetallesProcesoAutomatico;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de BitacoraAutomaticos
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BitacoraAutomaticosDTO {

	private Long idRegistro;
	
	private Date fechaSistema;
	
	private Date fechaHoraInicio;
	
	private Date fechaHoraFinal;
	
	private String codigoProceso;
	
	private String resultado;
	
	private String mensajeError;
	
	private List<DetallesProcesoAutomaticoDTO> detallesProcesosAutomaticosDTO;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<BitacoraAutomaticosDTO, BitacoraAutomaticos> CONVERTER_ENTITY = (BitacoraAutomaticosDTO t) -> {
		var bitacoraAutomaticos = new BitacoraAutomaticos();
		UtilsObjects.copiarPropiedades(t, bitacoraAutomaticos);
		if(t.getDetallesProcesosAutomaticosDTO().size() > 0 || t.getDetallesProcesosAutomaticosDTO() != null) {
			List<DetallesProcesoAutomatico> detallesProcesosAutomaticos = new ArrayList<>();
			t.getDetallesProcesosAutomaticosDTO().forEach(detalleProcesoAuto ->
				detallesProcesosAutomaticos.add(DetallesProcesoAutomaticoDTO.CONVERTER_ENTITY.apply(detalleProcesoAuto))
			);
			bitacoraAutomaticos.setDetallesProcesosAutomaticos(detallesProcesosAutomaticos);
		}
		return bitacoraAutomaticos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<BitacoraAutomaticos, BitacoraAutomaticosDTO> CONVERTER_DTO = (BitacoraAutomaticos t) -> {
		var bitacoraAutomaticosDTO = new BitacoraAutomaticosDTO();
		UtilsObjects.copiarPropiedades(t, bitacoraAutomaticosDTO);
		
		if(t.getDetallesProcesosAutomaticos().size() > 0 || t.getDetallesProcesosAutomaticos() != null) {
			List<DetallesProcesoAutomaticoDTO> detallesProcesosAutomaticos = new ArrayList<>();
			t.getDetallesProcesosAutomaticos().forEach(detalleProcesoAutoDTO ->
				detallesProcesosAutomaticos.add(DetallesProcesoAutomaticoDTO.CONVERTER_DTO.apply(detalleProcesoAutoDTO))
			);
			bitacoraAutomaticosDTO.setDetallesProcesosAutomaticosDTO(detallesProcesosAutomaticos);
		}
		return bitacoraAutomaticosDTO;
	};
}
