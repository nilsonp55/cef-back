package com.ath.adminefectivo.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.RolDTO;
import com.ath.adminefectivo.entities.Rol;
import com.ath.adminefectivo.repositories.RolRepository;
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
  public RolDTO createRoles(RolDTO rol) {
    log.debug("createRoles: {}", rol.getNombre());
    Rol rolEntity = rolRepository.save(RolDTO.CONVERTER_ENTITY.apply(rol));
    log.debug("Rol created: {}", rol.getNombre());
    return RolDTO.CONVERTER_DTO.apply(rolEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RolDTO updateRoles(RolDTO rol) {
    log.debug("updateRoles id: {}", rol.getIdRol());
    rolRepository.findById(rol.getIdRol()).orElseThrow();
    Rol rolEntity = rolRepository.save(RolDTO.CONVERTER_ENTITY.apply(rol));
    log.debug("id rol updated: {}", rol.getIdRol());
    return RolDTO.CONVERTER_DTO.apply(rolEntity);
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
