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
		var MenuRolDto = new MenuRolDTO();
		MenuRolDto.setCodigo(t.getCodigo());
		MenuRolDto.setMenu(t.getMenu());
		MenuRolDto.setEstado(t.getEstado());
		MenuRolDto.setIdRol(t.getRol().getIdRol());
		return MenuRolDto;
	};
	
	/**
	 * Funcion Conversor de ROL de Entity a Dto
	 * @author cesar.castano
	 */
	public static final Function<MenuRolDTO, MenuRol> CONVERTER_ENTITY = (MenuRolDTO t) -> {
		var MenuRol = new MenuRol();
		MenuRol.setCodigo(t.getCodigo());
		MenuRol.setMenu(t.getMenu());
		MenuRol.setEstado(t.getEstado());
		
		Rol rol = new Rol();
		rol.setIdRol(t.getRol().getIdRol());
		MenuRol.setRol(rol);

		return MenuRol;
	};
}
