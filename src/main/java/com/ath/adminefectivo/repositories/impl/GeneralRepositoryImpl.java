package com.ath.adminefectivo.repositories.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IGeneralRepository;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.repositories.ITransportadorasRepository;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.IPuntoInterno;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.impl.CostosProcesamientoServiceImpl;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

import lombok.extern.log4j.Log4j2;

/**
 * Repository encargado de manejar la logica de la entidad ArchivosCargados
 *
 * @author CamiloBenavides
 */
@Service
@Log4j2
public class GeneralRepositoryImpl implements IGeneralRepository{
	
	@Autowired
    ITransportadorasRepository transportadorasRepository;
	
	@Autowired
	ITransportadorasService transportadorasService;
	
	@Autowired
	IPuntosRepository puntosRepository;
	
	@Autowired
	CostosProcesamientoServiceImpl costosProcesamientoService;
	
	@Autowired
	IArchivosLiquidacionService archivosLiquidacionService;
	
    @PersistenceContext
    private EntityManager entityManager;
	
    @Override
	public boolean ejecutarQueryNativa(String consulta) {
		Query query =  entityManager.createNativeQuery(consulta);
		var result = query.getResultList();
		return !result.isEmpty();
	}
    
    @Override
	public boolean ejecutarQueryNativa(String consulta, String parametro) {
		Query query =  entityManager.createNativeQuery(consulta);
		log.debug("consulta "+ consulta);
		log.debug("PARAMETRO "+ parametro);
		query.setParameter("parametro", parametro);
		
		return (boolean)  query.getSingleResult();
	}

    /**
     * Construye una consulta SQL reemplazando los parámetros con sus valores reales,
     * aplicando el formato correspondiente según su tipo de dato.
     * 
     * La consulta debe contener parámetros en formato :nombre o :nombre{tipo}.
     * Si el tipo se especifica en la consulta ({tipo}), se toma como prioridad;
     * de lo contrario, se utiliza el tipo definido en el mapa detalleDefinicionMap.
     * 
     * Los tipos soportados son:
     * N = Entero, D = Decimal, T = Texto, C = Carácter, F = Fecha, H = Hora, Y = Fecha y hora.
     * El tipo determina si el valor se rodea con comillas simples o no.
     * 
     * El parámetro literal ":parametro" se conserva sin reemplazo.
     *
     * @param consulta Cadena SQL con parámetros a reemplazar
     * @param detalleDefinicionMap Mapa de parámetros con su valor y tipo de dato
     * @return Consulta con los valores reemplazados
     * @author jchaparro
     */
   
	@Override
	public String queryBuilder(String consulta, Map<String, ListaDetalleDTO> detalleDefinicionMap) {

		Pattern pattern = Pattern.compile(":(\\w+)(\\{([A-Z])\\})?");
		Matcher matcher = pattern.matcher(consulta);
		StringBuffer resultado = new StringBuffer();

		while (matcher.find()) {

			String nombreParametro = matcher.group(1);
			String tipoForzado = matcher.group(3); // puede ser null

			// Excluir :parametro sin tipo
			if ("parametro".equals(nombreParametro)) {
				matcher.appendReplacement(resultado, ":" + nombreParametro);
				continue;
			}

			ListaDetalleDTO campo = detalleDefinicionMap.get(nombreParametro);
			if (campo == null || campo.getValor() == null) {
//				throw new AplicationException(ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getCode(),
//						"Falta el valor para el parámetro: " + nombreParametro,
//						ApiResponseCode.ERROR_PARAMETRO_NOT_FOUND.getHttpStatus());
				continue;
			}

			// Prioridad: tipo en la consulta > tipo del Map
			String tipoDato = tipoForzado != null ? tipoForzado : campo.getTipoDato();
			String valor = campo.getValor();

			String valorReemplazo;
			switch (tipoDato) {
			case Dominios.TIPO_DATO_ENTERO:
			case Dominios.TIPO_DATO_DECIMAL:
				valorReemplazo = valor;
				break;
			case Dominios.TIPO_DATO_TEXTO:
			case Dominios.TIPO_DATO_CARACTER:
			case Dominios.TIPO_DATO_FECHA:
			case Dominios.TIPO_DATO_HORA:
			case Dominios.TIPO_FECHA_HORA:
				valorReemplazo = "'" + valor + "'";
				break;
			default:
				valorReemplazo = valor;
				break;
			}

			matcher.appendReplacement(resultado, Matcher.quoteReplacement(valorReemplazo));
		}

		matcher.appendTail(resultado);
		return resultado.toString();
	}

