package com.ath.adminefectivo.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase donde se personaliza la exception de negocio
 *
 * @author CamiloBenavides
 */
@Getter
@Setter
public class NegocioException extends RuntimeException {

	/**
	 * Contains the logger
	 * */
	private static final Logger logger = LogManager.getLogger(NegocioException.class);

	/*
	* Contains the serial version value
	* @author CamiloBenavides
	*/
	private static final long serialVersionUID = -4202662219335496160L;

	private final String code;
	
	private final HttpStatus status;

	private final String message;

	/**
	 * Constructor
	 * 
	 * @param code
	 * @param message
	 * @param status
	 * @author CamiloBenavides
	 */
	public NegocioException(String code, String message, HttpStatus status) {
		logger.error(message);
		this.code = code;
		this.message = message;
		this.status = status;
	}
	
	/**
	 * Constructor
	 * 
	 * @param code
	 * @param message
	 * @param status
	 * @param error
	 * @author CamiloBenavides
	 */
	public NegocioException(String code, String message, HttpStatus status, String error) {
		logger.error("{} - {}", error, message);
		this.code = code;
		this.message = message;
		this.status = status;
	}

}
