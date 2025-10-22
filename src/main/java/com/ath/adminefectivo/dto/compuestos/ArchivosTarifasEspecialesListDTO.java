package com.ath.adminefectivo.dto.compuestos;

import java.util.List;
import com.ath.adminefectivo.dto.ArchivosTarifasEspecialesDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO tipo lista para el transporte de informacion correspondiente a los archivos de Tarifas especiales
 *
 * @author johan.chaparro
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivosTarifasEspecialesListDTO {
	
	private List<ArchivosTarifasEspecialesDTO> validacionArchivo;
}