	/**
	 * Retorna el valor del parámetro correspondiente según la descripción de la regla.
	 * Este valor es utilizado en la construcción de la expresiones SQL del metodo queryBuilder.
	 * Si se inyecta en el Map, retorna nulo para indicar que se uso en el objeto
	 *
	 * @param descripcionRegla Descripción de la regla que se desea evaluar.
	 * @param validacionArchivo Objeto con los datos del archivo que podrían ser usados en la lógica.
	 * @return La expresión SQL correspondiente como String, o null si la descripción no coincide con ninguna regla conocida.
	 */
	@Override
	public String getvalorCampoRegla(String valorFijoRegla, ValidacionArchivoDTO validacionArchivo, Map<String, ListaDetalleDTO> detalleDefinicionMap) {

		if (valorFijoRegla == null) return null;

	    switch (valorFijoRegla.toUpperCase()) {
	        case Parametros.VALIDAR_FECHA_TRANSPORTE:
	        	
	        	SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);
	        	String fechaFormateada = sdf.format(validacionArchivo.getFechaArchivo());
	            
	        	detalleDefinicionMap.put("fecha_archivo", ListaDetalleDTO.builder()
	        	        .nombreCampo("fecha_archivo")
	        	        .tipoDato("F")
	        	        .valor(fechaFormateada)
	        	        .build());
	        	
	        	    return null;

	        case Parametros.CODIGO_TDV:
	        	
	        	String archivo = validacionArchivo.getNombreArchivo();
	        	String abreviaturaEncontrada = null;
	        	List<Transportadoras> transportadoras;
	        	transportadoras = transportadorasRepository.findAll();
	        	
	        	for (Transportadoras t : transportadoras) {
	        	    if (archivo.contains(t.getNombreTransportadora())) {
	        	    	detalleDefinicionMap.put("codigo_tdv", ListaDetalleDTO.builder()
		    	    			.nombreCampo("codigo_tdv")
		    	    			.tipoDato("T")
		    	    			.valor(t.getAbreviatura())
		    	    			.build());
		    	    	return null; 
	        	    }
	        	}
	        		        	
	            return null;

	        case Parametros.MAESTRO_LLAVE_PROCESAMIENTO:
	        	
				Map<String, ListaDetalleDTO> valProcesamiento = getLlaveProcesamiento(validacionArchivo, detalleDefinicionMap);
				return null;
				
			case Parametros.MAESTRO_LLAVE_TRANSPORTE:

				Map<String, ListaDetalleDTO> valTransporte = getLlaveTransporte(validacionArchivo, detalleDefinicionMap);
				return null;
	        
	        case Parametros.MONEDA_DIVISA:
	            return "fecha_servicio_transporte";
	        
	        case Parametros.TIPO_COMISION_TARIFA_ESPECIAL:
	        	
				String tipoOperacion = detalleDefinicionMap.get("tipo_operacion").getValor();

				detalleDefinicionMap.put("nombre_dominio_comision",
						ListaDetalleDTO.builder().nombreCampo("nombre_dominio_comision").tipoDato("T")
								.valor(Dominios.TIPO_OPERA_PROVISION.equalsIgnoreCase(tipoOperacion)
										? Dominios.COMISION_TARIFA_ESPECIAL_PROVISION
										: Dominios.COMISION_TARIFA_ESPECIAL_RECOLECCION)
								.build());

				return null;
	        	
	        default:
	            return null;
	    }
	}
	
	private Map<String, ListaDetalleDTO> getLlaveProcesamiento(ValidacionArchivoDTO validacionArchivo,
			Map<String, ListaDetalleDTO> detalleDefinicionMap) {

		String entidadProcesamiento = validacionArchivo.getValidacionLineas().get(0).getContenido().get(0);
		String agrupadorProcesamiento = validacionArchivo.getMaestroDefinicion().getIdMaestroDefinicionArchivo();

		String fechaRawProcesamiento = validacionArchivo.getValidacionLineas().get(0).getContenido().get(3);
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
		        .appendLiteral('/')
		        .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
		        .appendLiteral('/')
		        .appendValue(ChronoField.YEAR, 4)
		        .toFormatter();
		
		LocalDate fechaProcesamiento;

		try {
			fechaProcesamiento = LocalDate.parse(fechaRawProcesamiento, formatter);
		} catch (Exception e) {
			fechaProcesamiento = LocalDate.of(2000, 1, 1);		 
		}
		
		String fechaFormated = fechaProcesamiento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String codigoCiiuFondoProcesamiento = validacionArchivo.getValidacionLineas().get(0).getContenido().get(9);
		String fileProcesamiento = validacionArchivo.getNombreArchivo();

		List<Transportadoras> transportadorasProcesamiento = transportadorasRepository.findAll();

		String codigoTdvProcesamiento = null;
		String nombreTdvProcesamiento = null;

		for (Transportadoras t : transportadorasProcesamiento) {
			if (fileProcesamiento.contains(t.getNombreTransportadora())) {
				codigoTdvProcesamiento = t.getAbreviatura();
				nombreTdvProcesamiento = t.getNombreTransportadora();
				break;
			}
		}

		IPuntoInterno puntoInternoProcesamiento = puntosRepository.findPuntoInterno(
				validacionArchivo.getValidacionLineas().get(0).getContenido().get(7),
				validacionArchivo.getValidacionLineas().get(0).getContenido().get(8), codigoTdvProcesamiento, entidadProcesamiento);

		Integer puntoFondoProcesamiento = puntosRepository.findPuntoFondo(entidadProcesamiento, Constantes.PUNTO_FONDO, codigoCiiuFondoProcesamiento,
				codigoTdvProcesamiento, nombreTdvProcesamiento, validacionArchivo.getValidacionLineas().get(0).getContenido().get(10));

		String tipoOperacion = costosProcesamientoService.getOperacionProcesamiento(
				puntoInternoProcesamiento != null ? puntoInternoProcesamiento.getTipoPunto() : "UNDEFINED"
			);

		String codigoPunto1;
		String codigoPunto2;

		if ("RECOLECCION".equalsIgnoreCase(tipoOperacion) || "RETIRO".equalsIgnoreCase(tipoOperacion)) {
		    codigoPunto1 = (puntoInternoProcesamiento != null && puntoInternoProcesamiento.getCodigoPunto() != null)
		                   ? puntoInternoProcesamiento.getCodigoPunto().toString() : "0";
		    codigoPunto2 = (puntoFondoProcesamiento != null) ? puntoFondoProcesamiento.toString() : "0";
		} else {
		    codigoPunto1 = (puntoFondoProcesamiento != null) ? puntoFondoProcesamiento.toString() : "0";
		    codigoPunto2 = (puntoInternoProcesamiento != null && puntoInternoProcesamiento.getCodigoPunto() != null)
		                   ? puntoInternoProcesamiento.getCodigoPunto().toString() : "0";
		}

		String entradaSalida = archivosLiquidacionService.getEntradaSalida(tipoOperacion,
				Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO);


	    detalleDefinicionMap.put("codigo_ciiu_fondo", ListaDetalleDTO.builder()
	        .nombreCampo("codigo_ciiu_fondo")
	        .tipoDato("T")
	        .valor(codigoCiiuFondoProcesamiento)
	        .build());
	    
	    detalleDefinicionMap.put("fecha_servicio_transporte", ListaDetalleDTO.builder()
		        .nombreCampo("fecha_servicio_transporte")
		        .tipoDato("T")
		        .valor(fechaFormated)
		        .build());

	    detalleDefinicionMap.put("codigo_tdv", ListaDetalleDTO.builder()
	        .nombreCampo("codigo_tdv")
	        .tipoDato("T")
	        .valor(codigoTdvProcesamiento)
	        .build());

	    detalleDefinicionMap.put("codigoPunto1", ListaDetalleDTO.builder()
	        .nombreCampo("codigoPunto1")
	        .tipoDato("T")
	        .valor(codigoPunto1)
	        .build());

	    detalleDefinicionMap.put("codigoPunto2", ListaDetalleDTO.builder()
	        .nombreCampo("codigoPunto2")
	        .tipoDato("T")
	        .valor(codigoPunto2)
	        .build());

	    detalleDefinicionMap.put("entradaSalida", ListaDetalleDTO.builder()
	        .nombreCampo("entradaSalida")
	        .tipoDato("T")
	        .valor(entradaSalida)
	        .build());

	    detalleDefinicionMap.put("nombreArchivo", ListaDetalleDTO.builder()
	        .nombreCampo("nombreArchivo")
	        .tipoDato("T")
	        .valor(fileProcesamiento)
	        .build());

	    detalleDefinicionMap.put("agrupador", ListaDetalleDTO.builder()
	        .nombreCampo("agrupador")
	        .tipoDato("T")
	        .valor(agrupadorProcesamiento)
	        .build());

	    return detalleDefinicionMap;
	}
	
	
	private Map<String, ListaDetalleDTO> getLlaveTransporte(ValidacionArchivoDTO validacionArchivo,
			Map<String, ListaDetalleDTO> detalleDefinicionMap) {

		String entidad = validacionArchivo.getValidacionLineas().get(0).getContenido().get(0);
		String agrupador = validacionArchivo.getMaestroDefinicion().getIdMaestroDefinicionArchivo();

		String fechaRaw = validacionArchivo.getValidacionLineas().get(0).getContenido().get(3);

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
		        .appendLiteral('/')
		        .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
		        .appendLiteral('/')
		        .appendValue(ChronoField.YEAR, 4)
		        .toFormatter();
		
		LocalDate fecha;

		try {
		    fecha = LocalDate.parse(fechaRaw, formatter);
		} catch (Exception e) {
		    fecha = LocalDate.of(2000, 1, 1);		 
		}
		
		String fechaFormated = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String codigoCiiuFondo = validacionArchivo.getValidacionLineas().get(0).getContenido().get(10);
		String file = validacionArchivo.getNombreArchivo();

		List<Transportadoras> transportadorasTransporte = transportadorasRepository.findAll();

		String codigoTdv = null;
		String nombreTdv = null;

		for (Transportadoras t : transportadorasTransporte) {
			if (file.contains(t.getNombreTransportadora())) {
				codigoTdv = t.getAbreviatura();
				nombreTdv = t.getNombreTransportadora();
				break;
			}
		}

		IPuntoInterno puntoInterno = puntosRepository.findPuntoInterno(
				validacionArchivo.getValidacionLineas().get(0).getContenido().get(6),
				validacionArchivo.getValidacionLineas().get(0).getContenido().get(7), codigoTdv, entidad);

		Integer puntoFondo = puntosRepository.findPuntoFondo(entidad, Constantes.PUNTO_FONDO, codigoCiiuFondo,
				codigoTdv, nombreTdv, validacionArchivo.getValidacionLineas().get(0).getContenido().get(11));

		String tipoServicio = validacionArchivo.getValidacionLineas().get(0).getContenido().get(12);

		String entradaSalida = archivosLiquidacionService.getEntradaSalida(tipoServicio,
				Constantes.MAESTRO_ARCHIVO_TRANSPORTE);
		
		String codigoPunto1;
		String codigoPunto2;

		if ("SALIDA".equalsIgnoreCase(entradaSalida)) {
		    codigoPunto1 = puntoFondo != null ? puntoFondo.toString() : "0";
		    codigoPunto2 = (puntoInterno != null && puntoInterno.getCodigoPunto() != null)
		                   ? puntoInterno.getCodigoPunto().toString() : "0";
		} else {
		    codigoPunto1 = (puntoInterno != null && puntoInterno.getCodigoPunto() != null)
		                   ? puntoInterno.getCodigoPunto().toString() : "0";
		    codigoPunto2 = puntoFondo != null ? puntoFondo.toString() : "0";
		}


	    detalleDefinicionMap.put("codigo_ciiu_fondo", ListaDetalleDTO.builder()
	        .nombreCampo("codigo_ciiu_fondo")
	        .tipoDato("T")
	        .valor(codigoCiiuFondo != null ? codigoCiiuFondo : "0")
	        .build());
	    
	    detalleDefinicionMap.put("fecha_servicio_transporte", ListaDetalleDTO.builder()
		        .nombreCampo("fecha_servicio_transporte")
		        .tipoDato("T")
		        .valor(fechaFormated)
		        .build());

	    detalleDefinicionMap.put("codigo_tdv", ListaDetalleDTO.builder()
	        .nombreCampo("codigo_tdv")
	        .tipoDato("T")
	        .valor(codigoTdv)
	        .build());

	    detalleDefinicionMap.put("codigoPunto1", ListaDetalleDTO.builder()
	        .nombreCampo("codigoPunto1")
	        .tipoDato("T")
	        .valor(codigoPunto1)
	        .build());

	    detalleDefinicionMap.put("codigoPunto2", ListaDetalleDTO.builder()
	        .nombreCampo("codigoPunto2")
	        .tipoDato("T")
	        .valor(codigoPunto2)
	        .build());

	    detalleDefinicionMap.put("entradaSalida", ListaDetalleDTO.builder()
	        .nombreCampo("entradaSalida")
	        .tipoDato("T")
	        .valor(entradaSalida != null ? entradaSalida : "SIN_ENTRADA_SALIDA")
	        .build());

	    detalleDefinicionMap.put("nombreArchivo", ListaDetalleDTO.builder()
	        .nombreCampo("nombreArchivo")
	        .tipoDato("T")
	        .valor(file)
	        .build());

	    detalleDefinicionMap.put("agrupador", ListaDetalleDTO.builder()
	        .nombreCampo("agrupador")
	        .tipoDato("T")
	        .valor(agrupador)
	        .build());

	    return detalleDefinicionMap;
	}

}
