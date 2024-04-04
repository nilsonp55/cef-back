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

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FilesServiceImpl implements IFilesService {

  private static final String TEMPORAL_URL = "C:\\Ath\\Docs";
  @Value("${aws.s3.active}")
  Boolean s3Bucket;

  @Autowired
  IParametroService parametroService;

  @Autowired
  private S3Utils s3Util;

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
				// Divide la cadena de la ruta en segmentos usando el carácter "/"
				String[] segmentos = path.split("/");
				// Tomar el último segmento como el nombre del archivo
				String nombreArchivo = segmentos[segmentos.length - 1];

				File initialFile = new File(TEMPORAL_URL + "\\" + nombreArchivo);
				Resource recurso = new UrlResource(initialFile.toURI());
				InputStream inputStream = recurso.getInputStream();
				try {
					// Realiza operaciones de lectura del archivo usando inputStream
					download.setFile(inputStream);
				} finally {
					// Cierre seguro del inputStream
					if (inputStream != null) {
						// inputStream.close();
					}
				}
			}
			
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
      contenidoCarpeta = s3Util.getObjectsFromPathS3(url);
    } else {
      File carpeta = new File(url);
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
	  
	  List<String> contenidoArchivo = null;
	  List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
	  List<SummaryArchivoLiquidacionDTO> archivosDTOList = new ArrayList<>();
	    
	  // Procesa la informacion desde un bucke S3 si el parametro es true
	    if (Boolean.TRUE.equals(s3Bucket)) {
	    	if (!fileName.isBlank()) {
	    		s3ObjectSummaries = s3Util.getObjectsSummaryFromPathS3(url + fileName);
	    	}else {
	    		s3ObjectSummaries = s3Util.getObjectsBySegments(url,start,end);
	    	}
	    	
	    } else {
	    	
	    	if (!fileName.isEmpty() && !fileName.isBlank()) {
	    		
	    		if (!fileName.endsWith(".txt")) {
	    		    fileName = fileName + ".txt";
	    		}
	    	}
	    	// Procesa la informacion desde un directorio local si el parametro es false	    	
	    	File fileOrDirectory = new File(TEMPORAL_URL, fileName);
	    	
	    	if (fileOrDirectory.isDirectory()) {
	    	    // Si es un directorio, lista todos los archivos
	    	    File[] archivos = fileOrDirectory.listFiles();
	    	    if (archivos != null) {
	    	        for (File archivo : archivos) {
	    	            S3ObjectSummary objetoS3 = new S3ObjectSummary();
	    	            objetoS3.setKey(archivo.getName());
	    	            objetoS3.setLastModified(new Date(archivo.lastModified()));
	    	            objetoS3.setSize(archivo.length()); 
	    	            s3ObjectSummaries.add(objetoS3);
	    	        }
	    	    }
	    	} else if (fileOrDirectory.isFile()) {
	    	    // Es un archivo, lista solo el archivo
	    	    S3ObjectSummary objetoS3 = new S3ObjectSummary();
	    	    objetoS3.setKey(fileOrDirectory.getName());
	    	    objetoS3.setLastModified(new Date(fileOrDirectory.lastModified()));
	    	    objetoS3.setSize(fileOrDirectory.length()); 
	    	    s3ObjectSummaries.add(objetoS3);
	    	}
	    	
	    }
	    
	    if (Objects.isNull(s3ObjectSummaries)) {
	      throw new NegocioException(ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getCode(),
	              ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getDescription(),
	              ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getHttpStatus());
	    } else {    	    
	    	
	    	for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
	    		
	    		// Verifica si el tamaño del archivo es mayor que cero antes de procesarlo
	    		if (s3ObjectSummary.getSize() > 0) {
		    		SummaryArchivoLiquidacionDTO archivoDTO = new SummaryArchivoLiquidacionDTO();
		            archivoDTO.setS3ObjectSummary(s3ObjectSummary);
		            
		            // Verifica si la peticion solicta que los archivos tengan contenido
		            if(content) {

		            	 if (Boolean.TRUE.equals(s3Bucket)) {
		            		 // Obtiene el contenido del archivo en el bucket S3
		            		 contenidoArchivo = s3Util.getFileContent(s3ObjectSummary.getKey());	
		            	 } else {
		            		 // Obtiene el contenido del archivo en el directorio local
		            		 contenidoArchivo = leerContenidoArchivo(new File(TEMPORAL_URL + "\\" + s3ObjectSummary.getKey()));
		            	 }
		            	            				            		            
		            }
		            
		            archivoDTO.setContenidoArchivo(contenidoArchivo);		            
		            archivosDTOList.add(archivoDTO);
		            
	    		}
	        }
	    }

	    return archivosDTOList;
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
    Path origenPath = FileSystems.getDefault().getPath(urlSource);
    this.validarPath(urlDestino);
    String[] arregloNombre = nombreArchivo.split(Constantes.EXPRESION_REGULAR_PUNTO);
    nombreArchivo = arregloNombre[0].concat("-" + postfijo);
    Path destinoPath =
        FileSystems.getDefault().getPath(urlDestino, nombreArchivo.concat("." + arregloNombre[1]));
    try {
      if (Boolean.TRUE.equals(s3Bucket)) {
        s3Util.moverObjeto(origenPath.toString(), destinoPath.toString());
      } else {
        Files.move(origenPath, destinoPath);
      }

    } catch (IOException e) {
      throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
          ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
          ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
    }

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
	    	//moverArchivos(TEMPORAL_URL, TEMPORAL_URL + "\\" + destFolder, nombreArchivo,"");
	    	
			this.validarPath(TEMPORAL_URL + "\\" + destFolder);
			
			File fileOrigen = new File(TEMPORAL_URL + "\\" + nombreArchivo);
            File fileDestino = new File(TEMPORAL_URL + "\\" + destFolder);
            
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
