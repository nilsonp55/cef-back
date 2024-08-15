package com.ath.adminefectivo.delegate;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

public interface IArchivosLiquidacionDelegate {
    /**
     * Delegate responsable de consultar los ArchivosCargados del sistema, por filtros y paginador
     * @return List<ArchivosLiquidacionDTO>
     * @author hector.mercado
     */
	Page<ArchivosLiquidacionDTO> getAll(int start, int end, boolean content, String fileName, Optional<List<ArchivosLiquidacionDTO>> dtoResponseListOptional);

	ArchivosLiquidacionListDTO eliminarArchivo(ArchivosLiquidacionListDTO archivosLiquidacion);
    
    Page<ArchivosLiquidacionDTO> procesarAchivos(ArchivosLiquidacionListDTO archivosProcesar);
    
    void procesarAchivoCargado(ArchivosLiquidacionDTO archivoProcesar);
    
    ValidacionArchivoDTO consultarDetalleError(Long idArchivoCargado);
    
    List<RegistrosCargadosDTO> consultarDetalleArchivo(Long idArchivoCargado);
}
