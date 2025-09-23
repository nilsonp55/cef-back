package com.ath.adminefectivo.exception;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Clase donde se personaliza la exception de negocio
 *
 * @author CamiloBenavides
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
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
	
	private List<String> errors = new ArrayList<>();

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
		this.errors.add(error);
	}
	
	

}
