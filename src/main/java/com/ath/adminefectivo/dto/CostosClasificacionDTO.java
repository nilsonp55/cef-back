package com.ath.adminefectivo.dto;


import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.entities.CostosClasificacion;
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
public class CostosClasificacionDTO {


	private int idCostosClasificacion;
	
	private int bancoAval;
	
	private String transportadora;
	
	private String mesAño;
	
	private Date fechaModificacion;
	
	private String usuarioModificacion;
	
	private int fajosEstimados;
	
	private int bolsasEstimadas;
	
	private int cantidadFajos;
	
	private int cantidadRem;
	
	private int cantidadMonedas;
	
	private int valorClasifFajos;
	
	private int valorClasifRem;
	
	private int valorClasifMonedas;
		
	private int estado;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CostosClasificacionDTO, CostosClasificacion> CONVERTER_ENTITY = (CostosClasificacionDTO t) -> {
		var costosClasificacion = new CostosClasificacion();
		UtilsObjects.copiarPropiedades(t, costosClasificacion);
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
		return costosClasificacion;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<CostosClasificacion, CostosClasificacionDTO> CONVERTER_DTO = (CostosClasificacion t) -> {
		var costosClasificacionDTO = new CostosClasificacionDTO();
		UtilsObjects.copiarPropiedades(t, costosClasificacionDTO);
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

		return costosClasificacionDTO;
	};
}
