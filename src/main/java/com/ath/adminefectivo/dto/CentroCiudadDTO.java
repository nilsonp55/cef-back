package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CentroCiudad;
import com.ath.adminefectivo.entities.CentroCiudadPpal;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.utils.UtilsObjects;

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
	
	/**
	 * Convierte una instancia Entity en una instancia DTO para CentroCiudadPpal
	 * @author prv_nparra
	 */
	public static final Function<CentroCiudadDTO, CentroCiudadPpal> CONVERTER_ENTITY_PPAL = (CentroCiudadDTO dto) -> {
		var centroCiudadPpal = new CentroCiudadPpal();
		Bancos banco = new Bancos();
		banco.setCodigoPunto(dto.getBancoAval().getCodigoPunto());
		centroCiudadPpal.setBancoAval(banco);
		Ciudades ciudad = new Ciudades();
		ciudad.setCodigoDANE(dto.getCiudadDane().getCodigoDANE());
		centroCiudadPpal.setCodigoDane(ciudad);
		UtilsObjects.copiarPropiedades(dto, centroCiudadPpal);
		return centroCiudadPpal;
	};
	
	/**
	 * Convierte una instancia DTO en una instancia Entity para CentroCiudadPpal
	 * @author prv_nparra
	 */
	public static final Function<CentroCiudadPpal, CentroCiudadDTO> CONVERTER_DTO_PPAL = (CentroCiudadPpal entity) -> {
		var centroCiudadDTO = new CentroCiudadDTO();
		centroCiudadDTO.setIdCentroCiudad(entity.getIdCentroCiudadPpal());
		centroCiudadDTO.setBancoAval(BancosDTO.CONVERTER_DTO.apply(entity.getBancoAval()));
		centroCiudadDTO.setCiudadDane(CiudadesDTO.CONVERTER_DTO.apply(entity.getCodigoDane()));
		UtilsObjects.copiarPropiedades(entity, centroCiudadDTO);
		return centroCiudadDTO;
	};
}
