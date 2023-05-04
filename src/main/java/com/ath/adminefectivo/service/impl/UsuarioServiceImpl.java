package com.ath.adminefectivo.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Usuario;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.UsuarioRepository;
import com.ath.adminefectivo.service.IUsuarioService;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios a la tabla de usuario
 * @author bayronPerez
 */

@Service
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
		var cuentas = usuarioRepository.findById(idUsuario);
		if (Objects.isNull(cuentas)) {
			throw new AplicationException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getCode(),
					ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription(),
					ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getHttpStatus());
		} 
		return cuentas.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Usuario postUsuario(Usuario usuario) {
		if (usuario.getIdUsuario() != null && usuarioRepository
				.existsById(usuario.getIdUsuario())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_EXIST.getDescription());		
		}
		return usuarioRepository.save(usuario);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Usuario putUsuario(Usuario usuario) {
		if (usuario.getIdUsuario() == null && !usuarioRepository
				.existsById(usuario.getIdUsuario())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		return usuarioRepository.save(usuario);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteUsuario(String idUsuario) {
		if (idUsuario == null && !usuarioRepository
				.existsById(idUsuario)) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		usuarioRepository.deleteById(idUsuario);
	}

}
