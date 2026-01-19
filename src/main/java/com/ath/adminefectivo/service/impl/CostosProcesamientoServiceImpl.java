package com.ath.adminefectivo.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.entities.CostosProcesamiento;
import com.ath.adminefectivo.repositories.ICostosProcesamientoRepository;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.repositories.MaestroLlavesCostosRepository;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.ICostosProcesamientoService;
import com.ath.adminefectivo.service.IPuntoInterno;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.utils.UtilsString;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CostosProcesamientoServiceImpl implements ICostosProcesamientoService {

	
	@Autowired
	ITransportadorasService transportadorasService;
	
	@Autowired
	ICostosProcesamientoRepository costosprocesamientoRepository;
	
	@Autowired
	IPuntosRepository puntosRepository;
	
	@Autowired
	IArchivosLiquidacionService archivosLiquidacionService;
	
	@Autowired 
	MaestroLlavesCostosRepository maestroLlavesCostosRepository;
	

	@Override
	public Long persistir(ValidacionArchivoDTO validacionArchivo) {
		
		List<CostosProcesamiento> listCosto = costoFromValidacionArchivo(validacionArchivo);
		
		saveAll(listCosto);
		
		persistirMaestroLlavesProcesamiento();
		
		return null;
	}
	
	@Override
	public void saveAll(List<CostosProcesamiento> costo) {
		
		costosprocesamientoRepository.saveAll(costo);
		
	}


	private List<CostosProcesamiento> costoFromValidacionArchivo(ValidacionArchivoDTO validacionArchivo) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		List<String> listaDominioFecha = new ArrayList<>();
		listaDominioFecha.add(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);

		List<CostosProcesamiento> list = new ArrayList<>();

		// Obtener Lineas Validadas
		List<ValidacionLineasDTO> lineas = validacionArchivo.getValidacionLineas();

		var transportadora = transportadorasService.getcodigoTransportadora(validacionArchivo.getDescripcion());

		lineas.forEach(f -> {
			CostosProcesamiento costo = new CostosProcesamiento();
			var contenido = f.getContenido();
			costo.setEntidad(contenido.get(0));
			costo.setFactura(contenido.get(1));
			costo.setTipoRegistro(contenido.get(2));
			costo.setFechaServicioTransporte(UtilsString.toDate(contenido.get(3), listaDominioFecha));
			costo.setFechaProcesamiento(UtilsString.toDate(contenido.get(4), listaDominioFecha));
			costo.setIdentificacionCliente(contenido.get(5));
			costo.setRazonSocial(contenido.get(6));
			costo.setCodigoPuntoCargo(contenido.get(7));
			costo.setNombrePuntoCargo(contenido.get(8));
			costo.setCodigoCiiuFondo(UtilsString.toInteger(contenido.get(9)));
			costo.setCiudadFondo(contenido.get(10));
			costo.setNombreTipoServicio(contenido.get(11));
			costo.setMonedaDivisa(contenido.get(12));

			costo.setTrmConversion(UtilsString.toDecimal(contenido.get(13), Constantes.DECIMAL_SEPARATOR));
			costo.setValorProcesadoBillete(UtilsString.toDecimal(contenido.get(14), Constantes.DECIMAL_SEPARATOR));
			costo.setValorProcesadoMoneda(UtilsString.toDecimal(contenido.get(15), Constantes.DECIMAL_SEPARATOR));
			costo.setValorTotalProcesado(UtilsString.toDecimal(contenido.get(16), Constantes.DECIMAL_SEPARATOR));

			costo.setFactorLiquidacion(contenido.get(17));

			costo.setBaseLiquidacion(UtilsString.toDecimal(contenido.get(18), Constantes.DECIMAL_SEPARATOR));
			costo.setTarifa(UtilsString.toDecimal(contenido.get(19), Constantes.DECIMAL_SEPARATOR));
			costo.setCostoSubtotal(UtilsString.toDecimal(contenido.get(20), Constantes.DECIMAL_SEPARATOR));

			costo.setPorcentajeAiu(UtilsString.toInteger(contenido.get(21)));
			costo.setPorcentajeIva(UtilsString.toInteger(contenido.get(22)));

			costo.setIva(UtilsString.toDecimal(contenido.get(23), Constantes.DECIMAL_SEPARATOR));
			costo.setValorTotal(UtilsString.toDecimal(contenido.get(24), Constantes.DECIMAL_SEPARATOR));

			costo.setEstadoConciliacion(Constantes.ESTADO_PROCESO_PENDIENTE);
			costo.setEstado(Constantes.REGISTRO_ACTIVO);

			costo.setObservacionesAth(contenido.get(25));
			costo.setObservacionesTdv(contenido.get(26));

			costo.setIdArchivoCargado(validacionArchivo.getIdArchivo());
			costo.setIdRegistro(f.getNumeroLinea());

			costo.setUsuarioCreacion(Constantes.USUARIO_PROCESA_ARCHIVO);

			costo.setFechaCreacion(timestamp);

			costo.setUsuarioModificacion(null);

			costo.setFechaModificacion(null);

			costo.setCodigoTdv(transportadora);

			IPuntoInterno puntoInterno = puntosRepository.findPuntoInterno(contenido.get(7), contenido.get(8),
					transportadora, contenido.get(0));

			Integer puntoFondo = puntosRepository.findPuntoFondo(contenido.get(0), Constantes.PUNTO_FONDO,
					contenido.get(9), transportadora, validacionArchivo.getDescripcion(), contenido.get(10));

			if (puntoInterno != null) {
				costo.setCodigoPuntoInterno(puntoInterno.getCodigoPunto());
				costo.setTipoPunto(puntoInterno.getTipoPunto());
				costo.setTipoOperacion(getOperacionProcesamiento(puntoInterno.getTipoPunto()));
			}

			if (puntoFondo != null) {
				costo.setCodigoPuntoFondo(puntoFondo);
				costo.setNombreFondo(
						contenido.get(0) + "-" + contenido.get(10) + "-" + validacionArchivo.getDescripcion());
			}

			costo.setEntradaSalida(archivosLiquidacionService.getEntradaSalida(costo.getTipoOperacion(),
					Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO));

			asignarCostosValoresLiquidados(costo);

			list.add(costo);

		});

		return list;
	}

	/**
	 * Metodo encargado de realizar la busqueda en la tabla de Costos_procesamiento 
	 * 
	 * @param entidad
	 * @param fechaServicioTransporte
	 * @param codigoPuntoCargo
	 * @param nombrePuntoCargo
	 * @param ciudadFondo
	 * @param nombreTipoServicio
	 * @return List<CostosProcesamiento>
	 * @author hector.mercado
	 */	
	@Override
	public List<CostosProcesamiento> findAceptados(String entidad, Date fechaServicioTransporte, String codigoPuntoCargo,
			String nombrePuntoCargo, String ciudadFondo, String nombreTipoServicio) {
	
	
		return costosprocesamientoRepository.findByEstadoEntidadFechaServicio(entidad,
																			fechaServicioTransporte,
																			codigoPuntoCargo,
																			nombrePuntoCargo,
																			ciudadFondo,
																			nombreTipoServicio,
																			Dominios.ESTADO_VALIDACION_ACEPTADO);
	}

	public List<CostosProcesamiento> getByIdArchivoCargado(Long idArchivo)
	{
		return costosprocesamientoRepository.findByIdArchivoCargado(idArchivo);
	}
	
	
	public void aceptarConciliacionRegistro(Long idArchivoCargado)
	{
		costosprocesamientoRepository.actualizarEstadoByIdArchivoCargado(idArchivoCargado, Constantes.ESTADO_CONCILIACION_ACEPTADO);
	}
	
	public String getOperacionProcesamiento(String tipoPunto) {
		
		if (tipoPunto == null) {
	        return null;
	    }

	    switch (tipoPunto.toUpperCase()) {
	        case Constantes.PUNTO_OFICINA:
	        case Constantes.PUNTO_SITIO_CLIENTE:
	            return Dominios.TIPO_OPERA_RECOLECCION;
	        case Constantes.PUNTO_BANC_REP:
	            return Dominios.TIPO_OPERA_RETIRO;
	        case Constantes.PUNTO_FONDO:
	            return Dominios.TIPO_OPERA_TRASLADO;
	        case Constantes.PUNTO_BANCO:
	            return Dominios.TIPO_OPERA_VENTA;
	        default:
	            return null;
	    }
	}
	
	private void asignarCostosValoresLiquidados(CostosProcesamiento costo) {

		String tipoServicio = costo.getNombreTipoServicio();
		String factorLiquidacion = costo.getFactorLiquidacion();
		Double costoSubtotal = (costo.getCostoSubtotal() != null) ? costo.getCostoSubtotal().doubleValue() : 0.0;

		if (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_CONSOLIDACION_FAJADA)
				|| tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_VERIFICACION_BILLETE)
				|| tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_VERIFICACION_MONEDA)
				|| (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_CLASIFICACION_BILLETE)
						&& !factorLiquidacion.equalsIgnoreCase(Constantes.FACTOR_BILLETE))
				|| (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_CLASIFICACION_MONEDA)
						&& !factorLiquidacion.equalsIgnoreCase(Constantes.FACTOR_MONEDA))) {
			costo.setClasificacionFajado(costoSubtotal);
		} else if (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_CONSOLIDACION_NO_FAJADA)) {
			costo.setClasificacionNoFajado(costoSubtotal);
		} else if (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_COSTO_PAQUETEO)) {
			costo.setCostoPaqueteo(costoSubtotal);
		} else if (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_CLASIFICACION_MONEDA)
				&& factorLiquidacion.equalsIgnoreCase(Constantes.FACTOR_MONEDA)) {
			costo.setMonedaResiduo(costoSubtotal);
		} else if (tipoServicio.equalsIgnoreCase(Constantes.TIPO_OPERACION_CLASIFICACION_BILLETE)
				&& factorLiquidacion.equalsIgnoreCase(Constantes.FACTOR_BILLETE)) {
			costo.setBilleteResiduo(costoSubtotal);
		}
	}
	
	@Transactional
	@Override
	public void persistirMaestroLlavesProcesamiento() {
		maestroLlavesCostosRepository.insertarMaestroLlaves();
		maestroLlavesCostosRepository.insertarArchivosLlaves();
	}
}
