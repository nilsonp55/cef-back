package com.ath.adminefectivo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ath.adminefectivo.dto.RolDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Rol;
import com.ath.adminefectivo.service.IRolService;
import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a los Roles
 * 
 * @author CamiloBenavides
 */

@Log4j2
@RestController
@RequestMapping("${endpoints.Rol}")
public class RolController {

  @Autowired
  IRolService rolService;

  /**
   * Servicio encargado de retornar la consulta de todos los roles del aplicativo
   * 
   * @return ResponseEntity<ApiResponseADE<List<RolDTO>>>
   * @author CamiloBenavides
   */
  @GetMapping(value = "${endpoints.Rol.consultar}")
  public ResponseEntity<ApiResponseADE<List<RolDTO>>> getRoles() {
    log.info("getRoles");
    var consulta = rolService.getRoles();
    log.info("getRoles size: {}", consulta.size());
    return ResponseEntity.status(HttpStatus.OK).body(
        new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
            .description(ApiResponseCode.SUCCESS.getDescription()).build()));
  }
  
  @PostMapping(value = "${endpoints.Rol.guardar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponseADE<Rol>> createRol(@RequestBody Rol rol) {
	  Rol rolCreated = rolService.createRoles(rol);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponseADE<>(rolCreated,
            ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                .description(ApiResponseCode.SUCCESS.getDescription()).build()));
  }
  
  @PutMapping(value = "${endpoints.Rol.actualizar}", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponseADE<String>> updateRol(@RequestBody Rol rol, @RequestParam("previousId") String previousId) {
	  rolService.updateRoles(rol, previousId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponseADE<>("OK",
            ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                .description(ApiResponseCode.SUCCESS.getDescription()).build()));
  }
  
  public ResponseEntity<ApiResponseADE<Void>> deleteRol(String idRol) {
    rolService.deleteRoles(idRol);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(new ApiResponseADE<>(null,
            ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                .description(ApiResponseCode.SUCCESS.getDescription()).build()));
  }
}
