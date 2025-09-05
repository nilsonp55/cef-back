package com.ath.adminefectivo.exception;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.dto.response.ApiResponseCode;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        // Caso por defecto, por si se agrega una excepción personalizada nueva sin configuración
        return buildErrorResponse(ApiResponseCode.GENERIC_ERROR.getCode(), ex.getMessage(), "SERVICE", HttpStatus.INTERNAL_SERVER_ERROR);
    }

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
}
