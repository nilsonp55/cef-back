package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import javax.validation.constraints.NotEmpty;

import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nilsonparra
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {

	@NotEmpty
	private String idMenu;
	
	@NotEmpty
	private String idMenuPadre;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String tipo;
	
	@NotEmpty
	private String icono;
	
	@NotEmpty
	private String url;
	
	@NotEmpty
	private String estado;
	
	private Date fechaCreacion;
	
	@NotEmpty
	private String usuarioCreacion;
	
	private Date fechaModificacion;
	
	@NotEmpty
	private String usuarioModificacion;
	
	/**
	 * Convierte una instancia Entitie en una instancia DTO
	 * @author nilsonparra
	 */
	public static final Function<Menu, MenuDTO> CONVERTER_DTO = (Menu t) -> {
		MenuDTO menuDTO = new MenuDTO();
		UtilsObjects.copiarPropiedades(t, menuDTO);
		return menuDTO;
	};
	
	/**
	 * Convierte una instancia DTO en una isntancia Entity
	 * @author prv_nparra
	 */
	public static final Function<MenuDTO, Menu> CONVERTER_ENTITY = (MenuDTO t) -> {
		Menu menu = new Menu();
		UtilsObjects.copiarPropiedades(t, menu);
		return menu;
	};
	
}
