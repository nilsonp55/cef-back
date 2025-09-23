package com.ath.adminefectivo.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoTransporteDTO;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.ConciliacionCostosTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;

import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.entities.DetallesLiquidacionCostoFlatEntity;
import com.ath.adminefectivo.service.IDetalleLiquidacionProcesamiento;
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCostoFlat;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;
import com.ath.adminefectivo.repositories.ICostosTransporteRepository;
import com.ath.adminefectivo.repositories.IOperacionesLiquidacionTransporte;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.repositories.MaestroLlavesCostosRepository;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICostosProcesamientoService;
import com.ath.adminefectivo.service.ICostosTransporteService;
import com.ath.adminefectivo.service.IDetalleLiquidacionTransporte;
import com.ath.adminefectivo.service.IDetallesLiquidacionCostoService;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;
import com.ath.adminefectivo.service.IOtrosCostosFondoService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IPuntoInterno;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import com.ath.adminefectivo.utils.UtilsParsing;
import com.ath.adminefectivo.utils.UtilsString;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CostosTransporteServiceImpl implements ICostosTransporteService {

	
	@Autowired
	IOperacionesLiquidacionTransporte operacionesLiquidacionTransporte;
	
	@Autowired
	IParametrosLiquidacionCostosService parametrosLiquidacionCostosService;

	@Autowired
	IEstadoConciliacionParametrosLiquidacionService estadoConciliacionParametrosLiquidacionService;
	
	@Autowired
	IValoresLiquidadosFlatService valoresLiquidadosFlatService;
	
	@Autowired
	IDetallesLiquidacionCostoService detallesLiquidacionCostoService;
	
	@Autowired
	ICostosTransporteRepository costosTransporteRepository;
	
	@Autowired
	IBancosService bancoService;

	@Autowired
	ITransportadorasService transportadorasService;
	
	@Autowired
	IPuntosRepository puntosRepository;
	
	@Autowired 
	MaestroLlavesCostosRepository maestroLlavesCostosRepository;
	
	@Autowired
	IArchivosLiquidacionService archivosLiquidacionService;
	
	@Autowired
	IOtrosCostosFondoService otrosCostosFondo;
	
	@Autowired
	ICostosProcesamientoService costosProcesamientoService;
	
	/**
	 * Metodo para realizar la persistencia en la tabla de costo_transporte
	 */
	@Override
	public Long persistir(ValidacionArchivoDTO validacionArchivoTransporte) {

		List<CostosTransporte> listCostoTransporte = costoFromValidacionArchivoTransporte(validacionArchivoTransporte);

		saveAll(listCostoTransporte);
		
		persistirMaestroLlavesTransporte();

		return null;
	}

	/**
	 * Metodo para realizar el guardado de una lista en la tabla de costo_transporte
	 */
	@Override
	public void saveAll(List<CostosTransporte> costoTransporte) {

		costosTransporteRepository.saveAll(costoTransporte);

	}

	private List<CostosTransporte> costoFromValidacionArchivoTransporte(ValidacionArchivoDTO validacionArchivoTransporte) {

		var timestampTransporte = new Timestamp(System.currentTimeMillis());
		
		List<String> listaDominioFechaTransporte = new ArrayList<>();
		listaDominioFechaTransporte.add(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);

		List<CostosTransporte> listTransporteCostos = new ArrayList<>();

		// Obtener Lineas Validadas
		List<ValidacionLineasDTO> lineasTransporteValidacion = validacionArchivoTransporte.getValidacionLineas();
		
		var transportadoraCod = transportadorasService.getcodigoTransportadora(validacionArchivoTransporte.getDescripcion());
		
		lineasTransporteValidacion.forEach(f -> {
			var costoTransporteObj = new CostosTransporte();
			var contenidoArchivoTransporteObj = f.getContenido();
			costoTransporteObj.setEntidadTransporte(contenidoArchivoTransporteObj.get(0));
			costoTransporteObj.setFacturaTransporte(contenidoArchivoTransporteObj.get(1));
			costoTransporteObj.setTipoRegistroTransporte(contenidoArchivoTransporteObj.get(2));
			costoTransporteObj.setFechaServicioTransporte(UtilsString.toDate(contenidoArchivoTransporteObj.get(3), listaDominioFechaTransporte));

			costoTransporteObj.setIdentificacionClienteTransporte(contenidoArchivoTransporteObj.get(4));
			costoTransporteObj.setRazonSocialTransporte(contenidoArchivoTransporteObj.get(5));
			costoTransporteObj.setCodigoPuntoCargoTransporte(contenidoArchivoTransporteObj.get(6));
			costoTransporteObj.setNombrePuntoCargoTransporte(contenidoArchivoTransporteObj.get(7));

			costoTransporteObj.setCodigoCiiuPuntoTransporte(UtilsString.toInteger(contenidoArchivoTransporteObj.get(8)));
			costoTransporteObj.setCiudadMunicipioPunto(contenidoArchivoTransporteObj.get(9));
			costoTransporteObj.setCodigoCiiuFondoTransporte(UtilsString.toInteger(contenidoArchivoTransporteObj.get(10)));
			costoTransporteObj.setCiudadFondoTransporte(contenidoArchivoTransporteObj.get(11));

			costoTransporteObj.setNombreTipoServicioTransporte(contenidoArchivoTransporteObj.get(12));
			costoTransporteObj.setTipoPedidoTransporte(contenidoArchivoTransporteObj.get(13));

			costoTransporteObj.setEscalaTransporte(contenidoArchivoTransporteObj.get(14));

			costoTransporteObj.setExclusivoMonedaTransporte(contenidoArchivoTransporteObj.get(15));
			costoTransporteObj.setMonedaDivisaTransporte(contenidoArchivoTransporteObj.get(16));

			costoTransporteObj.setTrmConversionTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(17), Constantes.DECIMAL_SEPARATOR));

			costoTransporteObj.setValorTransportadoBillete(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(18), Constantes.DECIMAL_SEPARATOR));
			costoTransporteObj.setValorTransportadoMoneda(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(19), Constantes.DECIMAL_SEPARATOR));
			costoTransporteObj.setValorTotalTransportado(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(20), Constantes.DECIMAL_SEPARATOR));

			costoTransporteObj.setNumeroFajosTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(21), Constantes.DECIMAL_SEPARATOR));
			costoTransporteObj.setNumeroBolsasMonedaTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(22), Constantes.DECIMAL_SEPARATOR));

			costoTransporteObj.setCostoFijoTransporte(UtilsString.toLong(contenidoArchivoTransporteObj.get(23)));

			costoTransporteObj.setCostoMilajeTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(24), Constantes.DECIMAL_SEPARATOR));
			costoTransporteObj.setCostoBolsaTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(25), Constantes.DECIMAL_SEPARATOR));

			costoTransporteObj.setCostoFletesTransporte(UtilsString.toLong(contenidoArchivoTransporteObj.get(26)));
			costoTransporteObj.setCostoEmisariosTransporte(UtilsString.toLong(contenidoArchivoTransporteObj.get(27)));
			costoTransporteObj.setOtros1(UtilsString.toLong(contenidoArchivoTransporteObj.get(28)));
			costoTransporteObj.setOtros2(UtilsString.toLong(contenidoArchivoTransporteObj.get(29)));
			costoTransporteObj.setOtros3(UtilsString.toLong(contenidoArchivoTransporteObj.get(30)));
			costoTransporteObj.setOtros4(UtilsString.toLong(contenidoArchivoTransporteObj.get(31)));
			costoTransporteObj.setOtros5(UtilsString.toLong(contenidoArchivoTransporteObj.get(32)));

			costoTransporteObj.setSubtotalTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(33), Constantes.DECIMAL_SEPARATOR));
			costoTransporteObj.setIvaTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(34), Constantes.DECIMAL_SEPARATOR));
			costoTransporteObj.setValorTotalTransporte(UtilsString.toDecimal(contenidoArchivoTransporteObj.get(35), Constantes.DECIMAL_SEPARATOR));

			costoTransporteObj.setObservacionesAthTransporte(contenidoArchivoTransporteObj.get(36));
			costoTransporteObj.setObservacionesTdvTransporte(contenidoArchivoTransporteObj.get(37));

			costoTransporteObj.setEstadoConciliacionTransporte(Constantes.ESTADO_PROCESO_PENDIENTE);
			costoTransporteObj.setEstadoTransporte(Constantes.REGISTRO_ACTIVO);

			costoTransporteObj.setIdArchivoCargadoTransporte(validacionArchivoTransporte.getIdArchivo());
			costoTransporteObj.setIdRegistroTransporte(Long.valueOf(f.getNumeroLinea()));

			costoTransporteObj.setUsuarioCreacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);

			costoTransporteObj.setFechaCreacionTransporte(timestampTransporte);

			costoTransporteObj.setUsuarioModificacionTransporte(null);

			costoTransporteObj.setFechaModificacionTransporte(null);
			
			costoTransporteObj.setCodigoTdvTransportadora(transportadoraCod);
			
			IPuntoInterno puntoInterno = puntosRepository.findPuntoInterno(
					contenidoArchivoTransporteObj.get(6), 
					contenidoArchivoTransporteObj.get(7), 
					transportadoraCod, 
					contenidoArchivoTransporteObj.get(0));
			

		    Integer puntoFondo = puntosRepository.findPuntoFondo(
		        contenidoArchivoTransporteObj.get(0),
		        Constantes.PUNTO_FONDO,                             
		        contenidoArchivoTransporteObj.get(10),
		        transportadoraCod,
		        validacionArchivoTransporte.getDescripcion(),
		        contenidoArchivoTransporteObj.get(11)
		    );
			
			if (puntoInterno != null) {
		        costoTransporteObj.setCodigoPuntoInternoTransporte(puntoInterno.getCodigoPunto());
		        costoTransporteObj.setTipoPuntoTransporte(puntoInterno.getTipoPunto());
		    }
			
			if (puntoFondo != null) {
	            costoTransporteObj.setCodigoPuntoFondoTransporte(puntoFondo);
				costoTransporteObj.setNombreFondoTransporte(contenidoArchivoTransporteObj.get(0) + "-"
						+ contenidoArchivoTransporteObj.get(11) + "-" + validacionArchivoTransporte.getDescripcion());	            
	        }
			
			costoTransporteObj.setEntradaSalidaTransporte(archivosLiquidacionService
					.getEntradaSalida(contenidoArchivoTransporteObj.get(12), Constantes.MAESTRO_ARCHIVO_TRANSPORTE));

			listTransporteCostos.add(costoTransporteObj);
		});

		return listTransporteCostos;
	}
	
	/**
	 * Metodo para realizar la busqueda  de aceptados en DB en la tabla de Costos_transporte
	 */	
	@Override
	public List<CostosTransporte> findAceptados(String entidadTransporte, Date fechaServicioTransporte, String codigoPuntoCargoTransp,
			String nombrePuntoCargoTransp, String ciudadFondoTransporte, String nombreTipoServicioTransporte) {
	
	
		return costosTransporteRepository.findByEstadoEntidadFechaServicio(entidadTransporte,
																			fechaServicioTransporte,
																			codigoPuntoCargoTransp,
																			nombrePuntoCargoTransp,
																			ciudadFondoTransporte,
																			nombreTipoServicioTransporte,
																			Dominios.ESTADO_VALIDACION_ACEPTADO);
	}

	@Override
	public List<CostosTransporte> conciliadas(String entidadTransporte, String identificacionTransporte) {
		return costosTransporteRepository.conciliadas(entidadTransporte, identificacionTransporte);
	}

	@Override
	public List<ConciliacionCostosTransporteDTO> conciliadasDto(String entidadTransporte, String identificacionTransporte) {

		return toListDTO(conciliadas(entidadTransporte, identificacionTransporte));

	}

	public ConciliacionCostosTransporteDTO toDTO(CostosTransporte entTransporteCorsto) {

		return ConciliacionCostosTransporteDTO.builder().consecutivo(entTransporteCorsto.getConsecutivoTransporte())
				.entidad(entTransporteCorsto.getEntidadTransporte())
				.factura(entTransporteCorsto.getFacturaTransporte())
				.tipoRegistro(entTransporteCorsto.getTipoRegistroTransporte())
				.fechaServicioTransporte(entTransporteCorsto.getFechaServicioTransporte())
				.identificacionCliente(entTransporteCorsto.getIdentificacionClienteTransporte())
				.razonSocial(entTransporteCorsto.getRazonSocialTransporte())
				.codigoPuntoCargo(entTransporteCorsto.getCodigoPuntoCargoTransporte())
				.nombrePuntoCargo(entTransporteCorsto.getNombrePuntoCargoTransporte())
				.codigoCiiuPunto(entTransporteCorsto.getCodigoCiiuPuntoTransporte())
				.ciudadMunicipioPunto(entTransporteCorsto.getCiudadMunicipioPunto())
				.codigoCiiuFondo(entTransporteCorsto.getCodigoCiiuFondoTransporte())
				.ciudadFondo(entTransporteCorsto.getCiudadFondoTransporte())
				.nombreTipoServicio(entTransporteCorsto.getNombreTipoServicioTransporte())
				.tipoPedido(entTransporteCorsto.getTipoPedidoTransporte())
				.escala(entTransporteCorsto.getEscalaTransporte())
				.exclusivoMoneda(entTransporteCorsto.getExclusivoMonedaTransporte())
				.monedaDivisa(entTransporteCorsto.getMonedaDivisaTransporte())
				.trmConversion(entTransporteCorsto.getTrmConversionTransporte())
				.valorTransportadoBillete(entTransporteCorsto.getValorTransportadoBillete())
				.valorTransportadoMoneda(entTransporteCorsto.getValorTransportadoMoneda())
				.valorTotalTransportado(entTransporteCorsto.getValorTotalTransportado())
				.numeroFajos(entTransporteCorsto.getNumeroFajosTransporte())
				.numeroBolsasMoneda(entTransporteCorsto.getNumeroBolsasMonedaTransporte())
				.costoFijo(entTransporteCorsto.getCostoFijoTransporte())
				.costoMilaje(entTransporteCorsto.getCostoMilajeTransporte())
				.costoBolsa(entTransporteCorsto.getCostoBolsaTransporte())
				.costoFletes(entTransporteCorsto.getCostoFletesTransporte())
				.costoEmisarios(entTransporteCorsto.getCostoEmisariosTransporte())
				.otros1(entTransporteCorsto.getOtros1())
				.otros2(entTransporteCorsto.getOtros2())
				.otros3(entTransporteCorsto.getOtros3())
				.otros4(entTransporteCorsto.getOtros4())
				.otros5(entTransporteCorsto.getOtros5())
				.subtotal(entTransporteCorsto.getSubtotalTransporte())
				.iva(entTransporteCorsto.getIvaTransporte())
				.valorTotal(entTransporteCorsto.getValorTotalTransporte())
				.estadoConciliacion(entTransporteCorsto.getEstadoConciliacionTransporte())
				.estado(entTransporteCorsto.getEstadoTransporte())
				.observacionesAth(entTransporteCorsto.getObservacionesAthTransporte())
				.observacionesTdv(entTransporteCorsto.getObservacionesTdvTransporte())
				.idArchivoCargado(entTransporteCorsto.getIdArchivoCargadoTransporte())
				.idRegistro(entTransporteCorsto.getIdRegistroTransporte())
				.usuarioCreacion(entTransporteCorsto.getUsuarioCreacionTransporte())
				.fechaCreacion(entTransporteCorsto.getFechaCreacionTransporte())
				.usuarioModificacion(entTransporteCorsto.getUsuarioModificacionTransporte())
				.fechaModificacion(entTransporteCorsto.getFechaModificacionTransporte()).build();
	}

	public List<ConciliacionCostosTransporteDTO> toListDTO(List<CostosTransporte> listaCostosTransporte) {

		return Objects.nonNull(listaCostosTransporte)
				? listaCostosTransporte.stream().map(this::toDTO).collect(Collectors.toList())
				: new ArrayList<>();

	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(ParametrosFiltroCostoTransporteDTO filtrosCostoTransporte) {
		
		var ldtFechaServicioTransporte = transformToLocalDateTime(filtrosCostoTransporte.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = transformToLocalDateTime(filtrosCostoTransporte.getFechaServicioTransporteFinal());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		Pageable pageable = filtrosCostoTransporte.getPage() != null
	            ? filtrosCostoTransporte.getPage() 
	            : PageRequest.of(0, 10); 
		
		var consulta = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtrosCostoTransporte.getEntidad(),
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtrosCostoTransporte.getIdentificacionCliente(),
				filtrosCostoTransporte.getRazonSocial(),
				filtrosCostoTransporte.getCodigoPuntoCargo(),
				filtrosCostoTransporte.getNombrePuntoCargo(),
				filtrosCostoTransporte.getCiudadFondo(),
				filtrosCostoTransporte.getNombreTipoServicio(),
				filtrosCostoTransporte.getMonedaDivisa(),
				filtrosCostoTransporte.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, 
				pageable);

		List<OperacionesLiquidacionTransporteDTO> operacionesLiquidacionTransporteDTO = new ArrayList<>();
		consulta.forEach(entity -> operacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));
		
		return liquidacionPage(consulta, pageable);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroCostoTransporteDTO filtrosTransporteCostos) {
		
		var ldtFechaTransporteServicio = transformToLocalDateTime(filtrosTransporteCostos.getFechaServicioTransporte());
		var ldtFechaFinalServicioTransporte = transformToLocalDateTime(filtrosTransporteCostos.getFechaServicioTransporteFinal());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		Pageable pageable = filtrosTransporteCostos.getPage() != null
	            ? filtrosTransporteCostos.getPage() 
	            : PageRequest.of(0, 10); 
		
		var consultaLiqTransporte = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtrosTransporteCostos.getEntidad(),
				ldtFechaTransporteServicio,
				ldtFechaFinalServicioTransporte,
				filtrosTransporteCostos.getIdentificacionCliente(),
				filtrosTransporteCostos.getRazonSocial(),
				filtrosTransporteCostos.getCodigoPuntoCargo(),
				filtrosTransporteCostos.getNombrePuntoCargo(),
				filtrosTransporteCostos.getCiudadFondo(),
				filtrosTransporteCostos.getNombreTipoServicio(),
				filtrosTransporteCostos.getMonedaDivisa(),
				filtrosTransporteCostos.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, 
				pageable);

		List<OperacionesLiquidacionTransporteDTO> operacionesTransporteLiquidacionDTO = new ArrayList<>();
		consultaLiqTransporte.forEach(entity -> operacionesTransporteLiquidacionDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));
		
		return liquidacionPage(consultaLiqTransporte, pageable);
	}
	
	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroCostoTransporteDTO filtrosCostoTransporte) {
		
		var convertedFechaTransporteServicio = transformToLocalDateTime(filtrosCostoTransporte.getFechaServicioTransporte());
		var convertedFechaFinalServicioTransporte = transformToLocalDateTime(filtrosCostoTransporte.getFechaServicioTransporteFinal());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		Pageable pageable = filtrosCostoTransporte.getPage() != null
	            ? filtrosCostoTransporte.getPage() 
	            : PageRequest.of(0, 10); 
		
		var consultaLiquidacionTransporte = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtrosCostoTransporte.getEntidad(),
				convertedFechaTransporteServicio,
				convertedFechaFinalServicioTransporte,
				filtrosCostoTransporte.getIdentificacionCliente(),
				filtrosCostoTransporte.getRazonSocial(),
				filtrosCostoTransporte.getCodigoPuntoCargo(),
				filtrosCostoTransporte.getNombrePuntoCargo(),
				filtrosCostoTransporte.getCiudadFondo(),
				filtrosCostoTransporte.getNombreTipoServicio(),
				filtrosCostoTransporte.getMonedaDivisa(),
				filtrosCostoTransporte.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, 
				pageable);

		List<OperacionesLiquidacionTransporteDTO> listOperacionesLiquidacionTransporteDTO = new ArrayList<>();
		consultaLiquidacionTransporte.forEach(entity -> listOperacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));
		
		return liquidacionPage(consultaLiquidacionTransporte, pageable);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroCostoTransporteDTO filtrosParametrosTransporte) {
		
		var fechaServicioTransporteConverted = transformToLocalDateTime(filtrosParametrosTransporte.getFechaServicioTransporte());
		var fechaServicioTransporteFinalConverted = transformToLocalDateTime(filtrosParametrosTransporte.getFechaServicioTransporteFinal());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		Pageable pageable = filtrosParametrosTransporte.getPage() != null
	            ? filtrosParametrosTransporte.getPage() 
	            : PageRequest.of(0, 10); 
		
		var consultaOperacionesLiqTransporte = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtrosParametrosTransporte.getEntidad(),
				fechaServicioTransporteConverted,
				fechaServicioTransporteFinalConverted,
				filtrosParametrosTransporte.getIdentificacionCliente(),
				filtrosParametrosTransporte.getRazonSocial(),
				filtrosParametrosTransporte.getCodigoPuntoCargo(),
				filtrosParametrosTransporte.getNombrePuntoCargo(),
				filtrosParametrosTransporte.getCiudadFondo(),
				filtrosParametrosTransporte.getNombreTipoServicio(),
				filtrosParametrosTransporte.getMonedaDivisa(),
				filtrosParametrosTransporte.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, 
				pageable);

		List<OperacionesLiquidacionTransporteDTO> listOperacionesLiquidacionTransporte = new ArrayList<>();
		consultaOperacionesLiqTransporte.forEach(entity -> listOperacionesLiquidacionTransporte
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));
		
		return liquidacionPage(consultaOperacionesLiqTransporte, pageable);
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getEliminadasTransporte(ParametrosFiltroCostoTransporteDTO filtrosCostosTransporteDTO) {
		
		var ldtFechaServicioTransporteParsed = transformToLocalDateTime(filtrosCostosTransporteDTO.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinalParsed = transformToLocalDateTime(filtrosCostosTransporteDTO.getFechaServicioTransporteFinal());
		
		Pageable pageable = filtrosCostosTransporteDTO.getPage() != null
	            ? filtrosCostosTransporteDTO.getPage() 
	            : PageRequest.of(0, 10); 
		
		var consultaOperacionesLiq = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtrosCostosTransporteDTO.getEntidad(), 
				ldtFechaServicioTransporteParsed,
				ldtFechaServicioTransporteFinalParsed, 
				filtrosCostosTransporteDTO.getIdentificacionCliente(), 
				filtrosCostosTransporteDTO.getRazonSocial(), 
				filtrosCostosTransporteDTO.getCodigoPuntoCargo(),
				filtrosCostosTransporteDTO.getNombrePuntoCargo(), 
				filtrosCostosTransporteDTO.getCiudadFondo(), 
				filtrosCostosTransporteDTO.getNombreTipoServicio(), 
				filtrosCostosTransporteDTO.getMonedaDivisa(), 
				filtrosCostosTransporteDTO.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS_ELIMINADAS, 
				//Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS,
				pageable);

		System.out.println("Valor de consultaOperacionesLiq antes del forEach: " + consultaOperacionesLiq); // Agrega este log
		
		List<OperacionesLiquidacionTransporteDTO> operacionesLiqTransporteDTO = new ArrayList<>();
		consultaOperacionesLiq.forEach(entity -> operacionesLiqTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacionPage(consultaOperacionesLiq, pageable);
	}

	
	
	private Page<OperacionesLiquidacionTransporteDTO> liquidacionPage(
			Page<OperacionesLiquidacionTransporteEntity> liquidadasTranporte, Pageable page) {

		return new PageImpl<>(
				liquidadasTranporte.getContent().stream().map(e -> OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(e))
						.collect(Collectors.<OperacionesLiquidacionTransporteDTO>toList()),
				page, liquidadasTranporte.getTotalElements());
	}
	
	private LocalDateTime transformToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }

	/**
	 * Metodo encargado de desconciliar los registros de costos_transporte 
	 * 
	 * @param registrosConciliacionTransporte
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author hector.mercado
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registrosConciliacionTransporte) {
		
		List<RegistroOperacionConciliacionDTO> conciliadosOperacionTransporte = registrosConciliacionTransporte.getRegistroOperacion();
		var timestampTransporte = new Timestamp(System.currentTimeMillis());
		
		conciliadosOperacionTransporte.forEach(f->{
			var continuarFlag = false;
			Long idRegistro = f.getIdRegistro();
			
			List<IDetalleLiquidacionTransporte> detalles = obtenerDetalleLiquidacionTransporte(
					Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, idRegistro);			
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			List<CostosTransporte> costoTranporteList = obtenerCostoTransporteList(
					Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, idRegistro);
			
			if (costoTranporteList != null && !costoTranporteList.isEmpty()) {
				String estadoConciliacion = costoTranporteList.get(0).getEstadoConciliacionTransporte();
				Long idLiquidacionTransporte = obtenerIdPorTipo(detalles,
						Constantes.LISTA_CONCILIACION_IDLIQUIDACIONTDV);
				Integer tipoTransaccionTransporte = costoTranporteList.get(0).getTipoTransaccionTransporte();
				
				continuarFlag = desconciliarParametroLiquidacionCosto(estadoConciliacion, tipoTransaccionTransporte, idLiquidacionTransporte);
				
			    if (continuarFlag) {
					//actualizar estado de registro inicial
			    	 for (CostosTransporte costoTranporte : costoTranporteList) {
			    		 costoTranporte.setIdLiquidacionTransporte(idLiquidacionTransporte);
			    		 costoTranporte.setTipoTransaccionTransporte(0);
			    		 costoTranporte.setEstadoConciliacionTransporte(Dominios.ESTADO_VALIDACION_EN_CONCILIACION);
			    		 costoTranporte.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
			    		 costoTranporte.setFechaModificacionTransporte(timestampTransporte);
			    	 }
					costosTransporteRepository.saveAll(costoTranporteList);
			    	f.setOperacionEstado("DESCONCILIADO");
			    }
				
			}
			
		});
		
		return conciliadosOperacionTransporte;
	}
	

	/**
	 * Metodo para aceptar o rechazar los registros en costos_transporte
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registrosConciliacionListDTO) {
		
		List<RegistroOperacionConciliacionDTO> aceptadosRechazadosTransporte = registrosConciliacionListDTO.getRegistroOperacion();
		var timestampTransporte = new Timestamp(System.currentTimeMillis());
		
		aceptadosRechazadosTransporte.forEach(f->{
			var continuarTransporte = false;
			Long idView = f.getIdRegistro();
			
			String operacionEstadoAceptadosRechazados = f.getOperacionEstado();
			String observacionATHAceptadosRechazados = f.getObservacion();
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			List<CostosTransporte> costoTranporteList = obtenerCostoTransporteList(
					Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, idView);
			
			if (costoTranporteList != null && !costoTranporteList.isEmpty()) {
								
				if (operacionEstadoAceptadosRechazados.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR))
				{
					
					Long idLiquidacionParametros = aceptarParametroLiquidacionCosto(costoTranporteList.get(0), idView);
					if (idLiquidacionParametros.compareTo(0l)>0)
					{
						continuarTransporte = true;
						for (CostosTransporte costoTranporte : costoTranporteList) {
				            costoTranporte.setIdLiquidacionTransporte(idLiquidacionParametros);
				            costoTranporte.setTipoTransaccionTransporte(1);
				            costoTranporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_MANUAL);
				        }
					}
				}
				
				if (operacionEstadoAceptadosRechazados.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuarTransporte = true;
					for (CostosTransporte costoTranporte : costoTranporteList) {
				        costoTranporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_RECHAZADA);
				    }
				}
				
			    if (continuarTransporte) {
					//actualizar estado de registro
					for (CostosTransporte costoTranporte : costoTranporteList) {
						costoTranporte.setObservacionesAthTransporte(observacionATHAceptadosRechazados);
						costoTranporte.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
						costoTranporte.setFechaModificacionTransporte(timestampTransporte);						
					}
					
					costosTransporteRepository.saveAll(costoTranporteList);
					
					f.setOperacionEstado("APLICADO");
			    }
			}
		});
		return aceptadosRechazadosTransporte;
	}
	
	/**
	 * Metodo para eliminar o rechazar los registros en costos_transporte
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> liquidadasEliminarRechazar(RegistrosConciliacionListDTO conciliacionListDTO) {
		
		List<RegistroOperacionConciliacionDTO> eliminadosRechazados = conciliacionListDTO.getRegistroOperacion();
		
		eliminadosRechazados.forEach(f->{
			Long idRegTransporte = f.getIdRegistro();
			String operacionEstadoTransporte = f.getOperacionEstado();
			String observacionATHTransporte = f.getObservacion();
			
			List<IDetalleLiquidacionTransporte> detalles = obtenerDetalleLiquidacionTransporte(
					Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, idRegTransporte);
			
			List<Long> idLiquidacionList = UtilsParsing.parseStringToList(detalles.get(0).getIdsLiquidacionApp());
			
			for (Long idLiquidacion : idLiquidacionList) {
				
				f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);

				if (operacionEstadoTransporte.equalsIgnoreCase("ELIMINAR")) {
					eliminarParametroLiquidacionCosto(idLiquidacion, Constantes.MAESTRO_ARCHIVO_TRANSPORTE);
					f.setOperacionEstado("ELIMINADA");
				}

				if (operacionEstadoTransporte.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR)) {
					guardarCostoRechazado(idLiquidacion, observacionATHTransporte);
					f.setOperacionEstado("RECHAZADA");
				}
			}
			
		});
		
		return eliminadosRechazados;
	}
	
	private boolean desconciliarParametroLiquidacionCosto(String estadoTransporte, Integer tipoEstado,
			Long idLiquidacion) {

		// Validar si estado es conciliado manual
		// Validar si tipo es 1= eliminar el registro de la tabla
		// parametrosLiquidacionCostos
		var paso = validarEstadoTransporteManual(estadoTransporte, tipoEstado, idLiquidacion);

		// Validar si estado es conciliado manual
		// Validar si tipo es 2= actualizar valores antiguos de la tabla
		// parametrosLiquidacionCostos
		if (estadoTransporte.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipoEstado.equals(2)) {

			ObjectMapper objectMapper = new ObjectMapper();
			ParametrosLiquidacionCosto parametroLiquidacionCostoTransprote;
			ValoresLiquidadosFlatEntity valoresLiquidadosFlat;

			var valoresLiquidados = valoresLiquidadosFlatService.consultarPorIdLiquidacion(idLiquidacion);

			// Buscar valores antiguos
			List<EstadoConciliacionParametrosLiquidacion> oldListEstadoConciliacion = estadoConciliacionParametrosLiquidacionService
					.buscarLiquidacion(idLiquidacion, 2);
			try {
				if (Objects.nonNull(oldListEstadoConciliacion) && !oldListEstadoConciliacion.isEmpty()) {
					EstadoConciliacionParametrosLiquidacion oldEstadoConciliacion = oldListEstadoConciliacion.get(0);

					parametroLiquidacionCostoTransprote = objectMapper.readValue(
							oldEstadoConciliacion.getDatosParametrosLiquidacionCostos(),
							ParametrosLiquidacionCosto.class);
					valoresLiquidadosFlat = objectMapper.readValue(oldEstadoConciliacion.getDatosValoresLiquidados(),
							ValoresLiquidadosFlatEntity.class);

					if (Objects.nonNull(parametroLiquidacionCostoTransprote)) {
						parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiquidacionCostoTransprote);
					}

					if (Objects.nonNull(valoresLiquidadosFlat)) {
						
						// Recupera los valores de procesamiento existentes y los reasigna para
						// preservar la integridad de conciliaciones previas.
						if (valoresLiquidados != null) {
							valoresLiquidadosFlat.setClasificacionFajadoFlat(valoresLiquidados.getClasificacionFajadoFlat());
							valoresLiquidadosFlat.setClasificacionNoFajadoFlat(valoresLiquidados.getClasificacionNoFajadoFlat());
							valoresLiquidadosFlat.setCostoPaqueteoFlat(valoresLiquidados.getCostoPaqueteoFlat());
							valoresLiquidadosFlat.setModenaResiduoFlat(valoresLiquidados.getModenaResiduoFlat());
							valoresLiquidadosFlat.setBilleteResiduoFlat(valoresLiquidados.getBilleteResiduoFlat());
						}

						valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosFlat);
					}

					paso = true;

				}

			} catch (JsonProcessingException e) {
				paso = false;
			}

		}

		return paso;

	}
	
	private boolean validarEstadoTransporteManual(String estadoTransporte, Integer tipoEstado, Long idLiquidacion)
	{
		//Validar si estado es conciliado manual
		//Validar si tipo es 1= eliminar el registro de la tabla parametrosLiquidacionCostos
		if (estadoTransporte.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipoEstado.equals(1))
		{
			var parametroLiquidacion = 
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosByIdFlat(idLiquidacion);
					
				if (Objects.nonNull(parametroLiquidacion))
				{
					parametrosLiquidacionCostosService.f2eliminarParametrosLiquidacionCostos(parametroLiquidacion);
				}
				return true;
		}
		return false;
	}
	
	
	private Long aceptarParametroLiquidacionCosto(CostosTransporte costoTransport, Long consecutivoVista)
	{
	
		Long idLiquidacion = 0l;
		int consecutivoTransporte = Math.toIntExact(consecutivoVista);
		OperacionesLiquidacionTransporteEntity vwCostoRegistroLiqTransporte = operacionesLiquidacionTransporte
				.consultarConsecutivoRegistro(consecutivoTransporte).orElse(null);
		
		if (vwCostoRegistroLiqTransporte != null) {
			costoTransport.setEstadoConciliacionTransporte(vwCostoRegistroLiqTransporte.getEstado());
        }
	
		if (costoTransport.getEstadoConciliacionTransporte().equals(Dominios.ESTADO_VALIDACION_EN_CONCILIACION))
		{
			var parametroLiqCosto = new ParametrosLiquidacionCosto();
			var valoresLiquidadosFlatEntity = new ValoresLiquidadosFlatEntity();
		
			var bancoAbr = bancoService.findBancoByAbreviatura(costoTransport.getEntidadTransporte());
			
			parametroLiqCosto.setCodigoBanco(bancoAbr.getCodigoPunto());
			parametroLiqCosto.setFechaEjecucion(costoTransport.getFechaServicioTransporte());
			parametroLiqCosto.setCodigoTdv(costoTransport.getCodigoTdvTransportadora());
			parametroLiqCosto.setNombreCliente(costoTransport.getRazonSocialTransporte());
			parametroLiqCosto.setCodigoPropioTdv(
					costoTransport.getCodigoPuntoCargoTransporte() + costoTransport.getNombrePuntoCargoTransporte());
			parametroLiqCosto.setTipoOperacion(costoTransport.getNombreTipoServicioTransporte());
			parametroLiqCosto.setTipoServicio(costoTransport.getTipoPedidoTransporte());
			parametroLiqCosto.setEscala(costoTransport.getEscalaTransporte());			
			parametroLiqCosto.setValorBilletes(vwCostoRegistroLiqTransporte.getValorTransportadoBilletesTdv().doubleValue());
			parametroLiqCosto.setValorMonedas(vwCostoRegistroLiqTransporte.getValorTransportadoMonedasTdv().doubleValue());
			parametroLiqCosto.setValorTotal(vwCostoRegistroLiqTransporte.getValorTotalTransportadoTdv().doubleValue());
			parametroLiqCosto.setTotalBolsas(vwCostoRegistroLiqTransporte.getNumeroBolsasTdv().doubleValue());
			parametroLiqCosto.setTotalFajos(vwCostoRegistroLiqTransporte.getNumeroFajosTdv() != null
					? vwCostoRegistroLiqTransporte.getNumeroFajosTdv().doubleValue()
					: null);
			parametroLiqCosto.setEntradaSalida(costoTransport.getEntradaSalidaTransporte());
			
			if ("ENTRADA".equalsIgnoreCase(costoTransport.getEntradaSalidaTransporte())) {
			    parametroLiqCosto.setPuntoOrigen(costoTransport.getCodigoPuntoInternoTransporte());
			    parametroLiqCosto.setPuntoDestino(costoTransport.getCodigoPuntoFondoTransporte());
			} else if ("SALIDA".equalsIgnoreCase(costoTransport.getEntradaSalidaTransporte())) {
			    parametroLiqCosto.setPuntoDestino(costoTransport.getCodigoPuntoInternoTransporte());
			    parametroLiqCosto.setPuntoOrigen(costoTransport.getCodigoPuntoFondoTransporte());
			}
			
			valoresLiquidadosFlatEntity.setCostoFijoParadaFlat(vwCostoRegistroLiqTransporte.getCostoFijoTdv().doubleValue());
			valoresLiquidadosFlatEntity.setMilajePorRuteoFlat(vwCostoRegistroLiqTransporte.getCostoMilajeTdv().doubleValue());
			valoresLiquidadosFlatEntity.setCostoMonedaFlat(vwCostoRegistroLiqTransporte.getCostoBolsaTdv().doubleValue());
			valoresLiquidadosFlatEntity.setCostoCharterFlat(vwCostoRegistroLiqTransporte.getCostoFleteTdv().doubleValue());
			valoresLiquidadosFlatEntity.setCostoEmisarioFlat(vwCostoRegistroLiqTransporte.getCostoEmisarioTdv().doubleValue());
						
			parametroLiqCosto = parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiqCosto);
			idLiquidacion= parametroLiqCosto.getIdLiquidacion();
			
			valoresLiquidadosFlatEntity.setIdLiquidacionFlat(idLiquidacion);
			valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosFlatEntity);
		}
		
		return idLiquidacion;
		
	}
	
	public Long eliminarParametroLiquidacionCosto(Long id, String agrupador)
	{
		Long eliminadoId = 0l;
		var estadoConciliacionLiqTransporte = new EstadoConciliacionParametrosLiquidacion();
		var objectMapper = new ObjectMapper();
		//Obtener el registro de la base de datos
		var parametroLiquidacion = 
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosByIdFlat(id);
		var valorLiq =
				valoresLiquidadosFlatService.consultarPorIdLiquidacion(id);
		var detalleLiq =
				detallesLiquidacionCostoService.consultarPorIdLiquidacionFlat(id);
		
		if (Objects.nonNull(parametroLiquidacion)) {
			eliminadoId = id;
			var fotoParametrosLiqCostoTransportte = "";
			var foto2VarlorLiqFlagTransporte = "";
			var foto3DetalleLiqTransporte = "";
			//sacar foto del registro
			try {
				fotoParametrosLiqCostoTransportte = objectMapper.writeValueAsString(parametroLiquidacion);
				foto2VarlorLiqFlagTransporte = objectMapper.writeValueAsString(valorLiq);
				foto3DetalleLiqTransporte = objectMapper.writeValueAsString(detalleLiq);
				List<EstadoConciliacionParametrosLiquidacion> oldListEstadoConciliacionTransporte = estadoConciliacionParametrosLiquidacionService.buscarLiquidacion(id, 1);
				if (Objects.nonNull(oldListEstadoConciliacionTransporte) && !oldListEstadoConciliacionTransporte.isEmpty())
				{
					estadoConciliacionLiqTransporte = oldListEstadoConciliacionTransporte.get(0);
				}
				estadoConciliacionLiqTransporte.setIdLiquidacion(id);
				estadoConciliacionLiqTransporte.setDatosParametrosLiquidacionCostos(fotoParametrosLiqCostoTransportte);
				
				if ("LIQTP".equalsIgnoreCase(agrupador)) {
					estadoConciliacionLiqTransporte.setDatosValoresLiquidados(foto2VarlorLiqFlagTransporte);
				}else {
					
					estadoConciliacionLiqTransporte.setDatosValoresLiquidados("");
					estadoConciliacionLiqTransporte.setDatosValoresLiquidadosProc(foto2VarlorLiqFlagTransporte);
				}
				
				
				estadoConciliacionLiqTransporte.setDatosDetallesLiquidados(foto3DetalleLiqTransporte);
				estadoConciliacionLiqTransporte.setEstado(1);
											
				//se elimina el registro de parametro liquidacion
				parametrosLiquidacionCostosService.f2eliminarParametrosLiquidacionCostos(parametroLiquidacion, estadoConciliacionLiqTransporte);
				
			} catch (JsonProcessingException e) {
				//Excepcion no manejada
			}

		}
		
		return eliminadoId;
		
	}
	
	private Long guardarCostoRechazado(Long idParametrosLiqCostos, String observacionTransporte) {
		String idParametriLiq = idParametrosLiqCostos.toString();
		BigInteger idLlavesMaestroApp = operacionesLiquidacionTransporte
				.obtenerIdLlavesMaestroAppPorIdsLiquidacionApp(idParametriLiq);

		if (idLlavesMaestroApp != null) {
			maestroLlavesCostosRepository.actualizarEstadoAndObservacionesPorLlaves(Collections.singletonList(idLlavesMaestroApp),
					Constantes.ESTADO_CONCILIACION_RECHAZADA, observacionTransporte);
		}
		return idParametrosLiqCostos;
	}

	
	@Override
	public List<RegistroAceptarRechazarDTO> identificadasConDiferenciaAceptarRechazar(RegistrosAceptarRechazarListDTO aceptarRechazarListDTODiferencia) {
		
		List<RegistroAceptarRechazarDTO> aceptadosRechazados = aceptarRechazarListDTODiferencia.getRegistroOperacion();
		var timestampCurrent = new Timestamp(System.currentTimeMillis());
		
		aceptadosRechazados.forEach(f->{
			
			var continuarBandera = false;
			Long id = f.getIdRegistro();

			List<IDetalleLiquidacionTransporte> detalles = obtenerDetalleLiquidacionTransporte(
					Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, id);

			String operacionEstadoStr = f.getOperacionEstado();
			String observacionATHTransporteDiferencia = f.getObservacion();
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			List<CostosTransporte> costoTranporteList = obtenerCostoTransporteList(
					Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, id);
			
			CostosTransporte costoTrsprte = calcularDiferenciasCostos(detalles, costoTranporteList.get(0));
			
			if (costoTranporteList != null && !costoTranporteList.isEmpty()) {
								
				if (operacionEstadoStr.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR))
				{
					// Se cargan las diferencias solo a un registro de la liquidación para la conciliación.
					// Sin embargo, las actualizaciones del resultado se aplican a todos los registros unificados de costos.
					Long idLiquidacionTransporteDif = obtenerIdPorTipo(detalles,
							Constantes.LISTA_CONCILIACION_IDLIQUIDACIONAPP);

					aceptarCostoTransporteIdentificadasConDiferencia(costoTrsprte, idLiquidacionTransporteDif);

					if (idLiquidacionTransporteDif.compareTo(0l)>0)
					{	
						continuarBandera = true;
						for (CostosTransporte costoTranporte : costoTranporteList) {
							costoTranporte.setIdLiquidacionTransporte(idLiquidacionTransporteDif);
							costoTranporte.setTipoTransaccionTransporte(2);
							costoTranporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_MANUAL);
						}
					}
				}
				
				if (operacionEstadoStr.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuarBandera = true;
					for (CostosTransporte costoTranporte : costoTranporteList) {
						costoTranporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_RECHAZADA);
					}
				}
				
			    if (continuarBandera) {
			    	
					for (CostosTransporte costoTranporte : costoTranporteList) {
						costoTranporte.setObservacionesAthTransporte(observacionATHTransporteDiferencia);
						costoTranporte.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
						costoTranporte.setFechaModificacionTransporte(timestampCurrent);
					}
					costosTransporteRepository.saveAll(costoTranporteList);
					f.setOperacionEstado("APLICADO");
			    }
			}
		});
		return aceptadosRechazados;
	}
	
	private void aceptarCostoTransporteIdentificadasConDiferencia (CostosTransporte costoTransporteDif, Long idLiquidacionTransp)
	{	
		var liquidacionCostosTransporteParametro =
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(idLiquidacionTransp).orElse(null);

		var valoresLiquidadosParametro =
				valoresLiquidadosFlatService.getParametrosValoresLiquidadosByIdLiquidacion(idLiquidacionTransp);

		if (Objects.nonNull(liquidacionCostosTransporteParametro) && Objects.nonNull(valoresLiquidadosParametro))
			{	
				// Guarda una fotografia de como estaba nates los valores para poder recuperaros despues.
				salvarValoresLiquidadosLiquidacionCostos(liquidacionCostosTransporteParametro, valoresLiquidadosParametro);
			
				liquidacionCostosTransporteParametro.setTipoOperacion(costoTransporteDif.getNombreTipoServicioTransporte());
				
				liquidacionCostosTransporteParametro.setTotalFajos(
						aplicarAjuste(costoTransporteDif.getNumeroFajosTransporte().doubleValue(),
								liquidacionCostosTransporteParametro.getTotalFajos(),Double.class));
				
				liquidacionCostosTransporteParametro.setValorTotal(
						aplicarAjuste(costoTransporteDif.getValorTotalTransportado().doubleValue(),
								liquidacionCostosTransporteParametro.getValorTotal(),Double.class));
				
				liquidacionCostosTransporteParametro.setTotalBolsas(
						aplicarAjuste(costoTransporteDif.getNumeroBolsasMonedaTransporte().doubleValue(),
								liquidacionCostosTransporteParametro.getTotalBolsas(),Double.class));
				
								
//				liquidacionCostosTransporteParametro.setEscala(costoTransporteDif.getEscalaTransporte());
//				liquidacionCostosTransporteParametro.setValorBilletes(costoTransporteDif.getValorTransportadoBillete().doubleValue());
//				liquidacionCostosTransporteParametro.setValorMonedas(costoTransporteDif.getValorTransportadoMoneda().doubleValue());
				
				
				valoresLiquidadosParametro.setCostoFijoParadaFlat(
						aplicarAjuste(costoTransporteDif.getCostoFijoTransporte().doubleValue(),
								valoresLiquidadosParametro.getCostoFijoParadaFlat(),Double.class));
				
				double milajeVerificacionFlat = valoresLiquidadosParametro.getMilajeVerificacionFlat() != null 
					    ? valoresLiquidadosParametro.getMilajeVerificacionFlat() 
					    : 0;
				
				valoresLiquidadosParametro.setMilajePorRuteoFlat(
						aplicarAjuste(UtilsString.calcularDiferenciaAbsoluta(
							costoTransporteDif.getCostoMilajeTransporte().doubleValue(),
							milajeVerificacionFlat),valoresLiquidadosParametro.getMilajePorRuteoFlat(),Double.class));
				
				valoresLiquidadosParametro.setCostoMonedaFlat(aplicarAjuste(costoTransporteDif.getCostoBolsaTransporte().doubleValue(), 
						valoresLiquidadosParametro.getCostoMonedaFlat(),Double.class));
				
				double tasaAeroportuariaFlat = valoresLiquidadosParametro.getTasaAeroportuariaFlat() != null 
					    ? valoresLiquidadosParametro.getTasaAeroportuariaFlat() 
					    : 0;
				
				valoresLiquidadosParametro.setCostoCharterFlat(
						aplicarAjuste(UtilsString.calcularDiferenciaAbsoluta(
							costoTransporteDif.getCostoFletesTransporte().doubleValue(),
							tasaAeroportuariaFlat),valoresLiquidadosParametro.getCostoCharterFlat(),Double.class));
				
				valoresLiquidadosParametro.setCostoEmisarioFlat(
						aplicarAjuste(costoTransporteDif.getCostoEmisariosTransporte().doubleValue(), 
								valoresLiquidadosParametro.getCostoEmisarioFlat(),Double.class));
				
				//costoTransporteDif.getSubtotalTransporte(); //Revisar donde conseguir este parámetro
							
				parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(liquidacionCostosTransporteParametro);

				valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosParametro);
			}
	}

	private void salvarValoresLiquidadosLiquidacionCostos(
		ParametrosLiquidacionCosto liquidacionCostoParametro, ValoresLiquidadosFlatEntity valoresLiquidadosFlagEntity){
			
			try {
				var objectMapper = new ObjectMapper();
				var imgParametroLiqCostos = objectMapper.writeValueAsString(liquidacionCostoParametro);
				var imgparametroValLiquidados = objectMapper.writeValueAsString(valoresLiquidadosFlagEntity);

				var estadoConciliacionParametrosLiquidacion = new EstadoConciliacionParametrosLiquidacion();
				
				// Consulta si existe una entidad previamente guardada
				var existeEstadoLiquidacion = estadoConciliacionParametrosLiquidacionService
				                    .buscarLiquidacion(liquidacionCostoParametro.getIdLiquidacion(), 2);
				
				estadoConciliacionParametrosLiquidacion.setIdLiquidacion(liquidacionCostoParametro.getIdLiquidacion());
				estadoConciliacionParametrosLiquidacion.setDatosParametrosLiquidacionCostos(imgParametroLiqCostos);
				estadoConciliacionParametrosLiquidacion.setDatosValoresLiquidados(imgparametroValLiquidados);
				estadoConciliacionParametrosLiquidacion.setEstado(2);
				
				if (existeEstadoLiquidacion != null && !existeEstadoLiquidacion.isEmpty()) {
					var estadoExistente = existeEstadoLiquidacion.get(0);
					estadoConciliacionParametrosLiquidacion
							.setDatosValoresLiquidadosProc(estadoExistente.getDatosValoresLiquidadosProc());
					estadoConciliacionParametrosLiquidacion
							.setDatosOtrosCostosFondo(estadoExistente.getDatosOtrosCostosFondo());
				} else {
					estadoConciliacionParametrosLiquidacion.setDatosValoresLiquidadosProc(null);
					estadoConciliacionParametrosLiquidacion.setDatosOtrosCostosFondo(null);
				}

				estadoConciliacionParametrosLiquidacionService.save(estadoConciliacionParametrosLiquidacion);

			} catch (Exception e) {
				// No se maneja la excepcion para este metodo
			}
	}

	/**
	 * Metodo encargado de reintegrar los registros eliminados de costos_transporte 
	 * 
	 * @param RegistrosConciliacionListDTO
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author jose.pabon
	 */	
	@Override
	@Transactional
	public List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasTransporte(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> registrosReintegrados = registros.getRegistroOperacion();
		
		registrosReintegrados.forEach(f->{
			boolean continuar = false;
			Long id = f.getIdRegistro();
			String operacion = f.getOperacionEstado();
			
			f.setOperacionEstado("NO PUDO REALIZAR LA OPERACION");
			
			if (operacion.equalsIgnoreCase("REINTEGRAR"))
			{
				List<IDetalleLiquidacionTransporte> detalles = obtenerDetalleLiquidacionTransporte(
						Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS_ELIMINADAS, id);
				
				List<Long> idLiquidacionList = UtilsParsing.parseStringToList(detalles.get(0).getIdsLiquidacionApp());
				
				for (Long idLiq : idLiquidacionList) {
	                continuar = reintegrarRegistrosLiquidados(idLiq, continuar, Constantes.MAESTRO_ARCHIVO_TRANSPORTE);
	                if (!continuar) break;
	            }
			}
			if (continuar) {
				f.setOperacionEstado("REINTEGRADA");
			}
		});
		return registrosReintegrados;
	}

	public boolean reintegrarRegistrosLiquidados(Long id, boolean continuar, String agrupador)
	{
		var objectMapper = new ObjectMapper();
		ParametrosLiquidacionCostoFlat parametroLiquidacion;
		ValoresLiquidadosFlatEntity valoresLiquidados;
		List<DetallesLiquidacionCostoFlatEntity> detallesLiquidacionCosto;
		EstadoConciliacionParametrosLiquidacion estadoConciliacionParametrosLiquidacion = new EstadoConciliacionParametrosLiquidacion();

		
		var registrosEstadoConciliacion = estadoConciliacionParametrosLiquidacionService.buscarLiquidacion(id, 1);
		if (!Objects.nonNull(registrosEstadoConciliacion))
			{return false;}

		try {
			estadoConciliacionParametrosLiquidacion = registrosEstadoConciliacion.get(0);
			parametroLiquidacion = objectMapper.readValue(estadoConciliacionParametrosLiquidacion.getDatosParametrosLiquidacionCostos(), ParametrosLiquidacionCostoFlat.class);
			
			if (Constantes.MAESTRO_ARCHIVO_TRANSPORTE.equalsIgnoreCase(agrupador)) {
			    valoresLiquidados = objectMapper.readValue(
			        estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidados(),
			        ValoresLiquidadosFlatEntity.class
			    );
			} else {
			    valoresLiquidados = objectMapper.readValue(
			        estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidadosProc(),
			        ValoresLiquidadosFlatEntity.class
			    );
			}
			
			TypeReference<List<DetallesLiquidacionCostoFlatEntity>> typeReference = new TypeReference<List<DetallesLiquidacionCostoFlatEntity>>() {};
			detallesLiquidacionCosto = objectMapper.readValue(estadoConciliacionParametrosLiquidacion.getDatosDetallesLiquidados(),typeReference);

			if (Objects.nonNull(parametroLiquidacion) || Objects.nonNull(valoresLiquidados)
					|| Objects.nonNull(detallesLiquidacionCosto)) {
				
				if (Objects.nonNull(parametroLiquidacion)) {
					parametrosLiquidacionCostosService
							.f2actualizarParametrosLiquidacionCostosFlat(parametroLiquidacion);
				}

				if (Objects.nonNull(valoresLiquidados)) {
					valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidados);
				}

				if (Objects.nonNull(detallesLiquidacionCosto)) {
					detallesLiquidacionCostoService
							.f2actualizarListaDetallesValoresLiquidados(detallesLiquidacionCosto);
				}

				continuar = true;
			}
		} catch (JsonProcessingException e) {
			continuar = false;
		}
		if(continuar){
			procesarEstadoConciliacion(agrupador,estadoConciliacionParametrosLiquidacion);
		}
		return continuar;
	}
	
	public void procesarEstadoConciliacion(String agrupador, EstadoConciliacionParametrosLiquidacion estadoConciliacionParametrosLiquidacion) {
	    if (agrupador == null || estadoConciliacionParametrosLiquidacion == null) {
	        return;
	    }

	    if (Constantes.MAESTRO_ARCHIVO_TRANSPORTE.equals(agrupador)) {
	    	//Si valores de procesamiento estan vacions entonces podemos borrar el registro completo
	        boolean valoresLiqProcVacio = estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidadosProc() == null
	                || estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidadosProc().isEmpty();
	        boolean otrosCostosVacio = estadoConciliacionParametrosLiquidacion.getDatosOtrosCostosFondo() == null
	                || estadoConciliacionParametrosLiquidacion.getDatosOtrosCostosFondo().isEmpty();

	        if (valoresLiqProcVacio && otrosCostosVacio) {
	            estadoConciliacionParametrosLiquidacionService.delete(estadoConciliacionParametrosLiquidacion);
	        } else {
	            estadoConciliacionParametrosLiquidacion.setDatosParametrosLiquidacionCostos("");
	            estadoConciliacionParametrosLiquidacion.setDatosValoresLiquidados("");
	            estadoConciliacionParametrosLiquidacionService.save(estadoConciliacionParametrosLiquidacion);
	        }

	    } else if (Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO.equals(agrupador)) {
	    	//Si valores de transporte estan vacios entonces podemos borrar el registro completo
	        boolean valoresLiqTransVacio = estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidados() == null
	                || estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidados().isEmpty();

	        if (valoresLiqTransVacio) {
	            estadoConciliacionParametrosLiquidacionService.delete(estadoConciliacionParametrosLiquidacion);
	        } else {
	            estadoConciliacionParametrosLiquidacion.setDatosValoresLiquidadosProc(null);
	            estadoConciliacionParametrosLiquidacion.setDatosOtrosCostosFondo(null);
	            estadoConciliacionParametrosLiquidacionService.save(estadoConciliacionParametrosLiquidacion);
	        }
	    }
	}
	
	public List<CostosTransporte> getByIdArchivoCargado(Long idArchivo)
	{
		return costosTransporteRepository.findByIdArchivoCargado(idArchivo);
	}
	
	
	public void aceptarConciliacionRegistro(Long idArchivoCargado)
	{
		costosTransporteRepository.actualizarEstadoByIdArchivoCargado(idArchivoCargado, Constantes.ESTADO_CONCILIACION_ACEPTADO);
	}
	
	@Transactional
	@Override
	public void persistirMaestroLlavesTransporte () {
		maestroLlavesCostosRepository.insertarMaestroLlaves();
		maestroLlavesCostosRepository.insertarArchivosLlaves();
	}

	@Override
	public List<IDetalleLiquidacionTransporte> obtenerDetalleLiquidacionTransporte(String modulo, Long idLlave) {
		
		List<IDetalleLiquidacionTransporte> resultados = operacionesLiquidacionTransporte
				.obtenerDetallesPorModulo(modulo, idLlave);
		return resultados;
	}
	
	@Override
	public List<IDetalleLiquidacionTransporte> obtenerDetalleTransportePorIdArchivo(Integer idArchivo) {

		List<IDetalleLiquidacionTransporte> resultados = operacionesLiquidacionTransporte
				.obtenerDetallesPorIdArchivo(idArchivo);
		return resultados;
	}
	
	@Override
	public List<IDetalleLiquidacionTransporte> obtenerEstadoTransportePorLlave(BigInteger idLlave) {

		List<IDetalleLiquidacionTransporte> resultados = operacionesLiquidacionTransporte
				.countConciliadasTransporteByLlave(idLlave);
		return resultados;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Number> T aplicarAjuste(Number saldoTdv, T valorApp, Class<T> tipo) {
	    if (saldoTdv == null) saldoTdv = 0d;

	    if (valorApp == null) {
	        if (tipo == Integer.class) {
	            valorApp = (T) Integer.valueOf(0);
	        } else if (tipo == Long.class) {
	            valorApp = (T) Long.valueOf(0L);
	        } else if (tipo == Double.class) {
	            valorApp = (T) Double.valueOf(0d);
	        } else if (tipo == Float.class) {
	            valorApp = (T) Float.valueOf(0f);
	        } else if (tipo == BigDecimal.class) {
	            valorApp = (T) BigDecimal.ZERO;
	        } else if (tipo == BigInteger.class) {
	            valorApp = (T) BigInteger.ZERO;
	        } else {
	            throw new UnsupportedOperationException("Tipo no soportado: " + tipo);
	        }
	    }

	    double resultado = valorApp.doubleValue() + saldoTdv.doubleValue();

	    if (valorApp instanceof Integer) {
	        return (T) Integer.valueOf((int) resultado);
	    } else if (valorApp instanceof Long) {
	        return (T) Long.valueOf((long) resultado);
	    } else if (valorApp instanceof Double) {
	        return (T) Double.valueOf(resultado);
	    } else if (valorApp instanceof Float) {
	        return (T) Float.valueOf((float) resultado);
	    } else if (valorApp instanceof BigDecimal) {
	        return (T) BigDecimal.valueOf(resultado);
	    } else if (valorApp instanceof BigInteger) {
	        return (T) BigInteger.valueOf((long) resultado);
	    } else {
	        throw new UnsupportedOperationException("Tipo no soportado: " + valorApp.getClass());
	    }
	}


	@Override
	public CostosTransporte calcularDiferenciasCostos(List<IDetalleLiquidacionTransporte> detalles,
	        CostosTransporte costoTransporte) {
	    
	    CostosTransporte resultado = new CostosTransporte();
		BeanUtils.copyProperties(costoTransporte, resultado);
		
	    BigDecimal numeroFajos = BigDecimal.ZERO;
	    BigDecimal valorTotalTransportado = BigDecimal.ZERO;
	    BigDecimal numeroBolsas = BigDecimal.ZERO;
	    Long costoFijo = 0L;
	    BigDecimal costoMilaje = BigDecimal.ZERO;
	    BigDecimal costoBolsa = BigDecimal.ZERO;
	    Long costoFlete = 0L;
	    Long costoEmisario = 0L;

	    for (IDetalleLiquidacionTransporte d : detalles) {
	        if (d.getNumeroFajos() != null && d.getNumeroFajosTdv() != null) {
	            numeroFajos = numeroFajos.add(d.getNumeroFajosTdv().subtract(d.getNumeroFajos()));
	        }

	        if (d.getValorTotalTransportado() != null && d.getValorTotalTransportadoTdv() != null) {
	            valorTotalTransportado = valorTotalTransportado.add(
	                d.getValorTotalTransportadoTdv().subtract(d.getValorTotalTransportado())
	            );
	        }
	        
	        if (d.getNumeroBolsas() != null && d.getNumeroBolsasTdv() != null) {
	            numeroBolsas = numeroBolsas.add(d.getNumeroBolsasTdv().subtract(d.getNumeroBolsas()));
	        }

	        if (d.getCostoFijo() != null && d.getCostoFijoTdv() != null) {
	            costoFijo += d.getCostoFijoTdv() - d.getCostoFijo().intValue();
	        }

	        if (d.getCostoMilaje() != null && d.getCostoMilajeTdv() != null) {
	            costoMilaje = costoMilaje.add(d.getCostoMilajeTdv().subtract(d.getCostoMilaje()));
	        }

	        if (d.getCostoBolsa() != null && d.getCostoBolsaTdv() != null) {
	            costoBolsa = costoBolsa.add(d.getCostoBolsaTdv().subtract(d.getCostoBolsa()));
	        }

	        if (d.getCostoFlete() != null && d.getCostoFleteTdv() != null) {
	            costoFlete += d.getCostoFleteTdv() - d.getCostoFlete().longValue();
	        }

	        if (d.getCostoEmisario() != null && d.getCostoEmisarioTdv() != null) {
	            costoEmisario += d.getCostoEmisarioTdv() - d.getCostoEmisario().intValue();
	        }
	    }

	    resultado.setNumeroFajosTransporte(numeroFajos);
	    resultado.setValorTotalTransportado(valorTotalTransportado);
	    resultado.setNumeroBolsasMonedaTransporte(numeroBolsas);
	    resultado.setCostoFijoTransporte(costoFijo);
	    resultado.setCostoMilajeTransporte(costoMilaje);
	    resultado.setCostoBolsaTransporte(costoBolsa);
	    resultado.setCostoFletesTransporte(costoFlete);
	    resultado.setCostoEmisariosTransporte(costoEmisario);

	    return resultado;
	}
	
	@Override
	public <T> Long obtenerIdPorTipo(List<T> detalles, String tipo) {
	    if (detalles == null || detalles.isEmpty()) {
	        return null;
	    }

	    for (T d : detalles) {
	        String valor = switch (tipo) {
	            case Constantes.LISTA_CONCILIACION_CONSECUTIVO ->
	                (d instanceof IDetalleLiquidacionTransporte trans) ? trans.getConsecutivoRegistro()
	                : ((IDetalleLiquidacionProcesamiento) d).getConsecutivoRegistro();

	            case Constantes.LISTA_CONCILIACION_IDLIQUIDACIONAPP ->
	                (d instanceof IDetalleLiquidacionTransporte trans) ? trans.getIdsLiquidacionApp()
	                : ((IDetalleLiquidacionProcesamiento) d).getIdsLiquidacionApp();

	            case Constantes.LISTA_CONCILIACION_IDLIQUIDACIONTDV ->
	                (d instanceof IDetalleLiquidacionTransporte trans) ? trans.getIdsLiquidacionTdv()
	                : ((IDetalleLiquidacionProcesamiento) d).getIdsLiquidacionTdv();

	            default -> throw new IllegalArgumentException("Tipo no soportado: " + tipo);
	        };

	        List<Long> lista = UtilsParsing.parseStringToList(valor);
	        if (lista != null && !lista.isEmpty()) {
	            return lista.get(0);
	        }
	    }

	    return null;
	}

	@Override
	public List<CostosTransporte> obtenerCostoTransporteList(String operacion, Long idRegistro) {
	   
		List<IDetalleLiquidacionTransporte> detalles = obtenerDetalleLiquidacionTransporte(operacion, idRegistro);
	    
		if (detalles.isEmpty()) {
	        return new ArrayList<>();
	    }

	    List<Long> listaConsecutivos = UtilsParsing.parseStringToList(detalles.get(0).getConsecutivoRegistro());
	    List<CostosTransporte> costoTranporteList = new ArrayList<>();

	    for (Long idReg : listaConsecutivos) {
	        CostosTransporte costoTranporte = costosTransporteRepository.findById(idReg).orElse(null);
	        if (costoTranporte != null) {
	            costoTranporteList.add(costoTranporte);
	        }
	    }

	    return costoTranporteList;
	}
}
