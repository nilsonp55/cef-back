package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de contener la informacion del archivo en le bucket S3 y el contenido de cada archivo
 * 
 * @author prv_jchaparro
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryArchivoLiquidacionDTO {
	
	private S3ObjectSummary s3ObjectSummary;
    private List<String> contenidoArchivo;
}
