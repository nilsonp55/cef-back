package com.ath.adminefectivo.delegate;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import org.springframework.data.domain.Page;

public interface IArchivosLiquidacionDelegate {
    /**
     * Delegate responsable de consultar los ArchivosCargados del sistema, por filtros y paginador
     * @return List<ArchivosCargadosDTO>
     * @author CamiloBenavides
     */
	Page<ArchivosLiquidacionDTO> getAll(int start, int end, boolean content, String fileName);

    boolean eliminarArchivo(Long idArchivo);
    
    Page<ArchivosLiquidacionDTO> procesarAchivos(ArchivosLiquidacionListDTO archivosProcesar);
    
    void ProcesarAchivoCargado(ArchivosLiquidacionDTO archivoProcesar);
}
