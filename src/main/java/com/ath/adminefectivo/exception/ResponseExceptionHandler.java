package com.ath.adminefectivo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;

/**
 * Clase encargada de capturar los errores y poderlos transformar y perzonalizar
 *
 * @author BayronPerez
 */
@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Captura y maneja las excepciones no controladas
	 * @param exception
	 * @param request
	 * @return
	 * @return ResponseEntity<ApiResponseADE<ExceptionResponse>>
	 * @author CamiloBenavides
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ApiResponseADE<ExceptionResponse>> manejarTodasExcepciones(Exception exception,
			WebRequest request) {
		exception.printStackTrace();
		return ResponseEntity.status(ApiResponseCode.GENERIC_ERROR.getHttpStatus())
				.body(new ApiResponseADE<ExceptionResponse>(null, ResponseADE.builder().code(ApiResponseCode.GENERIC_ERROR.getCode())
								.description(ApiResponseCode.GENERIC_ERROR.getDescription())
								.source(request.getDescription(false)).build()));

	}

	/**
	 * Capturar el error tipo NOT_FOUND (Status 404) y devolver mensaje
	 * personalizado
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ExceptionResponse>
	 * @author BayronPerez
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionResponse> manejarModeloNotFoundException(NotFoundException ex, WebRequest request) {
		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
	}

	/**
	 * Capturar el error tipo CONFLICT (Status 409) y devolver mensaje personalizado
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity<ExceptionResponse>
	 * @author BayronPerez
	 */
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ExceptionResponse> manejarModeloConflict(ConflictException ex, WebRequest request) {
		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(er, HttpStatus.CONFLICT);
	}



	/**
	 * Captura y maneja las excepciones de negocio
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<ApiResponseADE<ExceptionResponse>>
	 * @author CamiloBenavides
	 */
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<ApiResponseADE<ExceptionResponse>> manejarExcepcionesNegocio(NegocioException exception,
			WebRequest request) {
		exception.printStackTrace();
		return ResponseEntity.status(exception.getStatus())
				.body(new ApiResponseADE<ExceptionResponse>(null, ResponseADE.builder().code(exception.getCode())
						.description(exception.getMessage()).source(request.getDescription(false)).build()));

	}
	
	/**
	 * Captura y maneja las excepcione del aplicativo.
	 * @param exception
	 * @param request
	 * @return
	 * @return ResponseEntity<ApiResponseADE<ExceptionResponse>>
	 * @author CamiloBenavides
	 */
	@ExceptionHandler(AplicationException.class)
	public ResponseEntity<ApiResponseADE<ExceptionResponse>> manejarExcepcionesDeAplicacin(AplicationException exception,
			WebRequest request) {
		exception.printStackTrace();
		return ResponseEntity.status(exception.getStatus())
				.body(new ApiResponseADE<ExceptionResponse>(null, ResponseADE.builder().code(exception.getCode())
						.description(ApiResponseCode.GENERIC_ERROR.getDescription()).source(request.getDescription(false)).build()));

	}

}
