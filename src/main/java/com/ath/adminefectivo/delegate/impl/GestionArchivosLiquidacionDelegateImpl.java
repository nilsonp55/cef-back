package com.ath.adminefectivo.delegate.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.IArchivosCargadosDelegate;
import com.ath.adminefectivo.delegate.IArchivosLiquidacionDelegate;
import com.ath.adminefectivo.delegate.IGestionArchivosLiquidacionDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.CostosProcesamiento;
import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.repositories.IOperacionesLiquidacionTransporte;
import com.ath.adminefectivo.repositories.MaestroLlavesCostosRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import com.ath.adminefectivo.service.ICostosProcesamientoService;
import com.ath.adminefectivo.service.ICostosTransporteService;
import com.ath.adminefectivo.service.IDetalleLiquidacionProcesamiento;
import com.ath.adminefectivo.service.IDetalleLiquidacionTransporte;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GestionArchivosLiquidacionDelegateImpl implements IGestionArchivosLiquidacionDelegate {
		
	@Autowired
	IArchivosLiquidacionDelegate archivosLiquidacion;
	
	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	@Autowired
	ICostosTransporteService costosTransporteService;
	
	@Autowired
	ICostosProcesamientoService costosProcesamientoService;
	
	@Autowired
	IConciliacionOperacionesProcesamientoService operacionesLiquidacion;
	
	@Autowired
	ICostosTransporteService operacionesLiquidacionTransporte;
	
	@Autowired 
	MaestroLlavesCostosRepository maestroLlavesCostosRepository;

	
	@Override
	public Page<ArchivosLiquidacionDTO> getAll(String agrupador, Pageable page) {
		 
		Set<String> estadoConciliacion = new HashSet<>();
		estadoConciliacion.add(Constantes.ESTADO_CARGUE_ERROR);
		estadoConciliacion.add(Dominios.ESTADO_VALIDACION_EN_CONCILIACION);
		
		// Obtener archivos cargados con estado ERRADO y EN_CONCILIACION
	    Page<ArchivosCargadosDTO> archivosCargadosPage = archivosCargadosService.getAllByAgrupadorAndEstadoCargue(agrupador, estadoConciliacion, page);
	    
	    // Crear una lista para almacenar los archivos de liquidación
	    List<ArchivosLiquidacionDTO> dtoResponseList = new ArrayList<>();   
	    
	    // Recorrer los archivos cargados y construir los DTO de liquidación
	    archivosCargadosPage.forEach(archivoCargado -> {
	        ArchivosLiquidacionDTO archivoLiquidacionDTO = new ArchivosLiquidacionDTO();
	        archivoLiquidacionDTO.setNombreArchivo(archivoCargado.getNombreArchivo());
	        archivoLiquidacionDTO.setNombreArchivoCompleto(archivoCargado.getNombreArchivo());
	        archivoLiquidacionDTO.setIdArchivo(archivoCargado.getIdArchivo());
	        archivoLiquidacionDTO.setIdArchivodb(archivoCargado.getIdArchivo());
	        archivoLiquidacionDTO.setEstado(archivoCargado.getEstadoCargue());
	        
	        dtoResponseList.add(archivoLiquidacionDTO);
	    });

		return new PageImpl<>(archivosLiquidacion.getAll(0, 0, false, "", Optional.of(dtoResponseList)).getContent(),
				archivosCargadosPage.getPageable(), archivosCargadosPage.getTotalElements());
	}
	
	
	/**
	 * Metodo encargado de iterar todos los archivos enviados para aceptar conciliacion
	 * 
	 * @param aceptarAchivos
	 * @return Page<ArchivosLiquidacionDTO>
	 * @author hector.mercado
	 */
	@Override
	public Page<ArchivosLiquidacionDTO> aceptarArchivos(ArchivosLiquidacionListDTO archivosAceptar) {
	
		 List<ArchivosLiquidacionDTO> responseList = new ArrayList<>();
		 
		 archivosAceptar.getValidacionArchivo().forEach(f->{
			
			 var aceptado = this.aceptarAchivo(f);
			 responseList.add(aceptado);
			
		 });
		
		 return new PageImpl<>(responseList);
	}
	
	/**
	 * Metodo encargado de realizar la validaciones  de registros para saber si se puede aceptar y 
	 * conciliar el archivo
	 * 
	 * @param archivoAceptar
	 * @return void
	 * @author hector.mercado
	 */
	@Override
	public ArchivosLiquidacionDTO aceptarAchivo(ArchivosLiquidacionDTO archivoAceptar) {
		
		var idArchivo = archivoAceptar.getIdArchivo();
		//1. Obtener todos los registros que pertenecen al archivo
		var archivoCargado =  archivosCargadosService.consultarArchivoById(idArchivo);
		var idModelo = archivoCargado.getIdModeloArchivo();

		archivoAceptar.setEstado(Constantes.ESTADO_NO_CONCILIADO);
		
		if (idModelo.equalsIgnoreCase(Constantes.MAESTRO_ARCHIVO_TRANSPORTE))
		{
			return aceptarArchivoTransporte(archivoAceptar, archivoCargado);
		}

		if (idModelo.equalsIgnoreCase(Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO))
		{
			//Validacion de transporte
			return aceptarArchivoProcesamiento(archivoAceptar, archivoCargado);
		}

		return archivoAceptar;
		
	}
	
	private ArchivosLiquidacionDTO aceptarArchivoTransporte(ArchivosLiquidacionDTO archivoAceptar,
			ArchivosCargados archivoCargado) {
		var filtrosT = new ParametrosFiltroCostoTransporteDTO();
		var codigoTransporteTDV = "";
		Pageable pr = PageRequest.of(1, 1);

		Long conciliados = 0l;
		Date fechaServicioMin;
		Date fechaServicioMax;
		Integer totalTransporte = 0;
		Set<Integer> idArchivosProcesamientoUnicos = new HashSet<>();
		AtomicBoolean validacionConcilProcesamiento = new AtomicBoolean(true);
		boolean llavesCoinciden = false;
		boolean validacionConcilProcCompleta = false;

		// Validacion de transporte
		var costosTransporte = costosTransporteService
				.obtenerDetalleTransportePorIdArchivo(archivoAceptar.getIdArchivo().intValue());

		totalTransporte = costosTransporte.size();

		if (totalTransporte > 0) {
			codigoTransporteTDV = costosTransporte.get(0).getCodigoPuntoCargo();// tiene que ser getCodigoTDV
			filtrosT.setCodigoPuntoCargo(codigoTransporteTDV);
			filtrosT.setPage(pr);

			conciliados = costosTransporte.stream()
					.filter(c -> c.getModulo().equalsIgnoreCase(Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS))
					.count();

			// Se recorre la lista de costosProcesamiento y se obtiene, por cada elemento no
			// nulo,
			// la lista de detalles mediante la llamada a
			// 'obtenerEstadoProcesamientoPorLlave'.
			// Por cada detalle recibido, se valida que:
			// 1. El campo 'modulo' sea igual a "CONCILIADAS".
			// 2. El campo 'idArchivoCargado' sea el mismo en todos los registros (solo se
			// permite un valor único).
			// Si alguna de estas condiciones no se cumple, se marca la variable
			// 'validacionConciliacion' como false.
		
			for (IDetalleLiquidacionTransporte procesamiento : costosTransporte) {
			    BigInteger llave = procesamiento.getIdLlavesMaestroTdv();
			    if (llave == null) continue;

			    List<IDetalleLiquidacionProcesamiento> detallesProcesamiento = operacionesLiquidacion.obtenerEstadoProcesamientoPorLlave(llave);
			    
			    if (detallesProcesamiento.isEmpty()) {
			    	validacionConcilProcesamiento.set(false);
			        break;
			    }
			    
			    for (IDetalleLiquidacionProcesamiento detalleProcesamiento : detallesProcesamiento) {
			        if (!Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS.equals(detalleProcesamiento.getModulo())) {
			        	validacionConcilProcesamiento.set(false);
			            break;
			        }

			        idArchivosProcesamientoUnicos.add(detalleProcesamiento.getIdArchivoCargado());

			        if (idArchivosProcesamientoUnicos.size() > 1) {
			        	validacionConcilProcesamiento.set(false);
			            break;
			        }
			    }

			    if (!validacionConcilProcesamiento.get()) break;
			}

			validacionConcilProcCompleta = validacionConcilProcesamiento.get();
			Integer idArchivoProcesamientoConciliado = (validacionConcilProcesamiento.get() && !idArchivosProcesamientoUnicos.isEmpty())
				    ? idArchivosProcesamientoUnicos.iterator().next()
				    : null;

			//Consulta los registros del archivo que contiene la coincidencias de las llaves en transporte
			//Para comparar que ambos archivos coincidan con el mismo numero y valor de llaves
			if (validacionConcilProcCompleta) {
				llavesCoinciden = validarCoincidenciaLlavesTransporteProcesamiento(costosTransporte,
						idArchivoProcesamientoConciliado);
			}
			
			fechaServicioMin = costosTransporte.stream()
				    .map(c -> c.getFechaServicioTransporte() != null ? new Timestamp(c.getFechaServicioTransporte().getTime()) : null)
				    .filter(Objects::nonNull)
				    .min(Timestamp::compareTo)
				    .orElse(null);
			
			fechaServicioMax = costosTransporte.stream()
				    .map(c -> c.getFechaServicioTransporte() != null ? new Timestamp(c.getFechaServicioTransporte().getTime()) : null)
				    .filter(Objects::nonNull)
				    .max(Timestamp::compareTo)
				    .orElse(null);


			filtrosT.setFechaServicioTransporte(fechaServicioMin);
			filtrosT.setFechaServicioTransporteFinal(fechaServicioMax);
		}

		// Validaciones necesarias para continuar con el proceso:
		// 1. Todas las llaves del archivo de transporte deben estar en estado 'CONCILIADAS'.
		// 2. Esas mismas llaves también deben existir en el archivo de procesamiento, y en estado 'CONCILIADAS'.
		// 3. El archivo de procesamiento no debe contener llaves adicionales que no estén presentes en el archivo de transporte.
		if (totalTransporte.longValue() == conciliados && validacionConcilProcCompleta && llavesCoinciden) {
			var hayLiquidadasNoCobradas = costosTransporteService.getLiquidadasNoCobradasTransporte(filtrosT);
			var lista = hayLiquidadasNoCobradas.getContent();

			if (lista.isEmpty()) {
				// puede cerrar proceso
				archivoCargado.setEstado(Constantes.ESTADO_CONCILIACION_ACEPTADO);
				archivosCargadosService.actualizarArchivosCargados(archivoCargado);
				costosTransporteService.aceptarConciliacionRegistro(archivoAceptar.getIdArchivo());
				archivoAceptar.setEstado("CONCILIADO");
				
				Set<BigInteger> llaves = obtenerLlavesDesdeTransporte(costosTransporte);
				maestroLlavesCostosRepository.actualizarEstadoAndObservacionesPorLlaves(new ArrayList<>(llaves),
						Constantes.ESTADO_CONCILIACION_ACEPTADO, archivoAceptar.getObservacion());
				
			} else {
				archivoAceptar.setEstado(Constantes.ESTADO_NO_CONCILIADO);
			}

		}

		return archivoAceptar;

	}
	
	private ArchivosLiquidacionDTO aceptarArchivoProcesamiento(ArchivosLiquidacionDTO archivoAceptar, ArchivosCargados archivoCargado)
	{
		var filtrosP = new ParametrosFiltroCostoProcesamientoDTO();
		var codigoProcesamientoTDV = "";
		
		Long conciliados =0l ;
		Integer totalProcesamiento = 0;
		Date fechaServicioMax;
		Date fechaServicioMin;
		Set<Integer> idArchivosUnicos = new HashSet<>();
		AtomicBoolean validacionConciliacion = new AtomicBoolean(true);
		boolean llavesCoinciden = false;
		boolean validacionConciliacionCompleta = false;
		
		//Validacion de procesamiento
		var costosProcesamiento = operacionesLiquidacion
				.obtenerDetalleProcesamientoPorIdArchivo(archivoAceptar.getIdArchivo().intValue());
		totalProcesamiento = costosProcesamiento.size();
		
		if (totalProcesamiento>0)
		{
			codigoProcesamientoTDV = costosProcesamiento.get(0).getCodigoPuntoCargo();// tiene que ser getCodigoTDV
			filtrosP.setCargoPointCode(codigoProcesamientoTDV);
			
			conciliados = costosProcesamiento.stream()
                    .filter(c -> c.getModulo().equalsIgnoreCase(Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS))
                    .count();
			
			// Se recorre la lista de costosProcesamiento y se obtiene, por cada elemento no nulo,
			// la lista de detalles mediante la llamada a 'obtenerEstadoProcesamientoPorLlave'.
			// Por cada detalle recibido, se valida que:
			// 1. El campo 'modulo' sea igual a "CONCILIADAS".
			// 2. El campo 'idArchivoCargado' sea el mismo en todos los registros (solo se permite un valor único).
			// Si alguna de estas condiciones no se cumple, se marca la variable 'validacionConciliacion' como false.
			
			for (IDetalleLiquidacionProcesamiento procesamiento : costosProcesamiento) {
			    BigInteger llave = procesamiento.getIdLlavesMaestroTdv();
			    if (llave == null) continue;

			    List<IDetalleLiquidacionTransporte> detalles = operacionesLiquidacionTransporte.obtenerEstadoTransportePorLlave(llave);
			    
			    if (detalles.isEmpty()) {
			        validacionConciliacion.set(false);
			        break;
			    }
			    
			    for (IDetalleLiquidacionTransporte detalle : detalles) {
			        if (!"CONCILIADAS".equals(detalle.getModulo())) {
			            validacionConciliacion.set(false);
			            break;
			        }

			        idArchivosUnicos.add(detalle.getIdArchivoCargado());

			        if (idArchivosUnicos.size() > 1) {
			            validacionConciliacion.set(false);
			            break;
			        }
			    }

			    if (!validacionConciliacion.get()) break;
			}
			
			// validacionConciliacionCompleta = true si las llaves en archivo de transporte
			// Tambien existen y se encuentran CONCILIADAS
			validacionConciliacionCompleta = validacionConciliacion.get();
			Integer idArchivoConciliado = (validacionConciliacion.get() && !idArchivosUnicos.isEmpty())
				    ? idArchivosUnicos.iterator().next()
				    : null;
			
			//Consulta los registros del archivo que contiene la coincidencias de las llaves en transporte
			//Para comparar que ambos archivos coincidan con el mismo numero y valor de llaves
			if (validacionConciliacionCompleta) {
				llavesCoinciden = validarCoincidenciaLlavesProcesamientoTransporte(costosProcesamiento,
						idArchivoConciliado);
			}
			
			fechaServicioMin = costosProcesamiento.stream()
				    .map(c -> c.getFechaServicioTransporte() != null ? new Timestamp(c.getFechaServicioTransporte().getTime()) : null)
				    .filter(Objects::nonNull)
				    .min(Timestamp::compareTo)
				    .orElse(null);
			
			fechaServicioMax = costosProcesamiento.stream()
				    .map(c -> c.getFechaServicioTransporte() != null ? new Timestamp(c.getFechaServicioTransporte().getTime()) : null)
				    .filter(Objects::nonNull)
				    .max(Timestamp::compareTo)
				    .orElse(null);

			filtrosP.setProcessServiceDate(fechaServicioMin);
			filtrosP.setFinalProcessServiceDate(fechaServicioMax);
		}
		
		// Validaciones necesarias para continuar con el proceso:
		// 1. Todas las llaves del archivo de procesamiento deben estar en estado 'CONCILIADAS'.
		// 2. Esas mismas llaves también deben existir en el archivo de transporte, y en estado 'CONCILIADAS'.
		// 3. El archivo de transporte no debe contener llaves adicionales que no estén presentes en el archivo de procesamiento.
		if (totalProcesamiento.longValue()==conciliados && validacionConciliacionCompleta && llavesCoinciden)
		{
			
			var hayLiquidadasNoCobradas = operacionesLiquidacion.getLiquidadasNoCobradasProcesamiento(filtrosP);
			var lista = hayLiquidadasNoCobradas.getContent();
			
			if(lista.isEmpty())
			{
				//puede cerrar proceso
				archivoCargado.setEstado(Constantes.ESTADO_CONCILIACION_ACEPTADO);
				archivosCargadosService.actualizarArchivosCargados(archivoCargado);
				costosProcesamientoService.aceptarConciliacionRegistro(archivoAceptar.getIdArchivo());
				archivoAceptar.setEstado("CONCILIADO");
				
				Set<BigInteger> llaves = obtenerLlavesDesdeProcesamiento(costosProcesamiento);
				maestroLlavesCostosRepository.actualizarEstadoAndObservacionesPorLlaves(new ArrayList<>(llaves),
						Constantes.ESTADO_CONCILIACION_ACEPTADO, archivoAceptar.getObservacion());
			}
			else
			{
				archivoAceptar.setEstado(Constantes.ESTADO_NO_CONCILIADO);
			}
		
		}
		
		return archivoAceptar;
	
	}
	
	private boolean validarCoincidenciaLlavesProcesamientoTransporte(
	        List<IDetalleLiquidacionProcesamiento> costosProcesamiento,
	        Integer idArchivoConciliado) {

	    Set<BigInteger> llavesProcesamiento = obtenerLlavesDesdeProcesamiento(costosProcesamiento);
	    List<IDetalleLiquidacionTransporte> detallesTransporte =
	            operacionesLiquidacionTransporte.obtenerDetalleTransportePorIdArchivo(idArchivoConciliado);
	    Set<BigInteger> llavesTransporte = obtenerLlavesDesdeTransporte(detallesTransporte);

	    return compararLlaves(llavesProcesamiento, llavesTransporte);
	}
	
	private boolean validarCoincidenciaLlavesTransporteProcesamiento(
	        List<IDetalleLiquidacionTransporte> transporte,
	        Integer idArchivoConciliado) {

	    Set<BigInteger> llavesTransporte = obtenerLlavesDesdeTransporte(transporte);
	    List<IDetalleLiquidacionProcesamiento> detallesProcesamiento =
	            operacionesLiquidacion.obtenerDetalleProcesamientoPorIdArchivo(idArchivoConciliado);
	    Set<BigInteger> llavesProcesamiento = obtenerLlavesDesdeProcesamiento(detallesProcesamiento);

	    return compararLlaves(llavesTransporte, llavesProcesamiento);
	}
	
	private Set<BigInteger> obtenerLlavesDesdeProcesamiento(List<IDetalleLiquidacionProcesamiento> lista) {
	    return lista.stream()
	            .map(IDetalleLiquidacionProcesamiento::getIdLlavesMaestroTdv)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());
	}

	private Set<BigInteger> obtenerLlavesDesdeTransporte(List<IDetalleLiquidacionTransporte> lista) {
	    return lista.stream()
	            .map(IDetalleLiquidacionTransporte::getIdLlavesMaestroTdv)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());
	}

	private boolean compararLlaves(Set<BigInteger> origen, Set<BigInteger> destino) {
	    return origen.equals(destino);
	}

	

}
