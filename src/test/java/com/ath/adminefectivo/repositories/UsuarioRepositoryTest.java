package com.ath.adminefectivo.repositories;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Rol;
import com.ath.adminefectivo.entities.Usuario;


/**
 * @author nilsonparra
 */

@DataJpaTest
class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	private Usuario usuario;
	
	@BeforeEach
	void setup() {
		
		Rol rol = new Rol();
		rol.setIdRol("1");
		rol.setDescripcion("Rol1");
		rol.setEstado("A");
		rol.setFechaCreacion(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		rol.setFechaModificacion(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
		rol.setNombre("ROL1");
		rol.setUsuarioCreacion("user1");
		rol.setUsuarioModificacion("user1");
		rolRepository.save(rol);
		
		usuario = Usuario.builder()
			.idUsuario("1")
			.apellidos("Sarmiento")
			.nombres("Carlos")
			.tipoUsuario("tecnico")
			.estado("1")
			.fechaCreacion( Date.from(Instant.now()) )
			.fechaModificacion( Date.from(Instant.now()) )
			.usuarioCreacion("user12")
			.usuarioModificacion("user13")
			.rol(rol)
			.build();
		
		usuarioRepository.save(usuario);
	}
	
	@DisplayName("Crear usuario nuevo devolver usuario creado")
	@Test
	void givenUsuarioObjectWhenSaveThenReturnUsuarioSaved() {		
		// given - precondition or setup				
		
		// when -  action or the behaviour that we are going test
		Usuario usuarioGuardado = usuarioRepository.save(usuario);
		
		// then - verify the output
		assertThat(usuarioGuardado).isNotNull();
		assertThat(usuarioGuardado.getIdUsuario()).isNotEmpty();
		
	}
	
	@DisplayName("Obtener todos los objetos Usuario de FindAll()")
	@Test
	void givenUsuarioListWhenFindAllThenUsuarioList(){
		// given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		List<Usuario> usuarioList = usuarioRepository.findAll();
		
		// then - verify the output
		assertThat(usuarioList).hasSize(1);
	}
	
	@DisplayName("Buscar por IdUsuario y retornar objeto Usuario")
	@Test
	void givenUsuarioObjectWhenFindByIdThenReturnUsuarioObject() {
		//given - precondition or setup		
		
		// when -  action or the behaviour that we are going test
		Optional<Usuario> usuarioFind = usuarioRepository.findById(usuario.getIdUsuario());
		
		// then - verify the output
		assertThat(usuarioFind).isNotEmpty();
		assertThat(usuarioFind.get().getIdUsuario()).isEqualTo(usuario.getIdUsuario());
		
	}
	
	@DisplayName("Actualizar objeto Usuario y retornar usuario actualizado")
	@Test
    void givenUsuarioObjectWhenUpdateUsuarioThenReturnUpdatedUsuario(){
		//given - precondition or setup
		Date fechaCreacion = Date.from(Instant.now());
		Date fechaModificacion = Date.from(Instant.now());
		Usuario usuarioFind = usuarioRepository.findById(usuario.getIdUsuario()).get();
		
		// when -  action or the behaviour that we are going test
		usuarioFind.setApellidos("apellidoTest");
		usuarioFind.setEstado("A");
		usuarioFind.setFechaCreacion(fechaCreacion);
		usuarioFind.setFechaModificacion(fechaModificacion);
		usuarioFind.setNombres("Nombres Test");
		usuarioFind.setTipoUsuario("test");
		usuarioFind.setUsuarioCreacion("user2");
		usuarioFind.setUsuarioModificacion("user3");
		Usuario usuarioSaved = usuarioRepository.save(usuarioFind);
		
		// then - verify the output
		assertThat(usuarioSaved).isNotNull();
		assertThat(usuarioSaved.getApellidos()).isEqualTo(usuarioFind.getApellidos());
		assertThat(usuarioSaved.getEstado()).isEqualTo(usuarioFind.getEstado());
		assertThat(usuarioSaved.getFechaCreacion()).isEqualTo(usuarioFind.getFechaCreacion());
		assertThat(usuarioSaved.getFechaModificacion()).isEqualTo(usuarioFind.getFechaModificacion());
		assertThat(usuarioSaved.getNombres()).isEqualTo(usuarioFind.getNombres());
		assertThat(usuarioSaved.getTipoUsuario()).isEqualTo(usuarioFind.getTipoUsuario());
		assertThat(usuarioSaved.getUsuarioCreacion()).isEqualTo(usuarioFind.getUsuarioCreacion());
		assertThat(usuarioSaved.getUsuarioModificacion()).isEqualTo(usuarioFind.getUsuarioModificacion());
	}
	
	@DisplayName("Eliminar objeto usuario y validar que no existe")
	@Test
    void givenUsuarioObjectWhenDeleteThenRemoveUsuario(){
		//given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		usuarioRepository.delete(usuario);
		Optional<Usuario> usuarioFind = usuarioRepository.findById(usuario.getIdUsuario());
		
		// then - verify the output
		assertThat(usuarioFind).isEmpty();
	}

}
