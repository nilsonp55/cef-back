package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.entities.id.DominioPK;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DominioDTO {

	private DominioPK id;

	private String descripcion;

	private String tipo;

	private String valorTexto;

	private Double valorNumero;

	private Date valorFecha;

	private String estado;

	private String usuarioCreacion;

	private Date fechaCreacion;

	private String usuarioModificacion;

	private Date fechaModificacion;

	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<DominioDTO, Dominio> CONVERTER_ENTITY = (DominioDTO t) -> {
		Dominio dominio = new Dominio();
		UtilsObjects.copiarPropiedades(t, dominio);
		dominio.setDominioPK(t.getId());
		return dominio;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Dominio, DominioDTO> CONVERTER_DTO = (Dominio t) -> {
		DominioDTO dominioDTO = new DominioDTO();
		DominioPK dominioPK = new DominioPK();
		dominioPK.setCodigo(t.getDominioPK().getCodigo());
		dominioPK.setDominio(t.getDominioPK().getDominio());
		UtilsObjects.copiarPropiedades(t, dominioDTO);
		dominioDTO.setId(dominioPK);
		return dominioDTO;
	};


}
