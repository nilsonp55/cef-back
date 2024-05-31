package com.ath.adminefectivo.exception;

/**
 * Excepcion para peticiones con datos incorrectos.
 *
 * @author BayronPerez
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7500322046753778196L;

	/**
	 * Constructor de la clase
	 * @param code
	 */
	public NotFoundException(String mensaje) {
		super(mensaje);
	}

	/**
	 * Constructor de la clase
	 * @param code
	 */
	public NotFoundException(String recurso, String id) {
		super(String.format("No se encontró registro(s) de %s con el ID %s", recurso, id));
	}
}
