package com.ath.adminefectivo.delegate.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
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
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import com.ath.adminefectivo.service.ICostosProcesamientoService;
import com.ath.adminefectivo.service.ICostosTransporteService;

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
	
	@Override
	public Page<ArchivosLiquidacionDTO> getAll(String agrupador, Pageable page) {
		 
		Set<String> estadoConciliacion = new HashSet<>();
		estadoConciliacion.add(Constantes.ESTADO_CARGUE_ERROR);
		estadoConciliacion.add(Constantes.ESTADO_CARGUE_VALIDO);
		
		// Obtener archivos cargados con estado ERRADO y OK
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
	    	  
	    Page<ArchivosLiquidacionDTO> responsePage = archivosLiquidacion.getAll(0, 0, false, "", Optional.of(dtoResponseList));
	    	    
		return responsePage;
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
		archivoAceptar.setEstado("NO CONCILIADO");
		
		if (idModelo.equalsIgnoreCase(Constantes.MAESTRO_ARCHIVO_TRANSPORTE))
		{
			archivoAceptar = aceptarArchivoTransporte(archivoAceptar, archivoCargado);
		}
		
		if (idModelo.equalsIgnoreCase(Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO))
		{
			//Validacion de transporte
			archivoAceptar = aceptarArchivoProcesamiento(archivoAceptar, archivoCargado);
			
		}
		
		return archivoAceptar;
		
	}
	
	private ArchivosLiquidacionDTO aceptarArchivoTransporte(ArchivosLiquidacionDTO archivoAceptar, ArchivosCargados archivoCargado)
	{
		var filtrosT = new ParametrosFiltroCostoTransporteDTO();
		var codigoTransporteTDV = "";
		var numRegistros = archivoCargado.getNumeroRegistros();
		Pageable pr = PageRequest.of(1, 1);
		
		Long conciliados =0l ;
		Date fechaServicioMin;
		Date fechaServicioMax;
		Integer totalTransporte = 0;
		
		//Validacion de transporte
		var costosTransporte = costosTransporteService.getByIdArchivoCargado(archivoAceptar.getIdArchivo());
		totalTransporte = costosTransporte.size();
		
		if (totalTransporte>0)
		{
			codigoTransporteTDV = costosTransporte.get(0).getCodigoPuntoCargoTransporte();// tiene que ser getCodigoTDV
			filtrosT.setCodigoPuntoCargo(codigoTransporteTDV);
			filtrosT.setPage(pr);
			
			conciliados = costosTransporte.stream()
                    .filter(c -> c.getEstadoConciliacionTransporte().equalsIgnoreCase(Constantes.ESTADO_CONCILIACION_MANUAL)||
                    		     c.getEstadoConciliacionTransporte().equalsIgnoreCase(Constantes.ESTADO_CONCILIACION_AUTOMATICO) )
                    .count();
			
			fechaServicioMin = costosTransporte.stream()
				      .map(CostosTransporte::getFechaServicioTransporte)
				      .min(Date::compareTo)
				      .get();
			
			fechaServicioMax = costosTransporte.stream()
				      .map(CostosTransporte::getFechaServicioTransporte)
				      .max(Date::compareTo)
				      .get();

			filtrosT.setFechaServicioTransporte(fechaServicioMin);
			filtrosT.setFechaServicioTransporteFinal(fechaServicioMax);
		}
		
		//Todos los registros se encuentran conciliados
		if (totalTransporte.longValue()==conciliados && numRegistros.longValue()==conciliados)
		{
			var hayLiquidadasNoCobradas = costosTransporteService.getLiquidadasNoCobradasTransporte(filtrosT);
			var lista = hayLiquidadasNoCobradas.getContent();
			
			if(lista.isEmpty())
			{
				//puede cerrar proceso
				archivoCargado.setEstado(Constantes.ESTADO_CONCILIACION_ACEPTADO);
				archivosCargadosService.actualizarArchivosCargados(archivoCargado);
				costosTransporteService.aceptarConciliacionRegistro(archivoAceptar.getIdArchivo());
				archivoAceptar.setEstado("CONCILIADO");
			}
			else
			{
				archivoAceptar.setEstado("NO CONCILIADO");
			}
		
	   }
		
		return archivoAceptar;
	
	}
	
	
	private ArchivosLiquidacionDTO aceptarArchivoProcesamiento(ArchivosLiquidacionDTO archivoAceptar, ArchivosCargados archivoCargado)
	{
		var filtrosP = new ParametrosFiltroCostoProcesamientoDTO();
		var numRegistros = archivoCargado.getNumeroRegistros();
		var codigoProcesamientoTDV = "";
		
		Long conciliados =0l ;
		Integer totalProcesamiento = 0;
		Date fechaServicioMax;
		Date fechaServicioMin;
		
		//Validacion de procesamiento
		var costosProcesamiento = costosProcesamientoService.getByIdArchivoCargado(archivoAceptar.getIdArchivo());
		totalProcesamiento = costosProcesamiento.size();
		
		if (totalProcesamiento>0)
		{
			codigoProcesamientoTDV = costosProcesamiento.get(0).getCodigoPuntoCargo();// tiene que ser getCodigoTDV
			filtrosP.setCargoPointCode(codigoProcesamientoTDV);
			
			conciliados = costosProcesamiento.stream()
                    .filter(c -> c.getEstadoConciliacion().equalsIgnoreCase(Constantes.ESTADO_CONCILIACION_MANUAL)||
                    		     c.getEstadoConciliacion().equalsIgnoreCase(Constantes.ESTADO_CONCILIACION_AUTOMATICO) )
                    .count();
			
			fechaServicioMin = costosProcesamiento.stream()
				      .map(CostosProcesamiento::getFechaServicioTransporte)
				      .min(Date::compareTo)
				      .get();
			
			fechaServicioMax = costosProcesamiento.stream()
				      .map(CostosProcesamiento::getFechaServicioTransporte)
				      .max(Date::compareTo)
				      .get();

			filtrosP.setProcessServiceDate(fechaServicioMin);
			filtrosP.setFinalProcessServiceDate(fechaServicioMax);
		}
		
		//Todos los registros se encuentran conciliados
		if (totalProcesamiento.longValue()==conciliados && numRegistros.longValue()==conciliados)
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
			}
			else
			{
				archivoAceptar.setEstado("NO CONCILIADO");
			}
		
		}
		
		return archivoAceptar;
	
	}
	

}
