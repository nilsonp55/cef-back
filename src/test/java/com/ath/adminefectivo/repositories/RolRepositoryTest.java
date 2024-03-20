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

import com.ath.adminefectivo.entities.Rol;

/**
 * @author nilsonparra
 * 
 */

@DataJpaTest
class RolRepositoryTest {
	
	@Autowired
	private RolRepository rolRepository;

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
	}
	
	@DisplayName("Crear objeto Rol devolver rol creado")
	@Test
	void givenRolObjectWhenSaveThenReturnRolSaved() {		
		// given - precondition or setup
		// Objeto this.rol 
		
		// when -  action or the behaviour that we are going test
		Rol rolCreado = rolRepository.save(rol);
		
		// then - verify the output
		assertThat(rolCreado).isNotNull();
		assertThat(rolCreado.getIdRol()).isNotEmpty();
	}
	
	@DisplayName("Obtener todos los objetos Rol de FindAll()")
	@Test
	void givenRolListWhenFindAllThenRolList(){		
		// given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		List<Rol> listOfRolesFindAll = rolRepository.findAll();
		
		// then - verify the output
		assertThat(listOfRolesFindAll).hasSize(1);		
	}
	
	@DisplayName("Buscar por IdRol y retornar objeto Rol")
	@Test
	void givenRolObjectWhenFindByIdThenReturnRolObject() {
		// given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		Rol rolFindById = rolRepository.findById(rol.getIdRol()).get();
		
		// then - verify the output
		assertThat(rolFindById).isNotNull();
		assertThat(rolFindById.getIdRol()).isEqualTo(rol.getIdRol());
	}
	
	@DisplayName("Actualizar objeto rol y retornar rol actualizado")
	@Test
    void givenRolObjectWhenUpdateRolThenReturnUpdatedRol(){		
		// given - precondition or setup
		Date fechaCreacion = Date.from(Instant.now());
		Date fechaModificacion = Date.from(Instant.now());
		
		// when -  action or the behaviour that we are going test
		Rol rolFind = rolRepository.findById(rol.getIdRol()).get();
		rolFind.setDescripcion("Nueva descripcion");
		rolFind.setEstado("I");
		rolFind.setFechaCreacion(fechaCreacion);
		rolFind.setFechaModificacion(fechaModificacion);
		rolFind.setNombre("Nuevo Nombre");
		rolFind.setUsuarioCreacion("usuario creacion");
		rolFind.setUsuarioModificacion("usuario modificacion");
		
		Rol rolSaved = rolRepository.save(rolFind);
				
		// then - verify the output
		assertThat(rolSaved).isNotNull();
		assertThat(rolSaved.getDescripcion()).isEqualTo(rolFind.getDescripcion());
		assertThat(rolSaved.getEstado()).isEqualTo(rolFind.getEstado());
		assertThat(rolSaved.getFechaCreacion()).isEqualTo(rolFind.getFechaCreacion());
		assertThat(rolSaved.getFechaModificacion()).isEqualTo(rolFind.getFechaModificacion());
		assertThat(rolSaved.getNombre()).isEqualTo(rolFind.getNombre());
		assertThat(rolSaved.getUsuarioCreacion()).isEqualTo(rolFind.getUsuarioCreacion());
		assertThat(rolSaved.getUsuarioModificacion()).isEqualTo(rolFind.getUsuarioModificacion());
	}
	
	@DisplayName("Eliminar objeto rol y validar que no existe")
	@Test
    void givenRolObjectWhenDeleteThenRemoveRol(){
		// given - precondition or setup
				
		// when -  action or the behaviour that we are going test
		rolRepository.delete(rol);
		Optional<Rol> rolFind = rolRepository.findById(rol.getIdRol());
		
		// then - verify the output
		assertThat(rolFind).isEmpty();
	}
	
}
