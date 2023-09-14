package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.TiposCentrosCostos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad TiposCentrosCostosDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TiposCentrosCostosDTO {

	private String tipoCentro;
	
	private BancosDTO bancoAval;

	private String nombreCentro;
	
	private String codigoCentro;
	
	private String tablaCentros;
	
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TiposCentrosCostosDTO, TiposCentrosCostos> CONVERTER_ENTITY = (TiposCentrosCostosDTO t) -> {
		TiposCentrosCostos tiposCentrosCostos = new TiposCentrosCostos();
		tiposCentrosCostos.setTipoCentro(t.getTipoCentro());
		Bancos ba = new Bancos();
		ba.setCodigoPunto(t.getBancoAval().getCodigoPunto());
		tiposCentrosCostos.setBancoAval(ba);
		tiposCentrosCostos.setNombreCentro(t.getNombreCentro());
		tiposCentrosCostos.setCodigoCentro(t.getCodigoCentro());
		tiposCentrosCostos.setTablaCentros(t.getTablaCentros());
		
		return tiposCentrosCostos;
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TiposCentrosCostos, TiposCentrosCostosDTO> CONVERTER_DTO = (TiposCentrosCostos t) -> {
		TiposCentrosCostosDTO tiposCentrosCostos = new TiposCentrosCostosDTO();
		tiposCentrosCostos.setTipoCentro(t.getTipoCentro());
		BancosDTO ba = new BancosDTO();
		ba.setCodigoPunto(t.getBancoAval().getCodigoPunto());
		ba.setAbreviatura(t.getBancoAval().getAbreviatura());
		tiposCentrosCostos.setBancoAval(ba);
		tiposCentrosCostos.setNombreCentro(t.getNombreCentro());
		tiposCentrosCostos.setCodigoCentro(t.getCodigoCentro());
		tiposCentrosCostos.setTablaCentros(t.getTablaCentros());
		
		return tiposCentrosCostos;
	};
	
}
