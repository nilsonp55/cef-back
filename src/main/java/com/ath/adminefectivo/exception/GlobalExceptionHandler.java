package com.ath.adminefectivo.exception;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ApplicationEventPublisher publisher;

    @Autowired
    public GlobalExceptionHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    // --- Manejo de validaciones ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseADE<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        return buildErrorResponse(
                ApiResponseCode.GENERIC_ERROR.getCode(),
                errorMessage,
                "VALIDATOR",
                HttpStatus.BAD_REQUEST
        );
    }

    // --- Manejo de excepciones personalizadas ---
    @ExceptionHandler({
        AplicationException.class,
        NegocioException.class,
        ConflictException.class,
        NotFoundException.class
    })
    public ResponseEntity<ApiResponseADE<Object>> handleCustomExceptions(RuntimeException ex) {
        if (ex instanceof AplicationException appEx) {
            return buildErrorResponse(appEx.getCode(), appEx.getMessage(), "APPLICATION", appEx.getStatus());
        }
        if (ex instanceof NegocioException negEx) {
            return buildErrorResponse(negEx.getCode(), negEx.getMessage(), "BUSINESS", negEx.getStatus());
        }
        if (ex instanceof ConflictException) {
            return buildErrorResponse(ApiResponseCode.CONFLICT_ERROR.getCode(), ex.getMessage(), "SERVICE", HttpStatus.CONFLICT);
        }
        if (ex instanceof NotFoundException) {
            return buildErrorResponse(ApiResponseCode.NOT_FOUND_ERROR.getCode(), ex.getMessage(), "SERVICE", HttpStatus.NOT_FOUND);
        }
        // Caso por defecto
        return buildErrorResponse(ApiResponseCode.GENERIC_ERROR.getCode(), ex.getMessage(), "SERVICE", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- Manejo global con auditoría (nuevo) ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseADE<Void>> handleGeneralException(Exception ex) {
        logger.error("Error global atrapado: {}", ex.getMessage(), ex);

        // Construir o recuperar el contexto de auditoría
        AuditData audit = AuditoriaContext.get();
        if (audit == null) {
            audit = AuditData.builder()
                    .fechaHora(LocalDateTime.now())
                    .build();
        }

        audit.setEstadoOperacion("ERROR");
        audit.setMensajeRespuesta(ex.getMessage());

        // Publicar evento para persistir auditoría con error
        try {
            publisher.publishEvent(new AuditReadyEvent(this, audit));
        } catch (Exception e) {
            logger.error("Error publicando AuditReadyEvent desde GlobalExceptionHandler: {}", e.getMessage());
        }

        ApiResponseADE<Void> body = new ApiResponseADE<>(null,
                ResponseADE.builder()
                        .code(ApiResponseCode.GENERIC_ERROR.getCode())
                        .description(ex.getMessage())
                        .source("GlobalExceptionHandler")
                        .build());

        return ResponseEntity.internalServerError().body(body);
    }

    // --- Helper privado ---
    private ResponseEntity<ApiResponseADE<Object>> buildErrorResponse(String code, String description, String source, HttpStatus status) {
        ResponseADE errorResponse = ResponseADE.builder()
                .code(code)
                .description(description)
                .source(source)
                .build();

        return ResponseEntity
                .status(status)
                .body(new ApiResponseADE<>(null, errorResponse));
    }
    
    /**
     * Encapsula excepciones lanzadas por CrudRepository dentro de un ResponseEntity y retorna un
     * mensaje de error con HttpStatus 400
     * 
     * @param exception DataAccessException
     * @param request WebRequest
     * @return ResponseEntity que contiene el mensaje de error de la excepcion lanzada por
     *         CrudRepository
     * @author prv_nparra
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponseADE<ExceptionResponse>> manejarDataAccessException(
        DataAccessException exception, WebRequest request) {
      logger.error("manejarDataAccessException: {}", exception.getMessage());

      return ResponseEntity.status(ApiResponseCode.GENERIC_ERROR.getHttpStatus())
          .body(new ApiResponseADE<ExceptionResponse>(null,
              ResponseADE.builder().code(ApiResponseCode.GENERIC_ERROR.getCode())
                  .description(ApiResponseCode.GENERIC_ERROR.getDescription())
                  .source(request.getDescription(false))
                  .errors(Arrays
                      .asList(Optional.of(exception.getRootCause()).orElse(exception).getMessage()))
                  .build()));
    }
	
    /**
     * Encapsula excepciones lanzadas por CrudRepository dentro de un ResponseEntity y retorna un
     * mensaje de error con HttpStatus 400
     * 
     * @param exception DataIntegrityViolationException
     * @param request WebRequest
     * @return ResponseEntity que contiene el mensaje de error de la excepcion lanzada por
     *         CrudRepository
     * @author prv_nparra
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseADE<ExceptionResponse>> manejarDataIntegrityViolationException(
        DataIntegrityViolationException exception, WebRequest request) {
      logger.error("manejarDataIntegrityViolationException: {}", exception.getMessage());
      return ResponseEntity.status(ApiResponseCode.GENERIC_ERROR.getHttpStatus())
          .body(new ApiResponseADE<ExceptionResponse>(null,
              ResponseADE.builder().code(ApiResponseCode.GENERIC_ERROR.getCode())
                  .description(ApiResponseCode.GENERIC_ERROR.getDescription())
                  .source(request.getDescription(false))
                  .errors(Arrays
                      .asList(Optional.of(exception.getRootCause()).orElse(exception).getMessage()))
                  .build()));
    }
}
