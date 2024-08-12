package com.ath.adminefectivo.delegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IArchivosLiquidacionDelegate;
import com.ath.adminefectivo.delegate.IFilesDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.DownloadGestionArchivosDTO;
import com.ath.adminefectivo.dto.GestionArchivosDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ICostosTransporteRepository;
import com.ath.adminefectivo.repositories.IGeneralRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
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
	IGeneralRepository generalRepository;
	
	@Autowired
	IArchivosLiquidacionDelegate archivosLiquidacion;
	
	@Autowired
	ICostosTransporteRepository costosTransporteRepository;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean persistirArchvoCargado(MultipartFile file) {
		
		var url = filesService.persistirArchvo(file);
		ArchivosCargadosDTO archivo = ArchivosCargadosDTO.builder().nombreArchivo(file.getOriginalFilename())
				.nombreArchivoUpper(file.getOriginalFilename().toUpperCase())
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
		DownloadDTO file = DownloadDTO.builder().name(archivo.getNombreArchivo())
												.url(archivo.getUrl()).build();
		return filesService.downloadFile(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargadosDTO> consultarArchivos(String idMaestroDefinicion, String estado) {

		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(
				idMaestroDefinicion);
		var urlPendinetes = filesService.consultarPathArchivos(estado);
		var url = maestroDefinicion.getUbicacion().concat(urlPendinetes);
		var archivos = filesService.obtenerContenidoCarpeta(url);
		return organizarDataArchivos(archivos, estado, idMaestroDefinicion, 
				maestroDefinicion.getMascaraArch());
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
	 * {@inheritDoc}
	 */
	@Override
	public DownloadDTO descargarArchivo(String nombreArchivo, String idMaestroArchivo) {
		DownloadDTO file = null;
		String carpeta = "";

		var maestrosDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroArchivo);
		carpeta = parametroService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		String ubicacion = maestrosDefinicion.getUbicacion();
		file = DownloadDTO.builder().name(nombreArchivo).url(ubicacion + carpeta + nombreArchivo).build();
		file = filesService.downloadFile(file);
		
		return file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DownloadDTO descargarArchivoProcesado(Long idArchivo) {
		
		DownloadDTO file = null;
		String carpeta = "";
		String nombreArchivo ="";

		var archivosCargados = archivosCargadosService.consultarArchivo(idArchivo);
		if (!Objects.isNull(archivosCargados)) {
			String[] arregloNombre = archivosCargados.getNombreArchivo().split(Constantes.EXPRESION_REGULAR_PUNTO);
			nombreArchivo = arregloNombre[0].concat("-" + idArchivo.toString());
			var maestrosDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(
										archivosCargados.getIdModeloArchivo());
			String ubicacion = maestrosDefinicion.getUbicacion();
			if (Constantes.ESTADO_CARGUE_ERROR.equals(archivosCargados.getEstadoCargue() )) {
				carpeta = parametroService.valorParametro(Parametros.RUTA_ARCHIVOS_ERRADOS);
			}
			else {
				carpeta = parametroService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);
			}	
			file = DownloadDTO.builder().name(nombreArchivo)
										.url(ubicacion + carpeta + nombreArchivo + "." + arregloNombre[1]).build();
			file = filesService.downloadFile(file);
		}

		return file;
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

		List<ArchivosCargadosDTO> archivosCargados = new ArrayList<>();
		archivos.forEach(x -> archivosCargados
				.add(ArchivosCargadosDTO.builder().estadoCargue(estado).idModeloArchivo(idModeloArchivo)
						.nombreArchivo(x).fechaArchivo(validacionArchivoService.obtenerFechaArchivo(x, mascaraArchivo)).build()));

		archivosCargados.sort(Comparator.comparing(ArchivosCargadosDTO::getFechaArchivo,
				Comparator.nullsLast(Comparator.naturalOrder())));
		return archivosCargados;
	}
	
	/**
	 * Este método se encarga de consultar los registros contenidos en los archivos de liquidación.
	 * Los resultados de la consulta se devuelven como un InputStream, permitiendo así su descarga.
	 * En caso de que los archivos contengan errores, el método implementa una lógica especial que 
	 * añade los detalles de dichos errores en la línea correspondiente.
	 * 
	 * @param idArchivoCargado
	 * @return DownloadDTO
	 * @author johan.chaparro
	 */
	public DownloadDTO descargarArchivoLiqProcesado(Long idArchivoCargado) {

		DownloadDTO downloadDTO = new DownloadDTO();		
		List<RegistrosCargadosDTO> listaRegistros = archivosLiquidacion.consultarDetalleArchivo(idArchivoCargado);

		if (!listaRegistros.isEmpty()) {

			ArchivosCargados archivosCargados = archivosCargadosService.consultarArchivoById(idArchivoCargado);

			if (Constantes.ESTADO_CARGUE_ERROR.equals(archivosCargados.getEstadoCargue())) {

				obtenerErrores(listaRegistros, idArchivoCargado);
			}

			downloadDTO.setId(idArchivoCargado);
			downloadDTO.setName(archivosCargados.getNombreArchivo());
			downloadDTO.setFile(obtenerContenidos(listaRegistros)); // InputStream del archivo	
						
		} else {
			
			throw new NegocioException(ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getCode(),
			          ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getDescription(),
			          ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getHttpStatus());
		}

		return downloadDTO;
	}
	
	/**
	 * Este método se encarga de consultar los registros contenidos en los archivos de liquidación.
	 * Los resultados de la consulta se devuelven como un InputStream, permitiendo así su descarga.
	 * En caso de que los archivos contengan errores, el método implementa una lógica especial que 
	 * añade los detalles de dichos errores en la línea correspondiente.
	 * 
	 * @param idArchivoCargado
	 * @return DownloadDTO
	 * @author johan.chaparro
	 */
	public DownloadGestionArchivosDTO descargarGestionArchivosLiq(GestionArchivosDTO Archivos) {

		DownloadDTO downloadDTO = new DownloadDTO();
		DownloadGestionArchivosDTO downloadGestionArchivosDTO = new DownloadGestionArchivosDTO();
		List<Long> idArchivos = Archivos.getIdArchivos();

        if (idArchivos.isEmpty()) {
            return downloadGestionArchivosDTO;
        }

        // Si la lista contiene solo un elemento, retornar el resultado del metodo
        if (idArchivos.size() == 1) {
        	downloadDTO = descargarArchivoLiqProcesado(idArchivos.get(0));
        	return convertToDownloadBase64(downloadDTO);
        }

        // Si la lista contiene más de un elemento, crear un archivo .zip
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (Long idArchivo : idArchivos) {
                DownloadDTO archivoDTO = descargarArchivoLiqProcesado(idArchivo);
                buildZip(zipOutputStream, archivoDTO);
            }
        } catch (IOException e) {
            // Manejar la excepción de manera adecuada
            e.printStackTrace();
        }

        // Configurar el DownloadDTO para el archivo .zip
        downloadDTO.setFile(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        downloadDTO.setName("RemisionArchivosLiquidacionTDVs.zip");

        return convertToDownloadBase64(downloadDTO);
	}
	
	// BORRAR HENRY
	public void saveFileFromDTO(DownloadGestionArchivosDTO downloadGestionArchivosDTO) {
		
	    try {
	        // Decodificar la cadena Base64 a un arreglo de bytes
	        byte[] fileBytes = Base64.getDecoder().decode(downloadGestionArchivosDTO.getFile());

	        // Crear un objeto File para la ruta en el disco
	        File file = Paths.get("C:\\Users\\henry.montoya\\Documents\\Test\\RemisionArchivosLiquidacionTDVs.zip").toFile();

	        // Escribir el arreglo de bytes en el archivo
	        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
	            fileOutputStream.write(fileBytes);
	        }
	    } catch (IOException e) {
	        // Manejar la excepción de manera adecuada
	        e.printStackTrace();
	    }
	}
	    	
	private void buildZip(ZipOutputStream zipOutputStream, DownloadDTO archivoDTO) throws IOException {
        ZipEntry zipEntry = new ZipEntry(archivoDTO.getName());
        zipOutputStream.putNextEntry(zipEntry);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = archivoDTO.getFile().read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, len);
        }
        zipOutputStream.closeEntry();
    }
	
	public DownloadGestionArchivosDTO convertToDownloadBase64(DownloadDTO downloadDTO) {
	    // Convertir InputStream a byte[]
	    byte[] fileBytes = null;
	    
		try {
			fileBytes = downloadDTO.getFile().readAllBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // Convertir byte[] a String en Base64
	    String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);

	    // Crear y retornar DownloadGestionArchivosDTO
	    DownloadGestionArchivosDTO downloadGestionArchivosDTO = new DownloadGestionArchivosDTO();
	    downloadGestionArchivosDTO.setId(downloadDTO.getId());
	    downloadGestionArchivosDTO.setName(downloadDTO.getName());
	    downloadGestionArchivosDTO.setUrl(downloadDTO.getUrl());
	    downloadGestionArchivosDTO.setFile(fileBase64);

	    return downloadGestionArchivosDTO;
	}
		
	private List<RegistrosCargadosDTO> obtenerErrores(List<RegistrosCargadosDTO> listaRegistros, Long idArchivoCargado) {
	    try {
	        actualizaPrimerRegistro(listaRegistros);
	        Map<Integer, List<String>> lineasAgrupadas = agruparLineasPorNumero(idArchivoCargado);
	        actualizaEstructurasAgrupadas(listaRegistros, lineasAgrupadas);
	    } catch (Exception e) {
	        throw new NegocioException(ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getCode(),
	                ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getDescription(),
	                ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getHttpStatus());
	    }
	    return listaRegistros;
	}

	private void actualizaPrimerRegistro(List<RegistrosCargadosDTO> listaRegistros) {
	    RegistrosCargadosDTO firstRecord = listaRegistros.get(0);
	    if (firstRecord.getId().getConsecutivoRegistro() == 0) {
	        String contenido = removerSaltoDeLineaSiExiste(firstRecord.getContenido());
	        contenido += "," + Constantes.CAMPO_OBSERVACION_ERRORES;
	        firstRecord.setContenido(contenido);
	        listaRegistros.set(0, firstRecord);
	    }
	}

	private String removerSaltoDeLineaSiExiste(String contenido) {
	    if (contenido.endsWith("\n")) {
	        contenido = contenido.substring(0, contenido.length() - 1);
	    }
	    return contenido;
	}

	private Map<Integer, List<String>> agruparLineasPorNumero(Long idArchivoCargado) {
	    ValidacionArchivoDTO validacionArchivoDTO = archivosLiquidacion.consultarDetalleError(idArchivoCargado);
	    Map<Integer, List<String>> lineasAgrupadas = new HashMap<>();
	    for (ValidacionLineasDTO linea : validacionArchivoDTO.getValidacionLineas()) {
	        int numeroLinea = linea.getNumeroLinea();
	        String estructura = getGrupoRegistrosError(linea);
	        lineasAgrupadas.computeIfAbsent(numeroLinea, k -> new ArrayList<>()).add(estructura);
	    }
	    return lineasAgrupadas;
	}

	private String getGrupoRegistrosError(ValidacionLineasDTO linea) {
	    StringBuilder estructura = new StringBuilder();
	    for (ErroresCamposDTO campo : linea.getCampos()) {
	        estructura.append("{");
	        estructura.append("CAMPO=").append(campo.getNumeroCampo()).append(",");
	        estructura.append("DESCRIPCION_ERROR=").append(campo.getMensajeErrorTxt()).append(",");
	        estructura.append("CONTENIDO_ERROR=").append(campo.getContenido());
	        estructura.append("},");
	    }
	    if (estructura.length() > 0)
	        estructura.setLength(estructura.length() - 1);
	    return estructura.toString();
	}

	private void actualizaEstructurasAgrupadas(List<RegistrosCargadosDTO> listaRegistros, Map<Integer, List<String>> lineasAgrupadas) {
	    for (RegistrosCargadosDTO registro : listaRegistros) {
	        int numeroLinea = registro.getId().getConsecutivoRegistro();
	        List<String> estructurasAgrupadas = lineasAgrupadas.get(numeroLinea);
	        if (estructurasAgrupadas != null) {
	            String contenido = removerSaltoDeLineaSiExiste(registro.getContenido());
	            StringBuilder contenidoActualizado = new StringBuilder(contenido);
	            for (String estructura : estructurasAgrupadas) {
	                contenidoActualizado.append(",[").append(estructura).append("]");
	            }
	            registro.setContenido(contenidoActualizado.toString());
	        }
	    }
	}

	private InputStream obtenerContenidos(List<RegistrosCargadosDTO> listaRegistros) {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<String> contenidos = listaRegistros.stream().map(RegistrosCargadosDTO::getContenido).collect(Collectors.toList());

        // Escribir el contenido en el flujo de bytes
        for (String contenido : contenidos) {
            try {
				outputStream.write(contenido.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				
				throw new NegocioException(ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getCode(),
						ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getDescription(),
						ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getHttpStatus());
			}
            try {
				outputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				
				throw new NegocioException(ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getCode(),
						ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getDescription(),
						ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getHttpStatus());
			} 
        }

        // Convierte el flujo de bytes en un array de bytes
        byte[] bytes = outputStream.toByteArray();
        // Cierra el recurso 
        try {
			outputStream.close();
			
		} catch (IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getCode(),
					ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getDescription(),
					ApiResponseCode.ERROR_CONTENIDO_ARCHIVO_PROCESADO.getHttpStatus());
		}
        
		return new ByteArrayInputStream(bytes);
	}	
}
