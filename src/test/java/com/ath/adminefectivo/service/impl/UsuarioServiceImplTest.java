package com.ath.adminefectivo.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ath.adminefectivo.repositories.UsuarioRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() throws Exception {
        log.info("setUp :{}");
    }
 
    @Test
    void testDeteleUsuario() {
        doNothing().when(this.usuarioRepository).deleteById(any());;
        this.usuarioService.deleteUsuario(any());

        log.info("Deleted usuario Test.");
    }
}