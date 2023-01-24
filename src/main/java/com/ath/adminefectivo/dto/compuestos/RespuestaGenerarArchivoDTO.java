package com.ath.adminefectivo.dto.compuestos;
import java.io.ByteArrayOutputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Bancos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaGenerarArchivoDTO {

	private String nombreArchivo;
	
	private ByteArrayOutputStream archivoBytes;
	
	
}
