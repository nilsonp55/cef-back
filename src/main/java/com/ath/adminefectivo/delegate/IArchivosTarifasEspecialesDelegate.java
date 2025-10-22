package com.ath.adminefectivo.delegate;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.ArchivosTarifasEspecialesDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosTarifasEspecialesListDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IArchivosTarifasEspecialesDelegate {
    /**
     * Delegate responsable de consultar los ArchivosCargados del sistema, por filtros y paginador
     * @return List<ArchivosTarifasEspecialesDTO>
     * @author jorge.trespalacios
     */
	Page<ArchivosTarifasEspecialesDTO> getAll(int start, int end, boolean content, String fileName, Optional<List<ArchivosTarifasEspecialesDTO>> dtoResponseListOptional, int idOption);
	
	Page<ArchivosLiquidacionDTO> procesarAchivosTarifasEspeciales(ArchivosLiquidacionListDTO archivosProcesar);
	
	Page<ArchivosLiquidacionDTO> getContentFile(ArchivosLiquidacionListDTO archivosProcesar);
	
	boolean eliminarArchivoTarifaEspecial(ArchivosLiquidacionListDTO archivosProcesar);


}