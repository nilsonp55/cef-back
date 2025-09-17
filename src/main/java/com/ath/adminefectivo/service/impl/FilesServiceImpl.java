package com.ath.adminefectivo.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.compuestos.SummaryArchivoLiquidacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.utils.S3Utils;
import com.ath.adminefectivo.utils.S3UtilsV2;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FilesServiceImpl implements IFilesService {

  private static final String TEMPORAL_URL = "C:\\Ath\\Docs\\";
  @Value("${aws.s3.active}")
  Boolean s3Bucket;

  @Autowired
  IParametroService parametroService;

  @Autowired
  private S3Utils s3Util;
  
  @Autowired
  private S3UtilsV2 s3UtilsV2;

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean persistirArchvos(List<MultipartFile> files) {

    this.validarPath(TEMPORAL_URL);
    for (MultipartFile file : files) {
      var dest = new File(TEMPORAL_URL, file.getOriginalFilename());
      try {
        Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        log.error("persistirArchvos: {}", e.getMessage());
        return false;
      }
    }

    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String persistirArchvo(MultipartFile file) {

    this.validarPath(TEMPORAL_URL);
    File dest = new File(TEMPORAL_URL, file.getOriginalFilename());
    try {
      Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      log.error("persistirArchvo: {}", e.getMessage());
      throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
          ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
          ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());

    }
    return dest.getPath();

  }

  /**
   * {@inheritDoc}
   */
	@Override
	public DownloadDTO downloadFile(DownloadDTO download) {
		String path = download.getUrl();
		try {

			if (Boolean.TRUE.equals(s3Bucket)) {
				if (s3Util.consultarArchivo(path)) {
					final InputStream streamReader = s3Util.downloadFile(path);
					download.setFile(streamReader);
				}
			} else {
				File initialFile = new File(TEMPORAL_URL+path);
				Resource recurso = new UrlResource(initialFile.toURI());
				InputStream inputStream = recurso.getInputStream();
				// Realiza operaciones de lectura del archivo usando inputStream
				download.setFile(inputStream);
			}
			log.debug("descarga archivo desde URL: {}", path);
		} catch (IOException e) {
			log.error("downloadFile: {}", e.getMessage());
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}
		return download;

	}

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean eliminarArchivo(String url) {
    try {
      if (Boolean.TRUE.equals(s3Bucket)) {
        s3Util.deleteObjectBucket(url);
      } else {
    	
    	// Divide la cadena de la ruta en segmentos usando el carácter "/"
    	String[] segmentos = url.split("/");
    	// Tomar el último segmento como el nombre del archivo
    	String nombreArchivo = segmentos[segmentos.length - 1];
    	url = TEMPORAL_URL + "\\" + nombreArchivo;
        Files.delete(Path.of(url));
        
      }
    } catch (IOException e) {
      throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
          ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
          ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> obtenerContenidoCarpeta(String url) {

    List<String> contenidoCarpeta;
    if (Boolean.TRUE.equals(s3Bucket)) {
      contenidoCarpeta = s3UtilsV2.getObjectsFromPathS3(url);
    } else {
      File carpeta = new File(TEMPORAL_URL+url);
      contenidoCarpeta = Arrays.asList(carpeta.list());
    }

    if (Objects.isNull(contenidoCarpeta)) {
      throw new NegocioException(ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getCode(),
          ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getDescription(),
          ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getHttpStatus());
    }

    return contenidoCarpeta;
  }

  /**
   * {@inheritDoc}
   */
   
  @Override
  public List<SummaryArchivoLiquidacionDTO> obtenerContenidoCarpetaSummaryS3Object(String url, int start, int end, boolean content, String fileName) {
	    List<S3ObjectSummary> s3ObjectSummaries = getResumenObjetosS3(url, start, end, fileName);
	    if (Objects.isNull(s3ObjectSummaries)) {
	        throw new NegocioException(ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getCode(),
	                ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getDescription(),
	                ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getHttpStatus());
	    }
	    return obtenerArchivosDTOList(s3ObjectSummaries, content, url);
	}

	private List<S3ObjectSummary> getResumenObjetosS3(String url, int start, int end, String fileName) {
	    if (Boolean.TRUE.equals(s3Bucket)) {
	        return getResumenObjetosS3Bucket(url, start, end, fileName);
	    } else {
	        return getResumenDirectorioLocal(url,fileName);
	    }
	}

	private List<S3ObjectSummary> getResumenObjetosS3Bucket(String url, int start, int end, String fileName) {
	    if (!fileName.isBlank()) {
	        return s3Util.getObjectsSummaryFromPathS3(url + fileName);
	    } else {
	        return s3Util.getObjectsBySegments(url, start, end);
	    }
	}

	private List<S3ObjectSummary> getResumenDirectorioLocal(String url, String fileName) {
	    if (!fileName.isEmpty() && !fileName.isBlank() && !fileName.endsWith(".txt")) {
	        fileName = fileName + ".txt";
	    }

	    File fileOrDirectory = new File(getLocalTemporalPath(url), fileName);
	    if (fileOrDirectory.isDirectory()) {
	        return getResumenObjetosDirectorio(fileOrDirectory);
	    } else if (fileOrDirectory.isFile()) {
	        return getResumenObjetoArchivo(fileOrDirectory);
	    }
	    return null;
	}
	
	private String getLocalTemporalPath(String url) {
		String temporalPath = TEMPORAL_URL;
		if (!Objects.isNull(url) && !url.isEmpty() && !url.isBlank()) {
			temporalPath = temporalPath.concat(url);
	    }
		return temporalPath;
	}

	private List<S3ObjectSummary> getResumenObjetosDirectorio(File directorio) {
	    List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
	    File[] archivos = directorio.listFiles();
	    if (archivos != null) {
	        for (File archivo : archivos) {
	            s3ObjectSummaries.add(crearResumenObjetoS3(archivo));
	        }
	    }
	    return s3ObjectSummaries;
	}

	private List<S3ObjectSummary> getResumenObjetoArchivo(File archivo) {
	    List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
	    s3ObjectSummaries.add(crearResumenObjetoS3(archivo));
	    return s3ObjectSummaries;
	}

	private S3ObjectSummary crearResumenObjetoS3(File archivo) {
	    S3ObjectSummary objetoS3 = new S3ObjectSummary();
	    objetoS3.setKey(archivo.getName());
	    objetoS3.setLastModified(new Date(archivo.lastModified()));
	    objetoS3.setSize(archivo.length());
	    return objetoS3;
	}

	private List<SummaryArchivoLiquidacionDTO> obtenerArchivosDTOList(List<S3ObjectSummary> s3ObjectSummaries, boolean content, String url) {
	    List<SummaryArchivoLiquidacionDTO> archivosDTOList = new ArrayList<>();
	    for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
	        if (s3ObjectSummary.getSize() > 0) {
	            SummaryArchivoLiquidacionDTO archivoDTO = new SummaryArchivoLiquidacionDTO();
	            archivoDTO.setS3ObjectSummary(s3ObjectSummary);
	            if (content) {
	                List<String> contenidoArchivo = obtenerContenidoArchivo(s3ObjectSummary, url);
	                archivoDTO.setContenidoArchivo(contenidoArchivo);
	            }
	            archivosDTOList.add(archivoDTO);
	        }
	    }
	    return archivosDTOList;
	}

	private List<String> obtenerContenidoArchivo(S3ObjectSummary s3ObjectSummary, String url) {
	    if (Boolean.TRUE.equals(s3Bucket)) {
	        return s3Util.getFileContent(s3ObjectSummary.getKey());
	    } else {
	        return leerContenidoArchivo(new File(getLocalTemporalPath(url) + s3ObjectSummary.getKey()));
	    }
	}

  /**
   * Metodo encargado de obtener el contenido de los archivos en un directorio local
   * 
   * @param archivo
   * @author johan.chaparro
   */
  public List<String> leerContenidoArchivo(File archivo) {
      List<String> content = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
          String linea;
          while ((linea = reader.readLine()) != null) {
        	  content.add(linea);
          }
      } catch (IOException e) {
    	  throw new NegocioException(ApiResponseCode.ERROR_CONTENIDO_ARCHIVO.getCode(),
    	          ApiResponseCode.ERROR_CONTENIDO_ARCHIVO.getDescription(),
    	          ApiResponseCode.ERROR_CONTENIDO_ARCHIVO.getHttpStatus());
      }
      return content;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean moverArchivos(String urlSource, String urlDestino, String nombreArchivo,
      String postfijo) {
    log.debug("moverArchivos inicio");
    
    if (Boolean.FALSE.equals(s3Bucket)) {
      urlSource = TEMPORAL_URL + urlSource;
      urlDestino = TEMPORAL_URL + urlDestino;
    }
    
    Path origenPath = FileSystems.getDefault().getPath(urlSource);
    this.validarPath(urlDestino);
    String[] arregloNombre = nombreArchivo.split(Constantes.EXPRESION_REGULAR_PUNTO);
    nombreArchivo = arregloNombre[0].concat("-" + postfijo);
    Path destinoPath =
        FileSystems.getDefault().getPath(urlDestino, nombreArchivo.concat("." + arregloNombre[1]));
    log.debug("origenPath: {} - arregloNombre:{} - nombreArchivo: {} - destinoPath: {}", origenPath, arregloNombre, nombreArchivo, destinoPath);
    try {
      if (Boolean.TRUE.equals(s3Bucket)) {
        s3Util.moverObjeto(origenPath.toString(), destinoPath.toString());
      } else {
        Files.move(origenPath, destinoPath);
      }

    } catch (IOException e) {
      log.debug("moverArchivos Exception: {}", e.getMessage());
      throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
          ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
          ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
    }
    log.debug("moverArchivos fin");
    return true;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean moverArchivosS3(String urlSource, 
		  						String urlDestino, 
		  						String nombreArchivo,
		  						String postfijo) 
  {
		if (Boolean.TRUE.equals(s3Bucket)) {
			this.validarPath(urlDestino);
			try {
				s3Util.moverObjeto(urlSource, urlDestino + nombreArchivo);
			} catch (Exception e) {
				throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
						ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
						ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
			}
		} else {
						
	    	String[] segmentos = urlDestino.split("/");
	    	String destFolder = segmentos[segmentos.length - 1];
	    	
			this.validarPath(TEMPORAL_URL + File.separator + destFolder);
			
			File fileOrigen = new File(TEMPORAL_URL + File.separator + nombreArchivo);
            File fileDestino = new File(TEMPORAL_URL + File.separator + destFolder);
            
            if (!fileOrigen.exists()) {
            	throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
						ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
						ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
            }
            
            Path fullPath = fileDestino.toPath().resolve(nombreArchivo);
            
            try {
				Files.move(fileOrigen.toPath(), fullPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
						ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
						ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
			}
            
		}

	  return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String consultarPathArchivos(String estado) {
    String parametro = switch (estado) {
      case Constantes.ESTADO_CARGUE_PENDIENTE -> Parametros.RUTA_ARCHIVOS_PENDIENTES;
      case Constantes.ESTADO_CARGUE_ERROR -> Parametros.RUTA_ARCHIVOS_ERRADOS;
      case Constantes.ESTADO_CARGUE_VALIDO -> Parametros.RUTA_ARCHIVOS_PROCESADOS;
      default -> throw new NegocioException(ApiResponseCode.ERROR_ESTADO_ARCHIVO.getCode(),
          ApiResponseCode.ERROR_ESTADO_ARCHIVO.getDescription(),
          ApiResponseCode.ERROR_ESTADO_ARCHIVO.getHttpStatus());
    };
    return parametroService.valorParametro(parametro);

  }

  /**
   * Metodo encargado de validar si la ruta del path existe en cason contrario la crea
   * 
   * @param path
   * @author CamiloBenavides
   */
  private void validarPath(String path) {
    var file = new File(path);
    if (!file.exists() && !file.mkdirs()) {
      throw new IllegalArgumentException();
    }
  }
}
