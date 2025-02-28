package com.ath.adminefectivo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.service.IMenuService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("${endpoints.Menu}")
public class MenuController {

	private final IMenuService menuService;

	public MenuController(@Autowired IMenuService menuService) {
		super();
		this.menuService = menuService;
	}

	@GetMapping(value = "${endpoints.Menu.crud}")
	public ResponseEntity<ApiResponseADE<List<MenuDTO>>> getAllMenu(
			@QuerydslPredicate(root = Menu.class) Predicate predicate) {

		log.info("Predicate: {}", predicate);
		List<MenuDTO> listMenuDTO = menuService.getMenuByPredicate(predicate);
		log.debug("result list size: {}", listMenuDTO.size());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponseADE<>(listMenuDTO, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
						.description(ApiResponseCode.SUCCESS.getDescription()).build()));
	}
	
    @PostMapping(value = "${endpoints.Menu.crud}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<MenuDTO>> createMenu(@RequestBody MenuDTO menu) {
      log.info("Create Menu nombre: {}", menu.getNombre());
      menu = menuService.createMenu(menu);
      log.info("Created Menu nombre: {} - Id: {}", menu.getNombre(), menu.getIdMenu());
      return ResponseEntity.status(HttpStatus.CREATED).body(
          new ApiResponseADE<>(menu, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
              .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }

    @PutMapping(value = "${endpoints.Menu.crud}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseADE<MenuDTO>> updateMenu(@RequestBody MenuDTO menu) {
      log.info("Update Menu nombre: {}", menu.getNombre());
      menu = menuService.updateMenu(menu);
      log.info("Updated Menu nombre: {} - Id: {}", menu.getNombre(), menu.getIdMenu());
      return ResponseEntity.status(HttpStatus.OK).body(
          new ApiResponseADE<>(menu, ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
              .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
    
    @DeleteMapping(value = "${endpoints.Menu.crud}/{idMenu}")
    public ResponseEntity<ApiResponseADE<Void>> deleteMenu(@PathVariable String idMenu) {
      log.info("Delete Menu Id: {}", idMenu);
      menuService.deleteMenu(idMenu);
      log.info("Deleted Menu Id: {}", idMenu);
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(new ApiResponseADE<Void>(null,
              ResponseADE.builder().code(ApiResponseCode.SUCCESS.getCode())
                  .description(ApiResponseCode.SUCCESS.getDescription()).build()));
    }
}
