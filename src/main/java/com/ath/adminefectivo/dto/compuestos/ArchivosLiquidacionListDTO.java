package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO tipo lista para el transporte de informacion correspondiente a los archivos de liquidacion pendientes de carga
 *
 * @author johan.chaparro
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivosLiquidacionListDTO {
	
	private List<ArchivosLiquidacionDTO> validacionArchivo;
}
