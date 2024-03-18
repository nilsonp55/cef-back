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

@DataJpaTest
class MenuRepositoryTest {
	
	@Autowired
	private MenuRepository menuRepository;

	
	private Menu menu;
	
	@BeforeEach
	void setup() {
		
		menu = Menu.builder()
			.idMenu("1")
			.icono("image.ico")
			.idMenuPadre("2")
			.nombre("menu 2")
			.tipo("opcion")
			.url("/cargue-programacion/preliminar")
			.estado("1")
			.fechaCreacion( Date.from(Instant.now()) )
			.fechaModificacion( Date.from(Instant.now()) )
			.usuarioCreacion("user1")
			.usuarioModificacion("user1")
			.build();
		
	}
	
	@DisplayName("Crear menu nuevo devolver menu creado")
	@Test
	void givenMenuObject_whenSave_thenReturnMenuSaved() {		
		// given - precondition or setup				
		
		// when -  action or the behaviour that we are going test
		Menu menuGuardado = menuRepository.save(menu);
		
		// then - verify the output
		assertThat(menuGuardado).isNotNull();
		assertThat(menuGuardado.getIdMenu()).isNotEmpty();
		
	}
	
	@DisplayName("Obtener todos los objetos Menu de FindAll()")
	@Test
	void givenMenuList_whenFindAll_thenMenuList(){
		// given - precondition or setup
		menuRepository.save(menu);
		
		// when -  action or the behaviour that we are going test
		List<Menu> menuList = menuRepository.findAll();
		
		// then - verify the output
		assertThat(menuList).hasSize(1);
	}
	
	@DisplayName("Buscar por IdMenu y retornar objeto Menu")
	@Test
	void givenMenuObject_whenFindById_thenReturnMenuObject() {
		//given - precondition or setup
		menuRepository.save(menu);
		
		// when -  action or the behaviour that we are going test
		Optional<Menu> menuFind = menuRepository.findById(menu.getIdMenu());
		
		// then - verify the output
		assertThat(menuFind).isNotEmpty();
		assertThat(menuFind.get().getIdMenu()).isEqualTo(menu.getIdMenu());
		
	}
	
	@DisplayName("Actualizar objeto Menu y retornar menu actualizado")
	@Test
    void givenMenuObject_whenUpdateMenu_thenReturnUpdatedMenu(){
		//given - precondition or setup
		Date fechaCreacion = Date.from(Instant.now());
		Date fechaModificacion = Date.from(Instant.now());
		menuRepository.save(menu);
		Menu menuFind = menuRepository.findById(menu.getIdMenu()).get();
		
		// when -  action or the behaviour that we are going test
		menuFind.setIcono("image.ico");
		menuFind.setIdMenuPadre("2");
		menuFind.setNombre("menu 7");
		menuFind.setTipo("opcion 2");
		menuFind.setUrl("/cargue-programacion/preliminar");
		menuFind.setEstado("I");
		menuFind.setFechaCreacion(fechaCreacion);
		menuFind.setFechaModificacion(fechaModificacion);
		menuFind.setUsuarioCreacion("user2");
		menuFind.setUsuarioModificacion("user3");
		Menu menuSaved = menuRepository.save(menuFind);
		
		// then - verify the output
		assertThat(menuSaved).isNotNull();
		assertThat(menuSaved.getIcono()).isEqualTo(menuFind.getIcono());
		assertThat(menuSaved.getEstado()).isEqualTo(menuFind.getEstado());
		assertThat(menuSaved.getFechaCreacion()).isEqualTo(menuFind.getFechaCreacion());
		assertThat(menuSaved.getFechaModificacion()).isEqualTo(menuFind.getFechaModificacion());
		assertThat(menuSaved.getIdMenuPadre()).isEqualTo(menuFind.getIdMenuPadre());
		assertThat(menuSaved.getNombre()).isEqualTo(menuFind.getNombre());
		assertThat(menuSaved.getTipo()).isEqualTo(menuFind.getTipo());
		assertThat(menuSaved.getUrl()).isEqualTo(menuFind.getUrl());
		assertThat(menuSaved.getUsuarioCreacion()).isEqualTo(menuFind.getUsuarioCreacion());
		assertThat(menuSaved.getUsuarioModificacion()).isEqualTo(menuFind.getUsuarioModificacion());
	}
	
	@DisplayName("Eliminar objeto menu y validar que no existe")
	@Test
    void givenMenuObject_whenDelete_thenRemoveMenu(){
		//given - precondition or setup
		menuRepository.save(menu);
		
		// when -  action or the behaviour that we are going test
		menuRepository.delete(menu);
		Optional<Menu> menuFind = menuRepository.findById(menu.getIdMenu());
		
		// then - verify the output
		assertThat(menuFind).isEmpty();
	}

}
