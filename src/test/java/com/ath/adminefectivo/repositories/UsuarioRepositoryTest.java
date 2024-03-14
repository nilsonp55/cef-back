package com.ath.adminefectivo.repositories;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Usuario;

/**
 * @author nilsonparra
 */

@DataJpaTest
class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepositoryTest;
	
	private Usuario usuario;
	
	@BeforeEach
	void setup() {
						
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
			.build();
	}
	
	@DisplayName("Crear usuario nuevo devolver usuario creado")
	@Test
	void givenUsuarioObject_whenSave_thenReturnUsuarioSaved() {
		
		// given - precondition or setup
		// Objeto this.usuario 
				
		
		// when -  action or the behaviour that we are going test
		Usuario usuarioGuardado = usuarioRepositoryTest.save(usuario);
		
		// then - verify the output
		assertThat(usuarioGuardado).isNotNull();
		assertThat(usuarioGuardado.getIdUsuario()).isNotEmpty();
		
	}

}
