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
		
		Rol rol = Rol.builder()
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
		
		usuario = Usuario.builder()
			.idUsuario("1")
			.apellidos("Sarmiento")
			.nombres("Carlos")
			.tipoUsario("tecnico")
			.estado("1")
			.fechaCreacion( Date.from(Instant.now()) )
			.fechaModificacion( Date.from(Instant.now()) )
			.usuarioCreacion("user1")
			.usuarioModificacion("user1")
			.rol(rol)
			.build();
		
		usuarioRepository.save(usuario);
	}
	
	@DisplayName("Crear usuario nuevo devolver usuario creado")
	@Test
	void givenUsuarioObject_whenSave_thenReturnUsuarioSaved() {		
		// given - precondition or setup				
		
		// when -  action or the behaviour that we are going test
		Usuario usuarioGuardado = usuarioRepository.save(usuario);
		
		// then - verify the output
		assertThat(usuarioGuardado).isNotNull();
		assertThat(usuarioGuardado.getIdUsuario()).isNotEmpty();
		
	}
	
	@DisplayName("Obtener todos los objetos Usuario de FindAll()")
	@Test
	void givenUsuarioList_whenFindAll_thenUsuarioList(){
		// given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		List<Usuario> usuarioList = usuarioRepository.findAll();
		
		// then - verify the output
		assertThat(usuarioList).hasSize(1);
	}
	
	@DisplayName("Buscar por IdUsuario y retornar objeto Usuario")
	@Test
	void givenUsuarioObject_whenFindById_thenReturnUsuarioObject() {
		//given - precondition or setup		
		
		// when -  action or the behaviour that we are going test
		Optional<Usuario> usuarioFind = usuarioRepository.findById(usuario.getIdUsuario());
		
		// then - verify the output
		assertThat(usuarioFind).isNotEmpty();
		assertThat(usuarioFind.get().getIdUsuario()).isEqualTo(usuario.getIdUsuario());
		
	}
	
	@DisplayName("Actualizar objeto Usuario y retornar usuario actualizado")
	@Test
    void givenUsuarioObject_whenUpdateUsuario_thenReturnUpdatedUsuario(){
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
		usuarioFind.setTipoUsario("test");
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
		assertThat(usuarioSaved.getTipoUsario()).isEqualTo(usuarioFind.getTipoUsario());
		assertThat(usuarioSaved.getUsuarioCreacion()).isEqualTo(usuarioFind.getUsuarioCreacion());
		assertThat(usuarioSaved.getUsuarioModificacion()).isEqualTo(usuarioFind.getUsuarioModificacion());
	}
	
	@DisplayName("Eliminar objeto usuario y validar que no existe")
	@Test
    void givenUsuarioObject_whenDelete_thenRemoveUsuario(){
		//given - precondition or setup
		
		// when -  action or the behaviour that we are going test
		usuarioRepository.delete(usuario);
		Optional<Usuario> usuarioFind = usuarioRepository.findById(usuario.getIdUsuario());
		
		// then - verify the output
		assertThat(usuarioFind).isEmpty();
	}

}
