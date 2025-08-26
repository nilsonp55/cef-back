package com.ath.adminefectivo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.service.ICiudadesService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

/**
 * Controlador responsable de exponer los metodos referentes a las Ciudades 
 * @author cesar.castano
 */
@Log4j2
@RestController
@RequestMapping("${endpoints.Ciudad}")
public class CiudadesController {

	@Autowired
	ICiudadesService ciudadesService;
	
	/**
	 * Servicio encargado de retornar la consulta de todos las Ciudades
	 * 
	 * @return ResponseEntity<ApiResponseADE<List<CiudadDTO>>>
	 * @author cesar.castano
	 */
	@GetMapping(value = "${endpoints.Ciudad.consultar}")
	public ResponseEntity<ApiResponseADE<List<CiudadesDTO>>> getCiudades(@QuerydslPredicate(root = Ciudades.class) Predicate predicate) {
		List<CiudadesDTO> consulta = ciudadesService.getCiudades(predicate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(consulta, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
    @PostMapping(value = "${endpoints.Ciudad.crud}")
    public ResponseEntity<ApiResponseADE<CiudadesDTO>> postCiudad(@RequestBody CiudadesDTO ciudad) {
      log.info("Crear ciudad id: {}", ciudad.getCodigoDANE());
      CiudadesDTO ciudadDTO = ciudadesService.createCiudad(ciudad);
      log.info("Creada ciudad id: {}", ciudad.getCodigoDANE());

      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseADE<CiudadesDTO>(ciudadDTO,
              ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
	
    @PutMapping(value = "${endpoints.Ciudad.crud}")
    public ResponseEntity<ApiResponseADE<CiudadesDTO>> putCiudad(@RequestBody CiudadesDTO ciudad) {
      log.info("Actualizar ciudad id: {}", ciudad.getCodigoDANE());
      CiudadesDTO ciudadDTO = ciudadesService.updateCiudad(ciudad);
      log.info("Actualizada ciudad id: {}", ciudad.getCodigoDANE());

      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseADE<CiudadesDTO>(ciudadDTO,
              ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
	
    @DeleteMapping(value = "${endpoints.Ciudad.crud}{codigoDane}")
    public ResponseEntity<ApiResponseADE<Void>> deleteCiudad(@PathVariable String codigoDane) {
      log.info("eliminar ciudad id: {}", codigoDane);
      ciudadesService.deleteCiudad(codigoDane);
      log.info("eliminada ciudad id: {}", codigoDane);
      
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(new ApiResponseADE<Void>(null,
              ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
}
