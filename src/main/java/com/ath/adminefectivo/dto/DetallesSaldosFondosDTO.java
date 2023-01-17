package com.ath.adminefectivo.dto;


import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.entities.DetallesSaldosFondos;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.PuntosCostos;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Costos Clasificacion
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetallesSaldosFondosDTO {



	private int idDetallesSaldosFondos;

	private SaldosFondosDTO saldosFondosDTO;

	private String calidad;

	private long denominacion;

	private String tipoMoneda;

	private String familia;

	private String tipoClasifica;

	private Double valor;
	
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<DetallesSaldosFondosDTO, DetallesSaldosFondos> CONVERTER_ENTITY = (DetallesSaldosFondosDTO t) -> {
		var detallesSaldosFondos = new DetallesSaldosFondos();
		UtilsObjects.copiarPropiedades(t, detallesSaldosFondos);
		if(!Objects.isNull(t.getSaldosFondosDTO())) {
			detallesSaldosFondos.setSaldosFondos(SaldosFondosDTO.CONVERTER_ENTITY.apply(t.getSaldosFondosDTO()));
		}
//		if(!Objects.isNull(t.getTransportadoraOrigenDTO())) {
//			escalas.setTransportadoraOrigen(TransportadorasDTO.CONVERTER_ENTITY.apply(t.getTransportadoraOrigenDTO()));
//		}
//		if(!Objects.isNull(t.getCiudadOrigenDTO())) {
//			escalas.setCiudadOrigen(CiudadesDTO.CONVERTER_ENTITY.apply(t.getCiudadOrigenDTO()));
//		}
//		if(!Objects.isNull(t.getTransportadoraDestinoDTO())) {
//			escalas.setTransportadoraDestino(TransportadorasDTO.CONVERTER_ENTITY.apply(t.getTransportadoraDestinoDTO()));
//		}
//		if(!Objects.isNull(t.getCiudadDestinoDTO())) {
//			escalas.setCiudadDestino(CiudadesDTO.CONVERTER_ENTITY.apply(t.getCiudadDestinoDTO()));
//		}
		return detallesSaldosFondos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<DetallesSaldosFondos, DetallesSaldosFondosDTO> CONVERTER_DTO = (DetallesSaldosFondos t) -> {
		var detallesSaldosFondosDTO = new DetallesSaldosFondosDTO();
		UtilsObjects.copiarPropiedades(t, detallesSaldosFondosDTO);
		if(!Objects.isNull(t.getSaldosFondos())) {
			detallesSaldosFondosDTO.setSaldosFondosDTO(SaldosFondosDTO.CONVERTER_DTO.apply(t.getSaldosFondos()));
		}
//		if(!Objects.isNull(t.getTransportadoraOrigen())) {
//			escalasDTO.setTransportadoraOrigenDTO(TransportadorasDTO.CONVERTER_DTO.apply(t.getTransportadoraOrigen()));
//		}
//		if(!Objects.isNull(t.getCiudadOrigen())) {
//			escalasDTO.setCiudadOrigenDTO(CiudadesDTO.CONVERTER_DTO.apply(t.getCiudadOrigen()));
//		}
//		if(!Objects.isNull(t.getTransportadoraDestino())) {
//			escalasDTO.setTransportadoraDestinoDTO(TransportadorasDTO.CONVERTER_DTO.apply(t.getTransportadoraDestino()));
//		}
//		if(!Objects.isNull(t.getCiudadDestino())) {
//			escalasDTO.setCiudadDestinoDTO(CiudadesDTO.CONVERTER_DTO.apply(t.getCiudadDestino()));
//		}

		return detallesSaldosFondosDTO;
	};
}
