package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.MenuRepository;
import com.ath.adminefectivo.service.IMenuService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MenuServiceImpl implements IMenuService {

	private final MenuRepository menuRepository;

	public MenuServiceImpl(@Autowired MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

    @Override
    public List<MenuDTO> getMenuByPredicate(Predicate predicate) {

      List<MenuDTO> listMenuDTO = new ArrayList<>();
      menuRepository.findAll(predicate)
          .forEach(t -> listMenuDTO.add(MenuDTO.CONVERTER_DTO.apply(t)));
      log.debug("List Menu size: ", listMenuDTO.size());

      return listMenuDTO;
    }
    
    @Override
    public List<String> getAllIdMenu() {
      List<String> listIdMenu = menuRepository.getAllIdMenu();
      log.debug("List ID Menu size: ", listIdMenu.size());

      return listIdMenu;
    }

    @Override
    public MenuDTO createMenu(MenuDTO menu) {
      log.debug("createMenu: {}", menu.getNombre());
      
      menuRepository.findById(menu.getIdMenu()).ifPresent(m -> {
        log.debug("menu id exists: {} - name: {}", m.getIdMenu(), m.getNombre());
        throw new NegocioException(ApiResponseCode.ERROR_EXIST_REGISTRO.getCode(), 
            ApiResponseCode.ERROR_EXIST_REGISTRO.getDescription() + m.getNombre(), 
            ApiResponseCode.ERROR_EXIST_REGISTRO.getHttpStatus());
      });
      
      menu.setIdMenu(String.valueOf(menuRepository.findMaxIdMenuQuery() + 1));
      
      var entity = menuRepository.save(MenuDTO.CONVERTER_ENTITY.apply(menu));
      log.debug("createMenu Nombre: {} - id: {}", entity.getNombre(), entity.getIdMenu());
      return MenuDTO.CONVERTER_DTO.apply(entity);
    }
  
    @Override
    public MenuDTO updateMenu(MenuDTO menu) {
      log.debug("updateMenu: Id: {}", menu.getIdMenu());
      
      menuRepository.findById(menu.getIdMenu()).orElseThrow();
      var entity = MenuDTO.CONVERTER_ENTITY.apply(menu);
      var entitySaved = menuRepository.save(entity);

      log.debug("updateMenu: Updated Id: {}", entitySaved.getIdMenu());
      return MenuDTO.CONVERTER_DTO.apply(entitySaved);
    }
  
    @Override
    public void deleteMenu(String idMenu) {
      log.debug("deleteMenu: Id: {}", idMenu);
      menuRepository.deleteById(idMenu);
      log.debug("deleteMenu: Deleted Id: {}", idMenu);
    }

}
