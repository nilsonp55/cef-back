package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import java.sql.SQLException;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.repositories.MenuRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private MenuServiceImpl menuService;

	private List<Menu> listOfMenus;
	
	private Menu menuEntity;
	
	private MenuDTO menuDto;
	
	private static final String S_USER_CREATION = "user1";

    @BeforeEach
    void setUp() throws Exception {

      listOfMenus = Instancio.ofList(Menu.class).size(11)
          .set(field(Menu::getUsuarioCreacion), S_USER_CREATION).create();
      log.info("setup size: {}", listOfMenus.size());
      
      this.menuEntity =
          Instancio.of(Menu.class).set(field(Menu::getUsuarioCreacion), S_USER_CREATION).create();      
      log.info("setup idEntity: {}", menuEntity.getIdMenu());
      
      this.menuDto = Instancio.of(MenuDTO.class).set(field(MenuDTO::getIdMenu), null).create();
      log.info("setup MenuDTO Nombre: {}", menuDto.getNombre());
    }

	@Test
	void testGetAllMenus() {
		// given
		BooleanBuilder builder = new BooleanBuilder();
		given(menuRepository.findAll(builder)).willReturn(listOfMenus);

		// when
		List<MenuDTO> listMenuDTO = menuService.getMenuByPredicate(builder);

		// then
		assertThat(listMenuDTO.size()).isEqualTo(listOfMenus.size());
		log.info("List size: {}", listMenuDTO.size());
	}
	
	@Test
	void testGetAllMenusReturnEntitieWithNull() {

		// given
		BooleanBuilder builder = new BooleanBuilder();
		List<Menu> listWithNullValue = Instancio.ofList(Menu.class).size(1)
				.set(field(Menu::getIdMenu), null)
				.create();
		given(menuRepository.findAll(builder)).willReturn(listWithNullValue);

		// when
		List<MenuDTO> itemsMenu = menuService.getMenuByPredicate(builder);
		
		// then 
		assertThat(itemsMenu.get(0).getIdMenu()).isNull();
	}
	
	@Test
	void testCreateMenu() {
	  this.menuDto = MenuDTO.CONVERTER_DTO.apply(this.menuEntity);
	  when(this.menuRepository.save(any())).thenReturn(this.menuEntity);
	  this.menuDto = this.menuService.createMenu(this.menuDto);
	  assertThat(this.menuDto.getIdMenu()).isNotNull();
	  assertThat(this.menuDto.getEstado()).isEqualTo(this.menuEntity.getEstado());
	  assertThat(this.menuDto.getFechaCreacion()).isEqualTo(this.menuEntity.getFechaCreacion());
	  assertThat(this.menuDto.getFechaModificacion()).isEqualTo(this.menuEntity.getFechaModificacion());
	  assertThat(this.menuDto.getIcono()).isEqualTo(this.menuEntity.getIcono());
	  assertThat(this.menuDto.getIdMenuPadre()).isEqualTo(this.menuEntity.getIdMenuPadre());
	  assertThat(this.menuDto.getNombre()).isEqualTo(this.menuEntity.getNombre());
	  assertThat(this.menuDto.getTipo()).isEqualTo(this.menuEntity.getTipo());
	  assertThat(this.menuDto.getUrl()).isEqualTo(this.menuEntity.getUrl());
	  assertThat(this.menuDto.getUsuarioCreacion()).isEqualTo(this.menuEntity.getUsuarioCreacion());
	  assertThat(this.menuDto.getUsuarioModificacion()).isEqualTo(this.menuEntity.getUsuarioModificacion());
	  log.info("testCreateMenu id: {}", this.menuDto.getIdMenu());
	}
	
    @Test
    void testCreateMenuExceptionData() {

      var throwedExceptionData = new DataIntegrityViolationException(
          "test created DataIntegrityException", new SQLException("test created SQLException"));
      when(this.menuRepository.save(any())).thenThrow(throwedExceptionData);
      Exception exception = assertThrows(DataAccessException.class, () -> {
        this.menuService.createMenu(this.menuDto);
      });
      assertThat(exception.getMessage()).contains(throwedExceptionData.getMessage());
      assertThat(exception.getCause().getMessage())
          .contains(throwedExceptionData.getCause().getMessage());

      log.info("testCreateMenuExceptionData msg: {}", exception.getMessage());
    }

}
