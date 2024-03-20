package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.entities.MenuRol;
import com.ath.adminefectivo.entities.Rol;

@DataJpaTest
class MenuRolRepositoryTest {
	
	@Autowired
	private MenuRolRepository menuRolRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	private MenuRol menuRol;
	
	private Menu menu;
	
	private Rol rol;
	
	@BeforeEach
	void setup() {
		
		rol = Rol.builder()
				.idRol("1")
				.descripcion("Rol1")
				.estado("A")
				.fechaCreacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.nombre("ROL1")
				.usuarioCreacion("user1")
				.usuarioModificacion("user1")
				.build();
		rolRepository.save(rol);
		
		menu = Menu.builder()
				.idMenu("1")
				.nombre("menu1")
				.tipo("2")
				.url("/cargue-certificacion/")
				.estado("A")
				.fechaModificacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.icono("meu-item.icon")
				.usuarioCreacion("user2")
				.usuarioModificacion("user2")
				.build();
		menuRepository.save(menu);
		
		// Id columna codigo
		menuRol = MenuRol.builder()
			.estado("1")
			.fechaCreacion("")
			.fechaModificacion("")
			.menu(menu)
			.rol(rol)
			.usuarioCreacion("user1")
			.usuarioModificacion("user1")
			.build();
		
		menuRolRepository.save(menuRol);
	}
	
	@DisplayName("Crear menuRol nuevo devolver menuRol creado")
	@Test
	void givenMenuRolObjectWhenSaveThenReturnMenuRolSaved() {		
		// given - precondition or setup				
		
		// when -  action or the behaviour that we are going test
		MenuRol menuRolGuardado = menuRolRepository.save(menuRol);
		
		// then - verify the output
		assertThat(menuRolGuardado).isNotNull();
		assertThat(menuRolGuardado.getCodigo()).isPositive();
		
	}
	
	@DisplayName("Obtener todos los objetos MenuRol de FindAll()")
	@Test
	void givenMenuRolListWhenFindAllThenMenuRolList(){
		// given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		List<MenuRol> menuRolList = menuRolRepository.findAll();
		
		// then - verify the output
		assertThat(menuRolList).hasSize(1);
	}
	
	@DisplayName("Buscar por IdMenuRol y retornar objeto MenuRol")
	@Test
	void givenMenuRolObjectWhenFindByIdThenReturnMenuRolObject() {
		//given - precondition or setup		
		
		// when -  action or the behaviour that we are going test
		Optional<MenuRol> menuRolFind = menuRolRepository.findById(menuRol.getCodigo());
		
		// then - verify the output
		assertThat(menuRolFind).isNotEmpty();
		assertThat(menuRolFind.get().getCodigo()).isEqualTo(menuRol.getCodigo());
		
	}
	
	@DisplayName("Actualizar objeto MenuRol y retornar menuRol actualizado")
	@Test
    void givenMenuRolObjectWhenUpdateMenuRolThenReturnUpdatedMenuRol(){
		//given - precondition or setup
		MenuRol menuRolFind = menuRolRepository.findById(menuRol.getCodigo()).get();
		
		// when -  action or the behaviour that we are going test
		menuRolFind.setMenu(menu);
		menuRolFind.setRol(rol);
		menuRolFind.setEstado("A");
		menuRolFind.setFechaCreacion("");
		menuRolFind.setFechaModificacion("");
		menuRolFind.setUsuarioCreacion("user2");
		menuRolFind.setUsuarioModificacion("user3");
		MenuRol menuRolSaved = menuRolRepository.save(menuRolFind);
		
		// then - verify the output
		assertThat(menuRolSaved).isNotNull();
		assertThat(menuRolSaved.getRol()).isEqualTo(menuRolFind.getRol());
		assertThat(menuRolSaved.getMenu()).isEqualTo(menuRolFind.getMenu());
		assertThat(menuRolSaved.getEstado()).isEqualTo(menuRolFind.getEstado());
		assertThat(menuRolSaved.getFechaCreacion()).isEqualTo(menuRolFind.getFechaCreacion());
		assertThat(menuRolSaved.getFechaModificacion()).isEqualTo(menuRolFind.getFechaModificacion());
		assertThat(menuRolSaved.getUsuarioCreacion()).isEqualTo(menuRolFind.getUsuarioCreacion());
		assertThat(menuRolSaved.getUsuarioModificacion()).isEqualTo(menuRolFind.getUsuarioModificacion());
	}
	
	@DisplayName("Eliminar objeto menuRol y validar que no existe")
	@Test
    void givenMenuRolObjectWhenDeleteThenRemoveMenuRol(){
		//given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		menuRolRepository.delete(menuRol);
		Optional<MenuRol> menuRolFind = menuRolRepository.findById(menuRol.getCodigo());
		
		// then - verify the output
		assertThat(menuRolFind).isEmpty();
	}
}
