package com.ath.adminefectivo.delegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.delegate.IFilesDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IGeneralRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IMotorReglasService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IValidacionArchivoService;

/**
 * Delegate responsable del manejo, consulta y persistencia de archivos
 *
 * @author CamiloBenavides
 */
@Service
public class FilesDelegateImpl implements IFilesDelegate {

	@Autowired
	IFilesService filesService;

	@Autowired
	IArchivosCargadosService archivosCargadosService;

	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

	@Autowired
	IParametroService parametroService;

	@Autowired
	IValidacionArchivoService validacionArchivoService;
	
	@Autowired
	IMotorReglasService motorReglasService;
 
	@Autowired
	IGeneralRepository generalRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean persistirArchvos(MultipartFile[] files) {

		if (files.length >= 3) {
			throw new NegocioException(ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getCode(),
					ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getDescription(),
					ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getHttpStatus());
		}
		return filesService.persistirArchvos(Arrays.asList(files));
	}

	@Override
	public Boolean persistirArchvoCargado(MultipartFile file) {
		var url = filesService.persistirArchvo(file);
		ArchivosCargadosDTO archivo = ArchivosCargadosDTO.builder().nombreArchivo(file.getOriginalFilename())
				.fechaInicioCargue(new Date()).estado(Constantes.REGISTRO_ACTIVO).contentType(file.getContentType())
				.estadoCargue(Constantes.ESTADO_CARGUE_PENDIENTE).url(url).build();

		archivosCargadosService.guardarArchivos(Arrays.asList(archivo));

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DownloadDTO downloadFile(Long idArchivo) {
		ArchivosCargadosDTO archivo = archivosCargadosService.consultarArchivo(idArchivo);
		DownloadDTO file = DownloadDTO.builder().name(archivo.getNombreArchivo()).url(archivo.getUrl()).build();

		return filesService.downloadFile(file);
	}

	@Override
	public List<ArchivosCargadosDTO> consultarArchivos(String idMaestroDefinicion, String estado) {
		System.out.println("Entro al delegate");
		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroDefinicion);

		var urlPendinetes = filesService.consultarPathArchivos(estado);
		System.out.println("**********"+urlPendinetes);
		var url = maestroDefinicion.getUbicacion().concat(urlPendinetes);
		System.out.println("*******************************"+url);
		var archivos = filesService.obtenerContenidoCarpeta(url);
		System.out.println("*******************************"+archivos);
		return organizarDataArchivos(archivos, estado, idMaestroDefinicion, maestroDefinicion.getMascaraArch());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean manejoExcepciones(Integer numeroExcepcion) {

		if (numeroExcepcion == 1) {
			throw new NegocioException(ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getCode(),
					ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getDescription(),
					ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getHttpStatus());
		} else if (numeroExcepcion == 2) {
			throw new AplicationException(ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getCode(),
					ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getDescription(),
					ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getHttpStatus());
		} else if (numeroExcepcion == 3) {
			throw new ConflictException(ApiResponseCode.ERROR_LIMITE_ARCHIVOS.getDescription());
		} else if (numeroExcepcion == 4) {
			generalRepository.ejecutarQueryNativa("SELECT * FROM PARAMETRO");
			
		}else if (numeroExcepcion == 7) {
			return generalRepository.ejecutarQueryNativa("select case count(1) "
					+ "when 1 then true "
					+ "else false "
					+ "end "
					+ "from puntos a, Bancos b "
					+ "where tipo_punto = 'BANCO' "
					+ "and a.codigo_punto = b.codigo_punto "
					+ "and ( (b.es_aval = true and b.abreviatura = :parameter ) "
					+ "or a.NOMBRE_PUNTO = :parameter ) ", "NO_EXISTE_JEJE");
			
		}
		

		return true;
	}

	/**
	 * Método encargado de organizar la lista de archivos y armar el objeto de
	 * archivos cargados
	 * 
	 * @param archivos
	 * @return
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	private List<ArchivosCargadosDTO> organizarDataArchivos(List<String> archivos, String estado,
			String idModeloArchivo, String mascaraArchivo) {
				System.out.println("Entro a Organizar Data Archivos");
		List<ArchivosCargadosDTO> archivosCargados = new ArrayList<>();
		archivos.forEach(x -> archivosCargados
				.add(ArchivosCargadosDTO.builder().estadoCargue(estado).idModeloArchivo(idModeloArchivo)
						.nombreArchivo(x).fechaArchivo(validacionArchivoService.obtenerFechaArchivo(x, mascaraArchivo)).build()));

		archivosCargados.sort(Comparator.comparing(ArchivosCargadosDTO::getFechaArchivo,
				Comparator.nullsLast(Comparator.naturalOrder())));
				System.out.println("***********"+archivosCargados);
		return archivosCargados;

	}

	@Override
	public DownloadDTO descargarArchivo(String nombreArchivo, String idMaestroArchivo) {
		DownloadDTO file = null;
		try {
			var maestrosDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroArchivo);
			String carpeta = parametroService.valorParametro("RUTA_ARCHIVOS_PENDIENTES");
			file = DownloadDTO.builder().name(nombreArchivo).url(maestrosDefinicion.getUbicacion()+carpeta+nombreArchivo).build();
		} catch (Exception e) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());

		}

		return filesService.downloadFile(file);
	}

}
