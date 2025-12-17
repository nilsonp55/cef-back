package com.ath.adminefectivo.service.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ParametrosRetencion;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IParametrosRetencionRepository;
import com.ath.adminefectivo.service.IGestionRetencionArchivosService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.utils.S3Utils;
import com.ath.adminefectivo.utils.UtilsString;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GestionRetencionArchivosServiceImpl implements IGestionRetencionArchivosService {

	@Autowired
	IParametrosRetencionRepository parametrosRetencionRepo;

	@Autowired
	IParametroService parametrosService;

	@Autowired
	S3Utils s3Utils;
	
	private String logPathFile;
	
	private List<String> registrosEliminados = new ArrayList<>();

	@Override
	public boolean eliminarArchivosPorRetencion() {
		
		try {
			boolean activo = true;
			var parametrosRetencionActivos = obtenerParametrosPorActivo(activo);
	
			if (!validarParametroRetencionLista(parametrosRetencionActivos)){
				throw new NegocioException(ApiResponseCode.ERROR_OBTENER_PARAMETROS_ACTIVOS.getCode(),
						ApiResponseCode.ERROR_OBTENER_PARAMETROS_ACTIVOS.getDescription(),
						ApiResponseCode.ERROR_OBTENER_PARAMETROS_ACTIVOS.getHttpStatus());
			}
			
			this.registrosEliminados = procesarParametroRetencionElemento(parametrosRetencionActivos);
		} catch (NegocioException ne) {
	        throw ne;
	    }		
		catch (Exception e) {
			
			log.error("[Política de Retención] Error al procesar eliminar registros por politica de retencion");
			throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
					ApiResponseCode.GENERIC_ERROR.getDescription(), ApiResponseCode.GENERIC_ERROR.getHttpStatus());
			
		} finally {
	        // Registrar el log con todos los archivos eliminados al final
			if (!this.registrosEliminados.isEmpty()) {
	            registrarLogEliminacion(this.registrosEliminados, this.logPathFile);
	        }
	        else 
	        {
	        	log.info("Proceso de Politica de Retencion Finalizado");
	        }
	        
	    }
	    return true;
	}

	private String validarBackSlash(String path) {
		char ultimoCaracterUbicacion = path.charAt(path.length() - 1);
		if (ultimoCaracterUbicacion != Constantes.SEPARADOR_DIRECTORIO_UNIX.charAt(0)) {
			path = path + Constantes.SEPARADOR_DIRECTORIO_UNIX;
		}
		return path;
	}

	// Valida la extensión del archivo contra la extensión del parametro de
	private boolean validarExtensionArchivo(String pathfileS3, String extension) {
		String extensionFileS3 = null;
		if (pathfileS3 != null && pathfileS3.contains(".")) {
			extensionFileS3 = pathfileS3.substring(pathfileS3.lastIndexOf(".") + 1);
			return extensionFileS3.equalsIgnoreCase(extension);
		}
		return false;
	}

	private String obtenerPrefix(String agrupador, String mascaraArch) {

		try {
			if (agrupador.equals("CERTI")) {
				return mascaraArch.substring(0, 2);
			} else if (agrupador.equals("LIQCO")) {
				return mascaraArch.replace("[", "").replace("]", "").substring(0, 6);
			} else if (agrupador.equals("DEFIN") || agrupador.equals("IPP")) {
				String[] estructuraProgramacion = mascaraArch.split(Constantes.SEPARADOR_FECHA_ARCHIVO);
				return estructuraProgramacion[0];
			} else if (agrupador.equals("CONTA")) {	         
	                return mascaraArch.substring(0, 13);
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_MASCARA_NO_VALIDA.getCode(),
					ApiResponseCode.ERROR_MASCARA_NO_VALIDA.getDescription(),
					ApiResponseCode.ERROR_MASCARA_NO_VALIDA.getHttpStatus());
		}
	}

	Date parseDate(String fechaStr, String formato) throws ParseException {
		return DateUtils.parseDate(fechaStr, formato);
	}
	
	private boolean validarParametroRetencionLista(List<ParametrosRetencion> parametrosRetencionActivos)
	{
		if (Objects.isNull(parametrosRetencionActivos) || parametrosRetencionActivos.isEmpty()){
			log.error("[Política de Retención] No se encontraron parametros de retencion o no se encuentran activos");
			return false;
		}
		return true;
	}
	
	private List<String> procesarParametroRetencionElemento(List<ParametrosRetencion> parametrosRetencionActivos)
	{
		List<String> registros = new ArrayList<>();
		
		if (Objects.isNull(parametrosRetencionActivos) || parametrosRetencionActivos.isEmpty())
		{
			return registros;
		}
		
		for (ParametrosRetencion parametroRetencion : parametrosRetencionActivos) {
			if (validaParametroRetencion(parametroRetencion))
			{
				String path = validarBackSlash(parametroRetencion.getUbicacion());
				logPathFile = path;
				path = path + obtenerPrefix(parametroRetencion.getAgrupador(), parametroRetencion.getMarcaraArch());
				var objectsSummary = s3Utils.getObjectsSummaryFromPathS3(path);
				
				validarObjectSummary(objectsSummary, parametroRetencion, logPathFile);
			}
		}
		
		return registros;
	}
	
	private boolean validaParametroRetencion(ParametrosRetencion parametroRetencion)
	{
		String agrupador = parametroRetencion.getAgrupador();
		String path = parametroRetencion.getUbicacion();
		String extension = parametroRetencion.getExtension();
		String mascaraArch = parametroRetencion.getMarcaraArch();
		int periodoRetencion = parametroRetencion.getPeriodoRetencion();
		
		if (Objects.isNull(agrupador) || Objects.isNull(path) || Objects.isNull(extension) || Objects.isNull(mascaraArch) || Objects.isNull(periodoRetencion)) {
			log.error("[Política de Retención] El parámetro de retención contiene campos nulos {}", parametroRetencion);
			return false;
		}
		
		return true;
	}
	
	private boolean validarObjectSummary(List<S3ObjectSummary> objectsSummary, ParametrosRetencion parametroRetencion,
															String logPathFile)
	{	    
	    String extension = parametroRetencion.getExtension();
        String agrupador =  parametroRetencion.getAgrupador();
        String mascaraArch = parametroRetencion.getMarcaraArch();
        int periodoRetencion = parametroRetencion.getPeriodoRetencion();
        Boolean sufijo = parametroRetencion.getSufijo();
		this.registrosEliminados = new ArrayList<>();
		if (Objects.isNull(objectsSummary) || objectsSummary.isEmpty()) {
			log.info("[Política de Retención] No hay archivos por procesar para política de retencion");
			return false ;
		}
		
		for (S3ObjectSummary objectSummary : objectsSummary) {
			String pathfileS3 = objectSummary.getKey();
			boolean esExtensionValida = validarExtensionArchivo(pathfileS3, extension);			
			if (esExtensionValida && agrupador.equals("CONTA")) {
			    ejecutarRetencion(objectSummary.getLastModified(), periodoRetencion, pathfileS3);
			} else if (esExtensionValida){
			    String nombreArchivoS3 = extraerNombreArchivo(pathfileS3, sufijo);			    
                if(!Objects.isNull(nombreArchivoS3)) {
                    Date fechaArchivoS3 = extraerFechaPorAgrupador(agrupador, nombreArchivoS3, mascaraArch);
                    if (!Objects.isNull(fechaArchivoS3)) {
                        ejecutarRetencion(fechaArchivoS3, periodoRetencion, pathfileS3);
                    }else {
                        log.error("[Política de Retención] Archivo no contiene una fecha valida o no se extrajo correctamente {} ", pathfileS3);
                    }
                }                
			}
		}
		if (!this.registrosEliminados.isEmpty()) {
            registrarLogEliminacion(this.registrosEliminados, logPathFile);
            this.registrosEliminados.clear();
            this.logPathFile = "";
        }
		
		return true;
	}

	private void ejecutarRetencion(Date fechaArchivoS3, int periodoRetencion, String pathfileS3 ) {
	    if (validarPeriodoRetencion(fechaArchivoS3, periodoRetencion)) {
            s3Utils.deleteObjectBucket(pathfileS3);
            this.registrosEliminados.add(pathfileS3);
        }        
    }

    @Override
	public List<ParametrosRetencion> obtenerParametrosPorActivo(boolean activo) {
		return parametrosRetencionRepo.findConfParametrosRetencionByActivo(activo);
	}

	private Date extraerFechaPorAgrupador(String agrupador, String nombreArchivo, String mascaraArch) {
		try {
			if (agrupador.equals("CERTI")) {
				return extraerFechaCERTI(nombreArchivo, mascaraArch);
			} else if (agrupador.equals("LIQCO")) {
				return extraerFechaLIQCO(nombreArchivo, Constantes.FECHA_PATTERN_NO_GUION);
			} else {
				return extraerFechaProgamacion(nombreArchivo, mascaraArch);
			}
		} catch (Exception e) {
			return null;
		}
	}

	private Date extraerFechaCERTI(String nombreArchivo, String mascaraArch) {
		String inicioNombre = nombreArchivo.substring(0, 2);
		switch (inicioNombre) {
		case "AC":
			return extraerFechaPorMascara(nombreArchivo.substring(5, 11), mascaraArch.substring(8, 14));
		case "BS", "BI":
			return extraerFechaPorMascara(nombreArchivo.substring(8, 14), mascaraArch.substring(13, 19));
		case "TH":
			String fecha = getStringFileCertiTH(nombreArchivo);
			return extraerFechaPorMascara(fecha, mascaraArch.substring(19, 27));
		case "SC":
			if (nombreArchivo.contains("VILLAS")) {
				return extraerFechaPorMascara(nombreArchivo.substring(4, 14), (mascaraArch.substring(5, 13) + "yy"));
			} else {
				return extraerFechaPorMascara(nombreArchivo.substring(4, 12), mascaraArch.substring(5, 13));
			}
		default:
			log.info("[Política de Retención] No se pudo extraer fecha de archivos CERTI {} y mascara {}",nombreArchivo, mascaraArch);
			return null;
		}
	}

	private Date extraerFechaLIQCO(String nombreArchivo, String mascaraArch) {
		String inicioNombre = nombreArchivo.substring(0, 5);
		String fechaArchivo = nombreArchivo.substring(Math.max(0, nombreArchivo.length() - 8));

		switch (inicioNombre) {
		case "LIQ_T":
			return extraerFechaPorMascara(fechaArchivo, mascaraArch);
		case "LIQ_P":
			return extraerFechaPorMascara(fechaArchivo, mascaraArch);
		default:
			log.info("[Política de Retención] No se pudo extraer fecha de archivos LIQCO {} y mascara {}",nombreArchivo, mascaraArch);
			return null;
		}
	}

	private Date extraerFechaProgamacion(String nombreArchivoS3, String mascaraArch) {
		Date fechaArchivoS3 = null;
		String[] arregloNombreS3 = nombreArchivoS3.split(Constantes.SEPARADOR_FECHA_ARCHIVO);
		String[] arregloMascara = mascaraArch.split(Constantes.SEPARADOR_FECHA_ARCHIVO);
		try {
			if (arregloMascara[1].length() == arregloNombreS3[1].length()) {
				fechaArchivoS3 = new SimpleDateFormat(arregloMascara[1]).parse(arregloNombreS3[1]);
			} else {
				if (arregloMascara[1].length() - arregloNombreS3[1].length() == 1) {
					fechaArchivoS3 = new SimpleDateFormat(arregloMascara[1]).parse("0".concat(arregloNombreS3[1]));
				}
			}

		} catch (Exception e) {
			log.info("[Política de Retención] No se pudo extraer fecha de archivos PROGRAMACION {} y mascara {}",nombreArchivoS3, mascaraArch);
		}
		return fechaArchivoS3;
	}

	private Date extraerFechaPorMascara(String fecha, String mascaraFecha) {
		List<String> formatoFecha = new ArrayList<>();
		formatoFecha.add(mascaraFecha);
		if (!UtilsString.isFecha(fecha, formatoFecha)) {
			throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
					ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
					ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
		}
		return UtilsString.convertirFecha(fecha, formatoFecha);
	}

	private boolean validarPeriodoRetencion(Date fechaArchivoS3, Integer periodoRetencion) {
		LocalDate fechaArchivoLocal = fechaArchivoS3.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate fechaProceso = parametrosService.valorParametroDate(Constantes.FECHA_DIA_PROCESO).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long diasDiferencia = ChronoUnit.DAYS.between(fechaArchivoLocal, fechaProceso);
		return diasDiferencia >= periodoRetencion;
	}
	
	private String getStringFileCertiTH(String nombreArchivoS3) {
		return nombreArchivoS3.substring(nombreArchivoS3.length()-8, nombreArchivoS3.length());
	}
	
	private String extraerNombreArchivo (String pathfileS3, boolean sufijo) {
	   try {
	       String nombreArchivoS3 = pathfileS3.substring(pathfileS3.lastIndexOf("/") + 1,
	                pathfileS3.lastIndexOf(".")); 
	       if(sufijo) {
	           nombreArchivoS3 = extraerNombreArchivoSufijo(nombreArchivoS3);
	       }
	       return nombreArchivoS3;
	   }
	   catch(Exception e) {
	       log.error("[Política de Retención] Error al extraer Nombre de archivo del pathfile: " + pathfileS3, e);
	   }
       return null; 
	}
	
	private String extraerNombreArchivoSufijo (String nombreArchivoS3) {   
        String sufijo = nombreArchivoS3.substring(nombreArchivoS3.lastIndexOf("-")+1,nombreArchivoS3.length());
        boolean patterIsOk = Pattern.compile("^\\d+$").matcher(sufijo).matches();
        if(!patterIsOk) {
            log.error("[Política de Retención] El patron del sufijo no ajusta para el archivo con nombre: " + nombreArchivoS3);
            return null;
        }
        return nombreArchivoS3.substring(0,nombreArchivoS3.lastIndexOf("-")); 
	}
	

	private void registrarLogEliminacion(List<String> registros, String pathFileS3) {
		
		if (Objects.isNull(pathFileS3) || Objects.isNull(registros))
		{
			log.error("[Política de Retención] Error al registrar log de eliminación, no se obtuvo parametro registros");
			log.error("[Política de Retención] Error al registrar log de eliminación, no se obtuvo parametro pathfile, ");
			return;
		}
		try {
			String rutaBase = pathFileS3.substring(0, pathFileS3.lastIndexOf("/"));
			LocalDateTime now = LocalDateTime.now();
			String nombreArchivoLog = String.format("log_archivo_depurados_%s.txt",
					now.format(DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA_ARCHIVOS_MS))); // Agregar hora,
																									// minutos y
																									// segundos, milisegundos
			String rutaArchivoLog = rutaBase + "/" + nombreArchivoLog;

			String headers = "fecha_depuracion,ubicacion";
			final String LINE_ENDING = "\r\n";
			StringBuilder contenidoFinal = new StringBuilder(headers).append(LINE_ENDING);
			
			// Agregar cada registro al contenido
			for (String registro : registros) {
	            contenidoFinal.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
	                         .append(",")
	                         .append(registro)
	                         .append(LINE_ENDING);
	        }

			// Configura metadatos del archivo
			ObjectMetadata metadata = new ObjectMetadata();
			byte[] contenidoBytes = contenidoFinal.toString().getBytes(StandardCharsets.UTF_8);
			metadata.setContentType("text/plain");
			metadata.setContentLength(contenidoBytes.length);

			// Sube el archivo a S3
			try (ByteArrayInputStream inputFile = new ByteArrayInputStream(contenidoBytes)) {
				s3Utils.putS3Objets(rutaArchivoLog, inputFile, metadata);
				log.info("[Política de Retención] Log de eliminación registrado exitosamente en: {}", rutaArchivoLog);
			}
		} catch (Exception e) {
			log.error("[Política de Retención] Error al registrar log de eliminación para los registros en: " + pathFileS3);
		}
	}

}
