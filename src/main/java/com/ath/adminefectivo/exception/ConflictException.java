package com.ath.adminefectivo.exception;

import lombok.Getter;
import lombok.Setter;


/**
 * cepcion para peticiones con datos incorrectos.
 *
 * @author BayronPerez
 */
@Getter
@Setter
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = -8556490604693291788L;

	/**
	 * Constructor de la clase
	 * 
	 * @param mensaje
	 */
	public ConflictException(String mensaje) {
		super(mensaje);
	}
}

