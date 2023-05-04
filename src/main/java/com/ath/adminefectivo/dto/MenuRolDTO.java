package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.entities.MenuRol;
import com.ath.adminefectivo.entities.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo Dto correspondiente al MENU-ROL
 * @author bayron.perez
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRolDTO {

	private Integer codigo;

	private Menu menu;

	private RolDTO rol;
	
	//Solo para salida de datos y noc aer en un json infinito
	private String idRol;

	private String estado;

	
	/**
	 * Funcion Conversor de ROL de Entity a Dto
	 * @author cesar.castano
	 */
	public static final Function<MenuRol, MenuRolDTO> CONVERTER_DTO = (MenuRol t) -> {
		MenuRolDTO menuRolDto = new MenuRolDTO();
		menuRolDto.setCodigo(t.getCodigo());
		menuRolDto.setMenu(t.getMenu());
		menuRolDto.setEstado(t.getEstado());
		menuRolDto.setIdRol(t.getRol().getIdRol());
		return menuRolDto;
	};
	
	/**
	 * Funcion Conversor de ROL de Entity a Dto
	 * @author cesar.castano
	 */
	public static final Function<MenuRolDTO, MenuRol> CONVERTER_ENTITY = (MenuRolDTO t) -> {
		MenuRol menuRolDto = new MenuRol();
		menuRolDto.setCodigo(t.getCodigo());
		menuRolDto.setMenu(t.getMenu());
		menuRolDto.setEstado(t.getEstado());
		
		Rol r = new Rol();
		r.setIdRol(t.getRol().getIdRol());
		menuRolDto.setRol(r);

		return menuRolDto;
	};
}
