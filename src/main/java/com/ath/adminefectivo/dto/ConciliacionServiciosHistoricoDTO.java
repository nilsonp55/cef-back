package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.ConciliacionServiciosHistorico;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la tabla de Conciliacion Servicios Historicos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConciliacionServiciosHistoricoDTO {

	private Integer idConciliacion;

	private Integer idOperacion;

	private Integer idCertificacion;

	private String estado;

	private Date fechaConciliacion;

	private String usuarioCreacion;

	private String usuarioModificacion;

	private Date fechaModificacion;

	private Date fechaCreacion;

	private String tipoConciliacion;

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ConciliacionServiciosHistorico, ConciliacionServiciosHistoricoDTO> CONVERTER_DTO = (
			ConciliacionServiciosHistorico t) -> {
		var conciliacionServiciosHistoricoDTO = new ConciliacionServiciosHistoricoDTO();
		UtilsObjects.copiarPropiedades(t, conciliacionServiciosHistoricoDTO);
		return conciliacionServiciosHistoricoDTO;
	};

	/**
	 * Funcion que convierte el archivo DTO ConciliacionServiciosHistoricoDTO a Entity
	 * ArchivosCargados
	 * 
	 * @author cesar.castano
	 */
	public static final Function<ConciliacionServiciosHistoricoDTO, ConciliacionServiciosHistorico> CONVERTER_ENTITY = (
			ConciliacionServiciosHistoricoDTO t) -> {

		ConciliacionServiciosHistorico conciliacionServiciosHistorico = new ConciliacionServiciosHistorico();
		UtilsObjects.copiarPropiedades(t, conciliacionServiciosHistorico);

		return conciliacionServiciosHistorico;
	};
}
