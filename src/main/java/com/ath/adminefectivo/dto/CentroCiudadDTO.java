package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CentroCiudad;
import com.ath.adminefectivo.entities.Ciudades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de CentroCiudadDTO
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CentroCiudadDTO {

	private Integer idCentroCiudad;

	private CiudadesDTO ciudadDane;

	private BancosDTO bancoAval;
	
	private String codigoCentro;


	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CentroCiudadDTO, CentroCiudad> CONVERTER_ENTITY = (CentroCiudadDTO t) -> {
		var centroCiudad = new CentroCiudad();

		centroCiudad.setIdCentroCiudad(t.getIdCentroCiudad());
		
		Bancos bancos = new Bancos();
		bancos.setCodigoPunto(t.getBancoAval().getCodigoPunto());
		centroCiudad.setBancoAval(bancos);
		
		Ciudades ciudades = new Ciudades();
		ciudades.setCodigoDANE(t.getCiudadDane().getCodigoDANE());
		centroCiudad.setCiudadDane(ciudades);
		
		centroCiudad.setCodigoCentro(t.getCodigoCentro());

		return centroCiudad;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<CentroCiudad, CentroCiudadDTO> CONVERTER_DTO = (CentroCiudad t) -> {
		var centroCiudadDTO = new CentroCiudadDTO();
		centroCiudadDTO.setIdCentroCiudad(t.getIdCentroCiudad());

		
		BancosDTO bancos = new BancosDTO();
		bancos.setCodigoPunto(t.getBancoAval().getCodigoPunto());
		bancos.setNombreBanco(t.getBancoAval().getNombreBanco());
		centroCiudadDTO.setBancoAval(bancos);
		
		CiudadesDTO ciudades = new CiudadesDTO();
		ciudades.setCodigoDANE(t.getCiudadDane().getCodigoDANE());
		ciudades.setNombreCiudad(t.getCiudadDane().getNombreCiudad());
		centroCiudadDTO.setCiudadDane(ciudades);
		
		centroCiudadDTO.setCodigoCentro(t.getCodigoCentro());
		
		return centroCiudadDTO;
	};
}
