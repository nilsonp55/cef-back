package com.ath.adminefectivo.delegate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;

public interface IGestionArchivosLiquidacionDelegate {

	Page<ArchivosLiquidacionDTO> getAll(String agrupador, Pageable page);
	
	Page<ArchivosLiquidacionDTO> aceptarArchivos(ArchivosLiquidacionListDTO archivosProcesar);
	
	ArchivosLiquidacionDTO aceptarAchivo(ArchivosLiquidacionDTO archivoAceptar);
}
