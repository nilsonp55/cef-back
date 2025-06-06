package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.entities.QMenu;

@DataJpaTest
class MenuRepositoryTest {

	@Autowired
	private MenuRepository menuRepository;

	private Menu menu;
	private List<Menu> listOfMenus;
	private List<Menu> itemsSaved;

	private static final String S_USER_CREATION = "user1";

	@BeforeEach
	void setup() throws Exception {

		listOfMenus = Instancio.ofList(Menu.class).size(11)
				.set(field(Menu::getUsuarioCreacion), S_USER_CREATION)
				.create();

		// given - precondition or setup
		itemsSaved = menuRepository.saveAll(listOfMenus);

		menu = Instancio.of(Menu.class).create();

	}

	@DisplayName("Crear menu nuevo devolver menu creado")
	@Test
	void givenMenuObjectWhenSaveThenReturnMenuSaved() {

		// when - action or the behaviour that we are going test
		Menu menuGuardado = menuRepository.save(menu);

		// then - verify the output
		assertThat(menuGuardado).isNotNull();
		assertThat(menuGuardado.getIdMenu()).isNotEmpty();

	}

	@DisplayName("Obtener todos los objetos Menu de FindAll()")
	@Test
	void givenMenuListWhenFindAllThenMenuList() {

		// when - action or the behaviour that we are going test
		List<Menu> menuList = menuRepository.findAll();

		// then - verify the output
		assertThat(menuList).hasSize(listOfMenus.size());
	}

	@DisplayName("Buscar por IdMenu y retornar objeto Menu")
	@Test
	void givenMenuObjectWhenFindByIdThenReturnMenuObject() {
		// given - precondition or setup
		menuRepository.save(menu);

		// when - action or the behaviour that we are going test
		Optional<Menu> menuFind = menuRepository.findById(menu.getIdMenu());

		// then - verify the output
		assertThat(menuFind).isNotEmpty();
		menuFind.ifPresent(m -> assertThat(m.getIdMenu()).isEqualTo(menu.getIdMenu()));

	}

	@DisplayName("Actualizar objeto Menu y retornar menu actualizado")
	@Test
	void givenMenuObjectWhenUpdateMenuThenReturnUpdatedMenu() {
		// given - precondition or setup
		Menu menuFind = itemsSaved.get(0);

		// when - action or the behaviour that we are going test
		menuFind.setIcono("image.ico");
		menuFind.setIdMenuPadre("2");
		menuFind.setNombre("menu 7");
		menuFind.setTipo("opcion 2");
		menuFind.setUrl("/cargue-programacion/preliminar");
		menuFind.setEstado("I");
		menuFind.setFechaCreacion(Date.from(Instant.now()));
		menuFind.setFechaModificacion(Date.from(Instant.now()));
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
	void givenMenuObjectWhenDeleteThenRemoveMenu() {
		// given - precondition or setup
		menuRepository.save(menu);

		// when - action or the behaviour that we are going test
		menuRepository.delete(menu);
		Optional<Menu> menuFind = menuRepository.findById(menu.getIdMenu());

		// then - verify the output
		assertThat(menuFind).isEmpty();
	}

	@DisplayName("Hallar en menus dado un Predicate para el campo UsuarioCreacion")
	@Test
	void givenMenuListWhenFindByPredicateByFieldUserCreationThenReturnList() {

		// given
		Iterable<Menu> menuList = menuRepository.findAll(QMenu.menu.usuarioCreacion.eq(S_USER_CREATION));

		List<Menu> listMenus = new ArrayList<Menu>();
		menuList.iterator().forEachRemaining(listMenus::add);

		// when - action or the behaviour that we are going test
		assertThat(menuList.iterator().hasNext()).isTrue();

		assertThat(listMenus.size()).isEqualTo(listOfMenus.size());

	}

	@DisplayName("Hallar en menus dado un Predicate para el campo Nombre")
	@Test
	void givenMenuListWhenFindByPredicateByMenuNameThenReturnList() {

		// given
		Menu menuFind = itemsSaved.get(0);
		Iterable<Menu> menuList = menuRepository.findAll(QMenu.menu.nombre.eq(menuFind.getNombre()));

		// when - action or the behaviour that we are going test
		assertThat(menuList.iterator().hasNext()).isTrue();
	}

}
