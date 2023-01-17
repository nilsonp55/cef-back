package com.ath.adminefectivo.dto;


import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.PuntosCostos;
import com.ath.adminefectivo.entities.SaldosFondos;
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
public class SaldosFondosDTO {



	private int idSaldosFondos;

	private int codigoFondo;

	private String transportadora;

	private int bancoAval;

	private String codigoCiudad;

	private Date fecha;

	private long saldoInicialMonedas;

	private long saldoFinalMonedas;

	private long saldoInicialRem;

	private long saldoFinalRem;

	private long saldoInicialBestd;

	private long saldoFinalBestd;

	private long saldoInicialCrlt;

	private long saldoFinalCrlt;

	private long saldoInicialDtro;

	private long saldoFinalDtro;
	
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<SaldosFondosDTO, SaldosFondos> CONVERTER_ENTITY = (SaldosFondosDTO t) -> {
		var saldosFondos = new SaldosFondos();
		UtilsObjects.copiarPropiedades(t, saldosFondos);
//		if(!Objects.isNull(t.getBancosDTO())) {
//			escalas.setBancos(BancosDTO.CONVERTER_ENTITY.apply(t.getBancosDTO()));
//		}
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
		return saldosFondos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<SaldosFondos, SaldosFondosDTO> CONVERTER_DTO = (SaldosFondos t) -> {
		var saldosFondosDTO = new SaldosFondosDTO();
		UtilsObjects.copiarPropiedades(t, saldosFondosDTO);
//		if(!Objects.isNull(t.getBancos())) {
//			escalasDTO.setBancosDTO(BancosDTO.CONVERTER_DTO.apply(t.getBancos()));
//		}
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

		return saldosFondosDTO;
	};
}
