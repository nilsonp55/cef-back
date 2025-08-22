package com.ath.adminefectivo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.RolDTO;
import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.entities.MenuRol;
import com.ath.adminefectivo.entities.Rol;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.RolRepository;
import com.ath.adminefectivo.service.IMenuRolService;
import com.ath.adminefectivo.service.IMenuService;
import com.ath.adminefectivo.service.IRolService;
import com.ath.adminefectivo.utils.UtilsObjects;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RolServiceImpl implements IRolService {

  private final RolRepository rolRepository;

  public RolServiceImpl(@Autowired RolRepository rolRepository) {
    this.rolRepository = rolRepository;
  }
  
  @Autowired
  IMenuService menuService;
  
  @Autowired
  IMenuRolService menuRolServiceImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RolDTO> getRoles() {
    List<Rol> roles = rolRepository.findAll();
    log.debug("getRoles size: {}", roles.size());

    return roles.stream().map(x -> {
      RolDTO dto = new RolDTO();
      UtilsObjects.copiarPropiedades(x, dto, false);
      return dto;
    }).toList();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Rol createRoles(Rol rol) {
    log.debug("createRoles: {}", rol.getNombre());
    if(rol.getIdRol() != null && rolRepository.existsById(rol.getIdRol())) {
    	throw new ConflictException("El Rol ya existe con el Id Rol: " + rol.getIdRol());
    }
    Rol rolEntity = rolRepository.save(rol);
    List<String> idMenus = menuService.getAllIdMenu(); 
    List<MenuRol> menuRolList = idMenus.stream().map(idMenu -> {
    	MenuRol menuRol = new MenuRol();
    	Menu menuRef = new Menu();
    	menuRef.setIdMenu(idMenu);
    	menuRol.setMenu(menuRef);
    	menuRol.setRol(rolEntity);
    	menuRol.setEstado("2");
    	return menuRol;
    }).toList();
    menuRolServiceImpl.saveAllMenuRol(menuRolList);
    log.debug("Rol created: {}", rol.getNombre());
    return rolEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateRoles(Rol rol, String previousId) {
    log.debug("updateRoles id: {}", rol.getIdRol());
    
    rolRepository.findById(previousId).orElseThrow(
        () -> new ConflictException("El Rol ya existe con el Id Rol: " + rol.getIdRol()));

    if(rol.getIdRol().equals(previousId)) {
    	rolRepository.updateRol(
        		rol.getNombre(),
        		rol.getDescripcion(), 
        		rol.getEstado(),
        		previousId );
    	log.debug("id rol updated: {}", rol.getIdRol());
    	return;
    }
    rolRepository.updateRolAndIdRol(
    		rol.getIdRol(),
    		rol.getNombre(),
    		rol.getDescripcion(), 
    		rol.getEstado(),
    		previousId 
    		);
    log.debug("id rol updated: {}", rol.getIdRol());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRoles(String idRol) {
    log.debug("deleteRoles id: {}", idRol);
    rolRepository.deleteById(idRol);
    log.debug("id rol deleted: {}", idRol);
  }

}
