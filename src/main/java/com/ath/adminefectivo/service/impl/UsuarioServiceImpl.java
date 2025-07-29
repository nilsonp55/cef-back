package com.ath.adminefectivo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Usuario;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.UsuarioRepository;
import com.ath.adminefectivo.service.IUsuarioService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

/**
 * Interfaz de los servicios a la tabla de usuario
 * @author bayronPerez
 */

@Service
@Log4j2
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	
	@Override
	public List<Usuario> getUsuarios(Predicate predicate) {
		var usuarios = usuarioRepository.findAll(predicate);
		return (List<Usuario>) usuarios;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Usuario getUsuarioById(String idUsuario) {
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		usuario.ifPresentOrElse(
			u -> log.debug("findById usuario: {}", idUsuario), 
			() -> {
				log.debug("No encontrado Usuario: {}", idUsuario);
				throw new NotFoundException(UsuarioServiceImpl.class.getName(), idUsuario);
			});
		return usuario.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Usuario postUsuario(Usuario usuario) throws NotFoundException {
		log.debug("crear usuario id: {}", usuario.getIdUsuario());
		Optional<Usuario> usuarioFind = usuarioRepository.findByIdUsuarioIgnoreCase(usuario.getIdUsuario());
		usuarioFind.ifPresent(u -> {
		  log.debug("Usuario existe :{}", u.getIdUsuario());
		  throw new NegocioException(ApiResponseCode.ERROR_USUARIO_EXISTE.getCode(), ApiResponseCode.ERROR_USUARIO_EXISTE.getDescription(), ApiResponseCode.ERROR_USUARIO_EXISTE.getHttpStatus());
		});
		
		usuario = usuarioRepository.save(usuario);
		log.debug("usuario creado id: {}", usuario.getIdUsuario());
		return usuario;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Usuario putUsuario(Usuario usuario) throws NotFoundException {
		log.debug("actualizar usuario id: {}", usuario.getIdUsuario());
		Optional<Usuario> usuarioFind = usuarioRepository.findByIdUsuarioIgnoreCase(usuario.getIdUsuario());
		
		log.debug("actualizar Usuario - isEmpty: {}", usuarioFind.isEmpty());
		usuarioFind.ifPresentOrElse(
				u -> {
				  usuario.setIdUsuario(u.getIdUsuario());
					usuarioRepository.save(usuario);
					log.debug("actualizar Usuario - save: {}", usuario.getIdUsuario());
				},
				() -> {
					log.debug("actualizar usuario error idUsuario: {}", usuario.getIdUsuario());
					throw new NotFoundException(UsuarioServiceImpl.class.getName(), usuario.getIdUsuario().toString());
				});

		return usuario;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteUsuario(String idUsuario) {
		log.debug("deleteUsuario - idUsuario: {}", idUsuario);
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		log.debug("deleteUsuario - isEmpty: {}", usuario.isEmpty());
		usuario.ifPresentOrElse(
				u -> usuarioRepository.delete(u),
				() -> {
					log.debug("deleteUsuario - delete error idUsuario: {}", idUsuario);
					throw new NotFoundException(UsuarioServiceImpl.class.getName(), idUsuario);
				});
	}

}
