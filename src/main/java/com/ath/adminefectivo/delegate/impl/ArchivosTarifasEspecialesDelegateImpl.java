package com.ath.adminefectivo.delegate.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.delegate.IArchivosLiquidacionDelegate;
import com.ath.adminefectivo.delegate.IArchivosTarifasEspecialesDelegate;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.ArchivosTarifasEspecialesDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.compuestos.SummaryArchivoLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancoSimpleInfoRepository;
import com.ath.adminefectivo.repositories.ITransportadorasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.IArchivosTarifasEspecialesService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IRegistrosCargadosService;
import com.ath.adminefectivo.service.IValidacionArchivoService;
import com.ath.adminefectivo.service.impl.FilesServiceImpl;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ArchivosTarifasEspecialesDelegateImpl implements IArchivosTarifasEspecialesDelegate {
	
	@Autowired
    IFilesService filesService;
	
	@Autowired
	IParametroService parametrosService;
    
	@Autowired
	IValidacionArchivoService validacionArchivoService;

    @Autowired
    IMaestroDefinicionArchivoService maestroDefinicionArchivoService;
	
	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	@Autowired
	ILecturaArchivoService lecturaArchivoService;
	
	@Autowired
	IArchivosLiquidacionService archivosLiquidacionService;
	
    @Autowired
    IBancoSimpleInfoRepository bancosRepository;

    @Autowired
    ITransportadorasRepository transportadorasRepository;
    
    @Autowired
	IRegistrosCargadosService registrosCargadosService;
    
    @Autowired
	IDetalleDefinicionArchivoService detalleDefinicionArchivoService;
    
    @Autowired
    IArchivosTarifasEspecialesService archivosTarifasEspecialesService;
    
    @Autowired
    ArchivosLiquidacionDelegateImpl archivosLiquidacionDelegateImpl;
    
    @Autowired
    FilesServiceImpl filesServiceImpl;
    
    @Value("${aws.s3.active}")
    Boolean s3aws;
   	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ArchivosTarifasEspecialesDTO> getAll(int start, int end, boolean content, String fileName,
			Optional<List<ArchivosTarifasEspecialesDTO>> dtoResponseListOptional, int idOption) {

		log.info("Inicia Proceso Archivos Pendientes de carga Tarifas Especiales: Acceso AWS:{}", s3aws);
		
		String agrupador;

		switch (idOption) {
		case 1:
			agrupador = Constantes.TARIFAS_ESPECIALES_AGRUPADOR;
			break;
		case 2:
			agrupador = Constantes.TARIFAS_REGULARES_AGRUPADOR;
			break;
		default:
			throw new NegocioException(ApiResponseCode.ERROR_TIPO_CARGUE_ARCHIVO.getCode(),
	                ApiResponseCode.ERROR_TIPO_CARGUE_ARCHIVO.getDescription(),
	                ApiResponseCode.ERROR_TIPO_CARGUE_ARCHIVO.getHttpStatus());
		}
		
		List<MaestrosDefinicionArchivoDTO> maestrosDefinicion = maestroDefinicionArchivoService
				.consultarDefinicionArchivoByAgrupador(null, agrupador);

		String urlPendientes = filesService.consultarPathArchivos(Constantes.ESTADO_CARGUE_PENDIENTE);
		String url = maestrosDefinicion.get(0).getUbicacion().concat(urlPendientes);
		String extension = maestrosDefinicion.get(0).getExtension();
		String mascara = maestrosDefinicion.get(0).getMascaraArch();
		String idMaestroDefinicion = maestrosDefinicion.get(0).getIdMaestroDefinicionArchivo();
		String prefijo = mascara.substring(mascara.indexOf("[") + 1, mascara.indexOf("]"));

		List<ArchivosTarifasEspecialesDTO> dtoResponseList = obtenerDtoResponseList(start, end, content, fileName, url);

		// Filtrar lista según el prefijo
		List<ArchivosTarifasEspecialesDTO> dtoFiltrada = dtoResponseList.stream()
				.filter(dto -> dto.getNombreArchivo() != null && dto.getNombreArchivo().startsWith(prefijo))
				.collect(Collectors.toList());

		log.info("Archivos en directorio Tarifas Especiales: url:{} - cantidad:{}", url, dtoResponseList.size());

		/**
		 * Contiene la lógica para validar y filtrar una lista de archivos, verificando
		 * que cumplan con la máscara y la extensión esperada.
		 */
		List<ArchivosTarifasEspecialesDTO> responseList = archivosTarifasEspecialesService
				.filtrarPorMascara(dtoFiltrada, mascara, extension, idMaestroDefinicion, true);

		log.info("Finaliza proceso cargue de archivos Tarifas Especiales - Total procesados: {}", responseList.size());
		return new PageImpl<>(responseList);
	}

	private List<ArchivosTarifasEspecialesDTO> obtenerDtoResponseList(int start, int end, boolean content,
			String fileName, String url) {

		return inicializarDtoList(
				filesService.obtenerContenidoCarpetaSummaryS3Object(url, start, end, content, fileName), url);
	}

	private List<ArchivosTarifasEspecialesDTO> inicializarDtoList(List<SummaryArchivoLiquidacionDTO> objetos,
			String url) {

		List<ArchivosTarifasEspecialesDTO> dtoResponseList = new ArrayList<>();
		for (SummaryArchivoLiquidacionDTO archivoDTO : objetos) {
			String nombreArchivo = obtenerNombreArchivo(archivoDTO.getS3ObjectSummary().getKey());

			dtoResponseList.add(ArchivosTarifasEspecialesDTO.builder().url(url).nombreArchivo(nombreArchivo)
					.contenidoArchivo(archivoDTO.getContenidoArchivo()).nombreArchivoCompleto(nombreArchivo)
					.fechaTransferencia(archivoDTO.getS3ObjectSummary().getLastModified()).build());
		}
		return dtoResponseList;
	}

	private String obtenerNombreArchivo(String rutaCompleta) {
		int index = rutaCompleta.lastIndexOf("/");
		return (index == -1) ? rutaCompleta : rutaCompleta.substring(index + 1);
	}

	@Override
	public Page<ArchivosLiquidacionDTO> procesarAchivosTarifasEspeciales(ArchivosLiquidacionListDTO archivosProcesar) {
		List<ArchivosLiquidacionDTO> responseList = new ArrayList<>();

		archivosProcesar.getValidacionArchivo().forEach(f -> {

			ValidacionArchivoDTO resultado = archivosTarifasEspecialesService.procesarAchivoCargadoTarifaEspecial(f);

			f.setEstado(resultado.getEstadoValidacion());
			f.setIdArchivodb(resultado.getIdArchivo());

			responseList.add(f);

		});

		return new PageImpl<>(responseList);
	}

	@Override
	public Page<ArchivosLiquidacionDTO> getContentFile(ArchivosLiquidacionListDTO archivosProcesar) {
		
		int start = 0;
	    int end = 0;
	    boolean content = true;

	    // Definiciones de maestros y paths (se consultan una sola vez)
	    List<MaestrosDefinicionArchivoDTO> maestrosDefinicion = archivosLiquidacionDelegateImpl
	            .consultarMaestrosDefinicion(Constantes.TARIFAS_ESPECIALES_AGRUPADOR);

	    // Lista final de respuesta
	    List<ArchivosLiquidacionDTO> dtoResponseList = new ArrayList<>();

	    // Iterar cada archivo a procesar
	    for (ArchivosLiquidacionDTO archivo : archivosProcesar.getValidacionArchivo()) {
	    	
	        String fileName = archivo.getNombreArchivoCompleto();	        
	        String urlPendientes = filesService.consultarPathArchivos(archivo.getEstado());
		    String url = maestrosDefinicion.get(0).getUbicacion().concat(urlPendientes);

	        // Procesar archivo individual
	        List<ArchivosLiquidacionDTO> responseParcial =
	                archivosLiquidacionDelegateImpl.obtenerDtoResponseList(start, end, content, fileName, url);

	        if (responseParcial != null && !responseParcial.isEmpty()) {
	            dtoResponseList.addAll(responseParcial);
	        }
	    }

	    return new PageImpl<>(dtoResponseList);
	}

	@Override
	public boolean eliminarArchivoTarifaEspecial(ArchivosLiquidacionListDTO archivosProcesar) {
	    boolean result = true;

	    // Definiciones de maestros y paths (se consultan una sola vez)
	    List<MaestrosDefinicionArchivoDTO> maestrosDefinicion =
	            archivosLiquidacionDelegateImpl.consultarMaestrosDefinicion(Constantes.TARIFAS_ESPECIALES_AGRUPADOR);

	    if (maestrosDefinicion == null || maestrosDefinicion.isEmpty()) {
	        return false; // No hay definición -> no se puede eliminar
	    }

	    String ubicacionBase = maestrosDefinicion.get(0).getUbicacion();

	    // Iterar cada archivo a procesar
	    for (ArchivosLiquidacionDTO archivo : archivosProcesar.getValidacionArchivo()) {
	        String fileName = archivo.getNombreArchivoCompleto();	        
	        String urlPendientes = filesService.consultarPathArchivos(archivo.getEstado());
	        String url = ubicacionBase.concat(urlPendientes).concat(fileName);

	        boolean eliminado = false;

	        if (archivo.getIdArchivodb() != null && archivo.getIdArchivodb() > 0) {
	            var archivoCargadoDTO = archivosCargadosService.eliminarArchivoCargado(archivo.getIdArchivodb());
	            if (Objects.nonNull(archivoCargadoDTO)) {
	                eliminado = filesService.eliminarArchivo(url);
	            }
	        } else {
	            eliminado = filesService.eliminarArchivo(url);
	        }

	        // Si alguno falla, marcamos result en false pero seguimos con los demás
	        if (!eliminado) {
	            result = false;
	        }
	    }

	    return result;
	}
}
