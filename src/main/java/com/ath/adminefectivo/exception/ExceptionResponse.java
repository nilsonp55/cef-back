package com.ath.adminefectivo.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase donde se personaliza la exception como deseamos enviarla en el servicio
 *
 * @author BayronPerez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

	private LocalDateTime fecha;

	private String mensaje;

	private String detalles;

}
