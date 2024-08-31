package com.ath.adminefectivo.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCostoFlat;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;
import com.ath.adminefectivo.repositories.ICostosTransporteRepository;
import com.ath.adminefectivo.repositories.IOperacionesLiquidacionTransporte;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICostosTransporteService;
import com.ath.adminefectivo.service.IDetallesLiquidacionCostoService;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import com.ath.adminefectivo.utils.UtilsParsing;
import com.ath.adminefectivo.utils.UtilsString;

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
	
	/**
	 * Metodo para realizar la persistencia en la tabla de costo_transporte
	 */
	@Override
	public Long persistir(ValidacionArchivoDTO validacionArchivoTransporte) {

		List<CostosTransporte> listCostoTransporte = costoFromValidacionArchivoTransporte(validacionArchivoTransporte);

		saveAll(listCostoTransporte);

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
				filtrosCostoTransporte.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesLiquidacionTransporteDTO = new ArrayList<>();
		consulta.forEach(entity -> operacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacionPage(consulta, filtrosCostoTransporte.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroCostoTransporteDTO filtrosTransporteCostos) {
		
		var ldtFechaTransporteServicio = transformToLocalDateTime(filtrosTransporteCostos.getFechaServicioTransporte());
		var ldtFechaFinalServicioTransporte = transformToLocalDateTime(filtrosTransporteCostos.getFechaServicioTransporteFinal());
		
		
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
				filtrosTransporteCostos.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesTransporteLiquidacionDTO = new ArrayList<>();
		consultaLiqTransporte.forEach(entity -> operacionesTransporteLiquidacionDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacionPage(consultaLiqTransporte, filtrosTransporteCostos.getPage());
	}
	
	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroCostoTransporteDTO filtrosCostoTransporte) {
		
		var convertedFechaTransporteServicio = transformToLocalDateTime(filtrosCostoTransporte.getFechaServicioTransporte());
		var convertedFechaFinalServicioTransporte = transformToLocalDateTime(filtrosCostoTransporte.getFechaServicioTransporteFinal());
		
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
				filtrosCostoTransporte.getPage());

		List<OperacionesLiquidacionTransporteDTO> listOperacionesLiquidacionTransporteDTO = new ArrayList<>();
		consultaLiquidacionTransporte.forEach(entity -> listOperacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacionPage(consultaLiquidacionTransporte, filtrosCostoTransporte.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroCostoTransporteDTO filtrosParametrosTransporte) {
		
		var fechaServicioTransporteConverted = transformToLocalDateTime(filtrosParametrosTransporte.getFechaServicioTransporte());
		var fechaServicioTransporteFinalConverted = transformToLocalDateTime(filtrosParametrosTransporte.getFechaServicioTransporteFinal());
		
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
				filtrosParametrosTransporte.getPage());

		List<OperacionesLiquidacionTransporteDTO> listOperacionesLiquidacionTransporte = new ArrayList<>();
		consultaOperacionesLiqTransporte.forEach(entity -> listOperacionesLiquidacionTransporte
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacionPage(consultaOperacionesLiqTransporte, filtrosParametrosTransporte.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getEliminadasTransporte(ParametrosFiltroCostoTransporteDTO filtrosCostosTransporteDTO) {
		
		var ldtFechaServicioTransporteParsed = transformToLocalDateTime(filtrosCostosTransporteDTO.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinalParsed = transformToLocalDateTime(filtrosCostosTransporteDTO.getFechaServicioTransporteFinal());
		
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
				filtrosCostosTransporteDTO.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesLiqTransporteDTO = new ArrayList<>();
		consultaOperacionesLiq.forEach(entity -> operacionesLiqTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacionPage(consultaOperacionesLiq, filtrosCostosTransporteDTO.getPage());
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
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			CostosTransporte costoTransporte = costosTransporteRepository.findById(idRegistro).orElse(null);
			
			if (Objects.nonNull(costoTransporte)) {
				String estadoConciliacion = costoTransporte.getEstadoConciliacionTransporte();
				Long idLiquidacionTransporte = costoTransporte.getIdLiquidacionTransporte();
				Integer tipoTransaccionTransporte = costoTransporte.getTipoTransaccionTransporte();
				
				continuarFlag = desconciliarParametroLiquidacionCosto(estadoConciliacion, tipoTransaccionTransporte, idLiquidacionTransporte);
				
			    if (continuarFlag) {
					//actualizar estado de registro inicial
					costoTransporte.setIdLiquidacionTransporte(0l);
					costoTransporte.setTipoTransaccionTransporte(0);
					costoTransporte.setEstadoConciliacionTransporte(Dominios.ESTADO_VALIDACION_EN_CONCILIACION);
					costoTransporte.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
					costoTransporte.setFechaModificacionTransporte(timestampTransporte);
						
					costosTransporteRepository.save(costoTransporte);
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
			Long idReg = f.getIdRegistro();
			String operacionEstadoAceptadosRechazados = f.getOperacionEstado();
			String observacionATHAceptadosRechazados = f.getObservacion();
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			CostosTransporte costoTranporte = costosTransporteRepository.findById(idReg).orElse(null);
			
			if (Objects.nonNull(costoTranporte)) {
								
				if (operacionEstadoAceptadosRechazados.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR))
				{
					
					Long idLiquidacionParametros = aceptarParametroLiquidacionCosto(costoTranporte);
					if (idLiquidacionParametros.compareTo(0l)>0)
					{
						continuarTransporte = true;
						costoTranporte.setIdLiquidacionTransporte(idLiquidacionParametros);
						costoTranporte.setTipoTransaccionTransporte(1);
						costoTranporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_MANUAL);
					}
				}
				
				if (operacionEstadoAceptadosRechazados.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuarTransporte = true;
					costoTranporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_RECHAZADA);
				}
				
			    if (continuarTransporte) {
					//actualizar estado de registro
			    	costoTranporte.setObservacionesAthTransporte(observacionATHAceptadosRechazados);
					costoTranporte.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
					costoTranporte.setFechaModificacionTransporte(timestampTransporte);
					costosTransporteRepository.save(costoTranporte);
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
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			if (operacionEstadoTransporte.equalsIgnoreCase("ELIMINAR"))
			{
				eliminarParametroLiquidacionCosto(idRegTransporte);
				f.setOperacionEstado("ELIMINADA");
			}
				
			if (operacionEstadoTransporte.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
			{
				guardarCostoRechazado(idRegTransporte, observacionATHTransporte);
				f.setOperacionEstado("RECHAZADA");
				
			}
				
			
		});
		
		return eliminadosRechazados;
	}
	
	private boolean desconciliarParametroLiquidacionCosto(String estadoTransporte, Integer tipoEstado, Long idLiquidacion)
	{
		
		var paso = false;
		
		//Validar si estado es conciliado manual
		//Validar si tipo es 1= eliminar el registro de la tabla parametrosLiquidacionCostos
		if (estadoTransporte.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipoEstado.equals(1)
			)
		{
			var parametroLiquidacion = 
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosByIdFlat(idLiquidacion);
			
			if (Objects.nonNull(parametroLiquidacion))
			{
				parametrosLiquidacionCostosService.f2eliminarParametrosLiquidacionCostos(parametroLiquidacion);
			}
			paso = true;
		}
		
		//Validar si estado es conciliado manual
		//Validar si tipo es 2= actualizar valores antiguos de la tabla parametrosLiquidacionCostos
		if (estadoTransporte.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipoEstado.equals(2))
		{
			
			ObjectMapper objectMapper = new ObjectMapper();
			ParametrosLiquidacionCosto parametroLiquidacionCostoTransprote;
			ValoresLiquidadosFlatEntity valoresLiquidadosFlat;
			
			//Buscar valores antiguos
			List<EstadoConciliacionParametrosLiquidacion> oldListEstadoConciliacion = estadoConciliacionParametrosLiquidacionService.buscarLiquidacion(idLiquidacion, 2);
			try {
				if (Objects.nonNull(oldListEstadoConciliacion) && !oldListEstadoConciliacion.isEmpty())
				{
					EstadoConciliacionParametrosLiquidacion oldEstadoConciliacion = oldListEstadoConciliacion.get(0);
					
					parametroLiquidacionCostoTransprote = objectMapper.readValue(oldEstadoConciliacion.getDatosParametrosLiquidacionCostos(), ParametrosLiquidacionCosto.class);
					valoresLiquidadosFlat = objectMapper.readValue(oldEstadoConciliacion.getDatosValoresLiquidados(), ValoresLiquidadosFlatEntity.class);
					
					if (Objects.nonNull(parametroLiquidacionCostoTransprote))
					{
						parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiquidacionCostoTransprote);
					}

					paso = true;

					if (Objects.nonNull(valoresLiquidadosFlat))
					{
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
	
	private Long aceptarParametroLiquidacionCosto(CostosTransporte costoTransport)
	{
	
		Long idLiquidacion = 0l;
		int consecutivoTransporte = Math.toIntExact(costoTransport.getConsecutivoTransporte());
		OperacionesLiquidacionTransporteEntity vwCostoRegistroLiqTransporte = operacionesLiquidacionTransporte.findByConsecutivoRegistro(consecutivoTransporte)
                .orElse(null);
		
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
			parametroLiqCosto.setNombreCliente(costoTransport.getRazonSocialTransporte());
			parametroLiqCosto.setCodigoPropioTdv(costoTransport.getNombrePuntoCargoTransporte());
			parametroLiqCosto.setTipoOperacion(costoTransport.getNombreTipoServicioTransporte());
			parametroLiqCosto.setTipoServicio(costoTransport.getTipoPedidoTransporte());
			parametroLiqCosto.setEscala(costoTransport.getEscalaTransporte());
			parametroLiqCosto.setValorBilletes(costoTransport.getValorTransportadoBillete().doubleValue());
			parametroLiqCosto.setValorMonedas(costoTransport.getValorTransportadoMoneda().doubleValue());
			parametroLiqCosto.setValorTotal(costoTransport.getValorTotalTransportado().doubleValue());
			parametroLiqCosto.setNumeroBolsas(costoTransport.getNumeroBolsasMonedaTransporte().intValue());
			valoresLiquidadosFlatEntity.setCostoFijoParadaFlat(costoTransport.getCostoFijoTransporte().doubleValue());
			valoresLiquidadosFlatEntity.setMilajePorRuteoFlat(costoTransport.getCostoMilajeTransporte().doubleValue());
			valoresLiquidadosFlatEntity.setCostoMonedaFlat(costoTransport.getCostoBolsaTransporte().doubleValue());
			valoresLiquidadosFlatEntity.setCostoCharterFlat(costoTransport.getCostoFletesTransporte().doubleValue());
			valoresLiquidadosFlatEntity.setCostoEmisarioFlat(costoTransport.getCostoEmisariosTransporte().doubleValue());
						
			parametroLiqCosto = parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiqCosto);
			idLiquidacion= parametroLiqCosto.getIdLiquidacion();
			
			valoresLiquidadosFlatEntity.setIdLiquidacionFlat(idLiquidacion);
			valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosFlatEntity);
		}
		
		return idLiquidacion;
		
	}
	
	public Long eliminarParametroLiquidacionCosto(Long id)
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
				estadoConciliacionLiqTransporte.setDatosValoresLiquidados(foto2VarlorLiqFlagTransporte);
				estadoConciliacionLiqTransporte.setDatosDetallesLiquidados(foto3DetalleLiqTransporte);
				estadoConciliacionLiqTransporte.setEstado(1);
				
				//Se guarda la foto de como estaba antes parametro liquidacion
				estadoConciliacionParametrosLiquidacionService.save(estadoConciliacionLiqTransporte);
				
				//se elimina el registro de parametro liquidacion
				parametrosLiquidacionCostosService.f2eliminarParametrosLiquidacionCostos(parametroLiquidacion);
				
			} catch (JsonProcessingException e) {
				//Excepcion no manejada
			}

		}
		
		return eliminadoId;
		
	}
	
	private Long guardarCostoRechazado(Long idParametrosLiqCostos, String observacionTransporte)
	{
		var timestamp = new Timestamp(System.currentTimeMillis());
		var costoObjTransporte = new CostosTransporte();
		var parametroLiquidacionCostosServicio =
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(idParametrosLiqCostos).orElse(null);
		
		if (Objects.nonNull(parametroLiquidacionCostosServicio))
		{
			var bancoTransporte = bancoService.findBancoByCodigoPunto(parametroLiquidacionCostosServicio.getCodigoBanco());
			BigDecimal subTotal = BigDecimal.ZERO;
			costoObjTransporte.setEntidadTransporte(bancoTransporte.getAbreviatura());
			costoObjTransporte.setFacturaTransporte("TRANSPORTE OFICINAS");
			costoObjTransporte.setTipoRegistroTransporte("TRANSPORTE");
			costoObjTransporte.setFechaServicioTransporte(parametroLiquidacionCostosServicio.getFechaEjecucion());
			costoObjTransporte.setIdentificacionClienteTransporte("0");
			costoObjTransporte.setRazonSocialTransporte(parametroLiquidacionCostosServicio.getNombreCliente() != null
					&& !parametroLiquidacionCostosServicio.getNombreCliente().isEmpty() ? parametroLiquidacionCostosServicio.getNombreCliente()
							: "SIN ESPECIFICAR");
			costoObjTransporte.setCodigoPuntoCargoTransporte("0");
			costoObjTransporte.setNombrePuntoCargoTransporte(parametroLiquidacionCostosServicio.getCodigoPropioTdv());
			costoObjTransporte.setCodigoCiiuPuntoTransporte(0);
			costoObjTransporte.setCiudadMunicipioPunto("0");
			costoObjTransporte.setCodigoCiiuFondoTransporte(0);
			costoObjTransporte.setCiudadFondoTransporte("0");
			costoObjTransporte.setNombreTipoServicioTransporte(parametroLiquidacionCostosServicio.getTipoOperacion());
			costoObjTransporte.setTipoPedidoTransporte(parametroLiquidacionCostosServicio.getTipoServicio());
			costoObjTransporte.setEscalaTransporte(parametroLiquidacionCostosServicio.getEscala());
			costoObjTransporte.setExclusivoMonedaTransporte("N");
			costoObjTransporte.setMonedaDivisaTransporte("COP");
			costoObjTransporte.setTrmConversionTransporte(BigDecimal.ZERO);
			costoObjTransporte.setValorTransportadoBillete(UtilsParsing.doubleToDecimal(parametroLiquidacionCostosServicio.getValorBilletes()));
			costoObjTransporte.setValorTransportadoMoneda(UtilsParsing.doubleToDecimal(parametroLiquidacionCostosServicio.getValorMonedas()));
			costoObjTransporte.setValorTotalTransportado(UtilsParsing.doubleToDecimal(parametroLiquidacionCostosServicio.getValorTotal()));
			costoObjTransporte.setNumeroFajosTransporte(BigDecimal.ZERO);
			costoObjTransporte.setNumeroBolsasMonedaTransporte(UtilsParsing.integerToDecimal(parametroLiquidacionCostosServicio.getNumeroBolsas()));
			costoObjTransporte.setCostoFijoTransporte(0l);
			costoObjTransporte.setCostoMilajeTransporte(BigDecimal.ZERO);
			costoObjTransporte.setCostoBolsaTransporte(BigDecimal.ZERO);
			costoObjTransporte.setCostoFletesTransporte(0l);
			costoObjTransporte.setCostoEmisariosTransporte(0l);
			
			var valoresLiquidados = parametroLiquidacionCostosServicio.getValoresLiquidados();
			
			if (Objects.nonNull(valoresLiquidados))
			{
				costoObjTransporte.setCostoFijoTransporte(UtilsParsing.doubleToLong(valoresLiquidados.getCostoFijoParada()));
				subTotal = subTotal.add(BigDecimal.valueOf(costoObjTransporte.getCostoFijoTransporte()));
				
				
				costoObjTransporte.setCostoMilajeTransporte(BigDecimal.valueOf(
			             valoresLiquidados.getMilajePorRuteo()+
						 valoresLiquidados.getMilajeVerificacion()
						 ));
				subTotal = subTotal.add(costoObjTransporte.getCostoMilajeTransporte());
				
				costoObjTransporte.setCostoBolsaTransporte(BigDecimal.valueOf(valoresLiquidados.getCostoMoneda()));
				subTotal = subTotal.add(costoObjTransporte.getCostoBolsaTransporte());
				
				costoObjTransporte.setCostoFletesTransporte(
			             valoresLiquidados.getCostoCharter().longValue()+
						 valoresLiquidados.getTasaAeroportuaria().longValue()
						 );
				subTotal = subTotal.add(BigDecimal.valueOf(costoObjTransporte.getCostoFletesTransporte()));
				
				costoObjTransporte.setCostoEmisariosTransporte(valoresLiquidados.getCostoEmisario().longValue());
				subTotal = subTotal.add(BigDecimal.valueOf(costoObjTransporte.getCostoEmisariosTransporte()));
			}
			
			costoObjTransporte.setOtros1(0l);
			costoObjTransporte.setOtros2(0l);
			costoObjTransporte.setOtros3(0l);
			costoObjTransporte.setOtros4(0l);
			costoObjTransporte.setOtros5(0l);
			costoObjTransporte.setSubtotalTransporte(subTotal);
			costoObjTransporte.setIvaTransporte(BigDecimal.ZERO);
			costoObjTransporte.setValorTotalTransporte(subTotal);
			costoObjTransporte.setObservacionesAthTransporte(observacionTransporte);
			
			costoObjTransporte.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_RECHAZADA);
			costoObjTransporte.setEstadoTransporte(Constantes.REGISTRO_ACTIVO);

			costoObjTransporte.setIdArchivoCargadoTransporte(0l);
			costoObjTransporte.setIdRegistroTransporte(idParametrosLiqCostos);

			costoObjTransporte.setUsuarioCreacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
			costoObjTransporte.setFechaCreacionTransporte(timestamp);
			costoObjTransporte.setUsuarioModificacionTransporte(null);
			costoObjTransporte.setFechaModificacionTransporte(null);
			costoObjTransporte.setIdLiquidacionTransporte(idParametrosLiqCostos);
			costoObjTransporte.setTipoTransaccionTransporte(3);
			
			costosTransporteRepository.save(costoObjTransporte);
			
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
			String operacionEstadoStr = f.getOperacionEstado();
			String observacionATHTransporteDiferencia = f.getObservacion();
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA); 
			
			//Obtener el estado del registro de la base de datos
			CostosTransporte costosTransporteDif = costosTransporteRepository.findById(id).orElse(null);
			
			if (Objects.nonNull(costosTransporteDif)) {
								
				if (operacionEstadoStr.toUpperCase().equals(Constantes.OPERACION_ACEPTAR))
				{
					Long idLiquidacionTransporteDif = f.getIdLiquidacion();

					aceptarCostoTransporteIdentificadasConDiferencia(costosTransporteDif, idLiquidacionTransporteDif);

					if (idLiquidacionTransporteDif.compareTo(0l)>0)
					{	
						continuarBandera = true;
						costosTransporteDif.setIdLiquidacionTransporte(idLiquidacionTransporteDif);
						costosTransporteDif.setTipoTransaccionTransporte(2);
						costosTransporteDif.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_MANUAL);
					}
				}
				
				if (operacionEstadoStr.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuarBandera = true;
					costosTransporteDif.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_RECHAZADA);
					
				}
				
			    if (continuarBandera) {
			    	costosTransporteDif.setObservacionesAthTransporte(observacionATHTransporteDiferencia);
					costosTransporteDif.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
					costosTransporteDif.setFechaModificacionTransporte(timestampCurrent);
					costosTransporteRepository.save(costosTransporteDif);
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
				salvarValoresLiquidadosLiquidacionCostos(liquidacionCostosTransporteParametro, valoresLiquidadosParametro);
			
				liquidacionCostosTransporteParametro.setTipoOperacion(costoTransporteDif.getNombreTipoServicioTransporte());
				liquidacionCostosTransporteParametro.setEscala(costoTransporteDif.getEscalaTransporte());
				liquidacionCostosTransporteParametro.setValorBilletes(costoTransporteDif.getValorTransportadoBillete().doubleValue());
				liquidacionCostosTransporteParametro.setValorMonedas(costoTransporteDif.getValorTransportadoMoneda().doubleValue());
				liquidacionCostosTransporteParametro.setValorTotal(costoTransporteDif.getValorTotalTransportado().doubleValue());
				liquidacionCostosTransporteParametro.setNumeroBolsas(costoTransporteDif.getNumeroBolsasMonedaTransporte().intValue());
				
				valoresLiquidadosParametro.setCostoFijoParadaFlat(costoTransporteDif.getCostoFijoTransporte().doubleValue());
				
				double milajeVerificacionFlat = valoresLiquidadosParametro.getMilajeVerificacionFlat() != null 
					    ? valoresLiquidadosParametro.getMilajeVerificacionFlat() 
					    : 0;
				
				valoresLiquidadosParametro.setMilajePorRuteoFlat(UtilsString.calcularDiferenciaAbsoluta(
						costoTransporteDif.getCostoMilajeTransporte().doubleValue(),
						milajeVerificacionFlat));
				valoresLiquidadosParametro.setCostoMonedaFlat(costoTransporteDif.getCostoBolsaTransporte().doubleValue());
				
				double tasaAeroportuariaFlat = valoresLiquidadosParametro.getTasaAeroportuariaFlat() != null 
					    ? valoresLiquidadosParametro.getTasaAeroportuariaFlat() 
					    : 0;
				
				valoresLiquidadosParametro.setCostoCharterFlat(UtilsString.calcularDiferenciaAbsoluta(
						costoTransporteDif.getCostoFletesTransporte().doubleValue(),
						tasaAeroportuariaFlat));
				valoresLiquidadosParametro.setCostoEmisarioFlat(costoTransporteDif.getCostoEmisariosTransporte().doubleValue());
				costoTransporteDif.getSubtotalTransporte(); //Revisar donde conseguir este parámetro
							
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

				estadoConciliacionParametrosLiquidacion.setIdLiquidacion(liquidacionCostoParametro.getIdLiquidacion());
				estadoConciliacionParametrosLiquidacion.setDatosParametrosLiquidacionCostos(imgParametroLiqCostos);
				estadoConciliacionParametrosLiquidacion.setDatosValoresLiquidados(imgparametroValLiquidados);
				estadoConciliacionParametrosLiquidacion.setEstado(2);

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
	public List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasTransporte(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> registrosReintegrados = registros.getRegistroOperacion();
		
		registrosReintegrados.forEach(f->{
			boolean continuar = false;
			Long id = f.getIdRegistro();
			String operacion = f.getOperacionEstado();
			
			f.setOperacionEstado("NO PUDO REALIZAR LA OPERACION");
			
			if (operacion.equalsIgnoreCase("REINTEGRAR"))
			{
				continuar = reintegrarRegistrosLiquidados(id, continuar);
			}
			if (continuar) {
				f.setOperacionEstado("REINTEGRADA");
			}
		});
		return registrosReintegrados;
	}

	public boolean reintegrarRegistrosLiquidados(Long id, boolean continuar)
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
			valoresLiquidados = objectMapper.readValue(estadoConciliacionParametrosLiquidacion.getDatosValoresLiquidados(), ValoresLiquidadosFlatEntity.class);
			TypeReference<List<DetallesLiquidacionCostoFlatEntity>> typeReference = new TypeReference<List<DetallesLiquidacionCostoFlatEntity>>() {};
			detallesLiquidacionCosto = objectMapper.readValue(estadoConciliacionParametrosLiquidacion.getDatosDetallesLiquidados(),typeReference);

			if (Objects.nonNull(parametroLiquidacion) && Objects.nonNull(valoresLiquidados) && Objects.nonNull(detallesLiquidacionCosto))
			{	parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostosFlat(parametroLiquidacion);
				valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidados);
				detallesLiquidacionCostoService.f2actualizarListaDetallesValoresLiquidados(detallesLiquidacionCosto);
				continuar= true;
			}
		} catch (JsonProcessingException e) {
			continuar = false;
		}
		if(continuar){
			estadoConciliacionParametrosLiquidacionService.delete(estadoConciliacionParametrosLiquidacion);
		}
		return continuar;
	}
	
	public List<CostosTransporte> getByIdArchivoCargado(Long idArchivo)
	{
		return costosTransporteRepository.findByIdArchivoCargado(idArchivo);
	}
	
	
	public void aceptarConciliacionRegistro(Long idArchivoCargado)
	{
		costosTransporteRepository.actualizarEstadoByIdArchivoCargado(idArchivoCargado, Constantes.ESTADO_CONCILIACION_ACEPTADO);
	}
	
}
