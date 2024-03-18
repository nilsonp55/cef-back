package com.ath.adminefectivo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * Clase que contiene la estructura de los archivos Liquidación
 * @author juan.ortizt
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivosLiquidacionDTO {

	    private Long idArchivo;

	    private Date fechaArchivo;

	    private String estado;

	    private String banco;

	    private String tdv;

	    private Date fechaTransferencia;

	    private String tipoArchivo;

	    private String url;

	    private String nombreArchivo;
	    
	    private String idMaestroArchivo;
	    
	    private String nombreArchivoCompleto;
	    
	    private Long idArchivodb;
	        
	    private List<String> contenidoArchivo;      

}
