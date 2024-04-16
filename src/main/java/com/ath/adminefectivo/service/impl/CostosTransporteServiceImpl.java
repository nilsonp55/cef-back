package com.ath.adminefectivo.service.impl;

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
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.ConciliacionCostosTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionTransporteDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;

import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;
import com.ath.adminefectivo.repositories.ICostosTransporteRepository;
import com.ath.adminefectivo.repositories.IOperacionesLiquidacionTransporte;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICostosTransporteService;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import com.ath.adminefectivo.utils.UtilsString;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	ICostosTransporteRepository costosTransporteRepository;
	
	@Autowired
	IBancosService bancoService;

	
	/**
	 * Metodo encargado de realizar la persistencia en la tabla de costo_transporte 
	 * 
	 * @param ValidacionArchivoDTO
	 * @return Long
	 * @author hector.mercado
	 */	
	@Override
	public Long persistir(ValidacionArchivoDTO validacionArchivo) {

		List<CostosTransporte> listCosto = costoFromValidacionArchivo(validacionArchivo);

		saveAll(listCosto);

		return null;
	}
	
	/**
	 * Metodo encargado de realizar la persistencia de una lista en la tabla de costo_transporte 
	 * 
	 * @param List<CostosTransporte>
	 * @return void
	 * @author hector.mercado
	 */	
	@Override
	public void saveAll(List<CostosTransporte> costo) {

		costosTransporteRepository.saveAll(costo);

	}

	private List<CostosTransporte> costoFromValidacionArchivo(ValidacionArchivoDTO validacionArchivo) {

		var timestamp = new Timestamp(System.currentTimeMillis());
		
		List<String> listaDominioFecha = new ArrayList<>();
		listaDominioFecha.add(Constantes.FECHA_PATTERN_DD_MM_YYYY_WITH_SLASH);

		List<CostosTransporte> listTransporte = new ArrayList<>();

		// Obtener Lineas Validadas
		List<ValidacionLineasDTO> lineas = validacionArchivo.getValidacionLineas();

		lineas.forEach(f -> {
			var costoTransporte = new CostosTransporte();
			var contenidoArchivoTransporte = f.getContenido();
			costoTransporte.setEntidadTransporte(contenidoArchivoTransporte.get(0));
			costoTransporte.setFacturaTransporte(contenidoArchivoTransporte.get(1));
			costoTransporte.setTipoRegistroTransporte(contenidoArchivoTransporte.get(2));
			costoTransporte.setFechaServicioTransporte(UtilsString.toDate(contenidoArchivoTransporte.get(3), listaDominioFecha));

			costoTransporte.setIdentificacionClienteTransporte(contenidoArchivoTransporte.get(4));
			costoTransporte.setRazonSocialTransporte(contenidoArchivoTransporte.get(5));
			costoTransporte.setCodigoPuntoCargoTransporte(contenidoArchivoTransporte.get(6));
			costoTransporte.setNombrePuntoCargoTransporte(contenidoArchivoTransporte.get(7));

			costoTransporte.setCodigoCiiuPuntoTransporte(UtilsString.toInteger(contenidoArchivoTransporte.get(8)));
			costoTransporte.setCiudadMunicipioPunto(contenidoArchivoTransporte.get(9));
			costoTransporte.setCodigoCiiuFondoTransporte(UtilsString.toInteger(contenidoArchivoTransporte.get(10)));
			costoTransporte.setCiudadFondoTransporte(contenidoArchivoTransporte.get(11));

			costoTransporte.setNombreTipoServicioTransporte(contenidoArchivoTransporte.get(12));
			costoTransporte.setTipoPedidoTransporte(contenidoArchivoTransporte.get(13));

			costoTransporte.setEscalaTransporte(contenidoArchivoTransporte.get(14));

			costoTransporte.setExclusivoMonedaTransporte(contenidoArchivoTransporte.get(15));
			costoTransporte.setMonedaDivisaTransporte(contenidoArchivoTransporte.get(16));

			costoTransporte.setTrmConversionTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(17), Constantes.DECIMAL_SEPARATOR));

			costoTransporte.setValorTransportadoBillete(UtilsString.toDecimal(contenidoArchivoTransporte.get(18), Constantes.DECIMAL_SEPARATOR));
			costoTransporte.setValorTransportadoMoneda(UtilsString.toDecimal(contenidoArchivoTransporte.get(19), Constantes.DECIMAL_SEPARATOR));
			costoTransporte.setValorTotalTransportado(UtilsString.toDecimal(contenidoArchivoTransporte.get(20), Constantes.DECIMAL_SEPARATOR));

			costoTransporte.setNumeroFajosTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(21), Constantes.DECIMAL_SEPARATOR));
			costoTransporte.setNumeroBolsasMonedaTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(22), Constantes.DECIMAL_SEPARATOR));

			costoTransporte.setCostoFijoTransporte(UtilsString.toLong(contenidoArchivoTransporte.get(23)));

			costoTransporte.setCostoMilajeTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(24), Constantes.DECIMAL_SEPARATOR));
			costoTransporte.setCostoBolsaTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(25), Constantes.DECIMAL_SEPARATOR));

			costoTransporte.setCostoFletesTransporte(UtilsString.toLong(contenidoArchivoTransporte.get(26)));
			costoTransporte.setCostoEmisariosTransporte(UtilsString.toLong(contenidoArchivoTransporte.get(27)));
			costoTransporte.setOtros1(UtilsString.toLong(contenidoArchivoTransporte.get(28)));
			costoTransporte.setOtros2(UtilsString.toLong(contenidoArchivoTransporte.get(29)));
			costoTransporte.setOtros3(UtilsString.toLong(contenidoArchivoTransporte.get(30)));
			costoTransporte.setOtros4(UtilsString.toLong(contenidoArchivoTransporte.get(31)));
			costoTransporte.setOtros5(UtilsString.toLong(contenidoArchivoTransporte.get(32)));

			costoTransporte.setSubtotalTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(33), Constantes.DECIMAL_SEPARATOR));
			costoTransporte.setIvaTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(34), Constantes.DECIMAL_SEPARATOR));
			costoTransporte.setValorTotalTransporte(UtilsString.toDecimal(contenidoArchivoTransporte.get(35), Constantes.DECIMAL_SEPARATOR));

			costoTransporte.setObservacionesAthTransporte(contenidoArchivoTransporte.get(36));
			costoTransporte.setObservacionesTdvTransporte(contenidoArchivoTransporte.get(37));

			costoTransporte.setEstadoConciliacionTransporte(Constantes.ESTADO_PROCESO_PENDIENTE);
			costoTransporte.setEstadoTransporte(Constantes.REGISTRO_ACTIVO);

			costoTransporte.setIdArchivoCargadoTransporte(validacionArchivo.getIdArchivo());
			costoTransporte.setIdRegistroTransporte(Long.valueOf(f.getNumeroLinea()));

			costoTransporte.setUsuarioCreacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);

			costoTransporte.setFechaCreacionTransporte(timestamp);

			costoTransporte.setUsuarioModificacionTransporte(null);

			costoTransporte.setFechaModificacionTransporte(null);

			listTransporte.add(costoTransporte);

		});

		return listTransporte;
	}
	
	/**
	 * Metodo encargado de realizar la busqueda en la tabla de Costos_transporte 
	 * 
	 * @param entidad
	 * @param fechaServicioTransporte
	 * @param codigoPuntoCargo
	 * @param nombrePuntoCargo
	 * @param ciudadFondo
	 * @param nombreTipoServicio
	 * @return List<CostosTransporte>
	 * @author hector.mercado
	 */	
	@Override
	public List<CostosTransporte> findAceptados(String entidad, Date fechaServicioTransporte, String codigoPuntoCargo,
			String nombrePuntoCargo, String ciudadFondo, String nombreTipoServicio) {
	
	
		return costosTransporteRepository.findByEstadoEntidadFechaServicio(entidad,
																			fechaServicioTransporte,
																			codigoPuntoCargo,
																			nombrePuntoCargo,
																			ciudadFondo,
																			nombreTipoServicio,
																			Dominios.ESTADO_VALIDACION_ACEPTADO);
	}

	@Override
	public List<CostosTransporte> conciliadas(String entidad, String identificacion) {
		return costosTransporteRepository.conciliadas(entidad, identificacion);
	}

	@Override
	public List<ConciliacionCostosTransporteDTO> conciliadasDto(String entidad, String identificacion) {

		return toListDTO(conciliadas(entidad, identificacion));

	}

	public ConciliacionCostosTransporteDTO toDTO(CostosTransporte ent) {

		return ConciliacionCostosTransporteDTO.builder().consecutivo(ent.getConsecutivoTransporte())
				.entidad(ent.getEntidadTransporte())
				.factura(ent.getFacturaTransporte())
				.tipoRegistro(ent.getTipoRegistroTransporte())
				.fechaServicioTransporte(ent.getFechaServicioTransporte())
				.identificacionCliente(ent.getIdentificacionClienteTransporte())
				.razonSocial(ent.getRazonSocialTransporte())
				.codigoPuntoCargo(ent.getCodigoPuntoCargoTransporte())
				.nombrePuntoCargo(ent.getNombrePuntoCargoTransporte())
				.codigoCiiuPunto(ent.getCodigoCiiuPuntoTransporte())
				.ciudadMunicipioPunto(ent.getCiudadMunicipioPunto())
				.codigoCiiuFondo(ent.getCodigoCiiuFondoTransporte())
				.ciudadFondo(ent.getCiudadFondoTransporte())
				.nombreTipoServicio(ent.getNombreTipoServicioTransporte())
				.tipoPedido(ent.getTipoPedidoTransporte())
				.escala(ent.getEscalaTransporte())
				.exclusivoMoneda(ent.getExclusivoMonedaTransporte())
				.monedaDivisa(ent.getMonedaDivisaTransporte())
				.trmConversion(ent.getTrmConversionTransporte())
				.valorTransportadoBillete(ent.getValorTransportadoBillete())
				.valorTransportadoMoneda(ent.getValorTransportadoMoneda())
				.valorTotalTransportado(ent.getValorTotalTransportado())
				.numeroFajos(ent.getNumeroFajosTransporte())
				.numeroBolsasMoneda(ent.getNumeroBolsasMonedaTransporte())
				.costoFijo(ent.getCostoFijoTransporte())
				.costoMilaje(ent.getCostoMilajeTransporte())
				.costoBolsa(ent.getCostoBolsaTransporte())
				.costoFletes(ent.getCostoFletesTransporte())
				.costoEmisarios(ent.getCostoEmisariosTransporte())
				.otros1(ent.getOtros1())
				.otros2(ent.getOtros2())
				.otros3(ent.getOtros3())
				.otros4(ent.getOtros4())
				.otros5(ent.getOtros5())
				.subtotal(ent.getSubtotalTransporte())
				.iva(ent.getIvaTransporte())
				.valorTotal(ent.getValorTotalTransporte())
				.estadoConciliacion(ent.getEstadoConciliacionTransporte())
				.estado(ent.getEstadoTransporte())
				.observacionesAth(ent.getObservacionesAthTransporte())
				.observacionesTdv(ent.getObservacionesTdvTransporte())
				.idArchivoCargado(ent.getIdArchivoCargadoTransporte())
				.idRegistro(ent.getIdRegistroTransporte())
				.usuarioCreacion(ent.getUsuarioCreacionTransporte())
				.fechaCreacion(ent.getFechaCreacionTransporte())
				.usuarioModificacion(ent.getUsuarioModificacionTransporte())
				.fechaModificacion(ent.getFechaModificacionTransporte()).build();
	}

	public List<ConciliacionCostosTransporteDTO> toListDTO(List<CostosTransporte> listCostosTransporte) {

		return Objects.nonNull(listCostosTransporte)
				? listCostosTransporte.stream().map(this::toDTO).collect(Collectors.toList())
				: new ArrayList<>();

	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionConciliadaTransporte(ParametrosFiltroCostoTransporteDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		
		var consulta = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, 
				filtros.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesLiquidacionTransporteDTO = new ArrayList<>();
		consulta.forEach(entity -> operacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidacionRemitidasNoIdentificadasTransporte(ParametrosFiltroCostoTransporteDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		
		var consulta = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, 
				filtros.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesLiquidacionTransporteDTO = new ArrayList<>();
		consulta.forEach(entity -> operacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacion(consulta, filtros.getPage());
	}

	

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getLiquidadasNoCobradasTransporte(ParametrosFiltroCostoTransporteDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		
		var consulta = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, 
				filtros.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesLiquidacionTransporteDTO = new ArrayList<>();
		consulta.forEach(entity -> operacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionTransporteDTO> getIdentificadasConDiferenciasTransporte(ParametrosFiltroCostoTransporteDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getFechaServicioTransporte());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFechaServicioTransporteFinal());
		
		var consulta = operacionesLiquidacionTransporte.conciliadasLiquidadasTransporte(filtros.getEntidad(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getIdentificacionCliente(), 
				filtros.getRazonSocial(), 
				filtros.getCodigoPuntoCargo(),
				filtros.getNombrePuntoCargo(), 
				filtros.getCiudadFondo(), 
				filtros.getNombreTipoServicio(), 
				filtros.getMonedaDivisa(), 
				filtros.getEstado(),
				Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, 
				filtros.getPage());

		List<OperacionesLiquidacionTransporteDTO> operacionesLiquidacionTransporteDTO = new ArrayList<>();
		consulta.forEach(entity -> operacionesLiquidacionTransporteDTO
				.add(OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(entity)));

		return liquidacion(consulta, filtros.getPage());
	}

	
	
	private Page<OperacionesLiquidacionTransporteDTO> liquidacion(
			Page<OperacionesLiquidacionTransporteEntity> liquidadas, Pageable page) {

		return new PageImpl<>(
				liquidadas.getContent().stream().map(e -> OperacionesLiquidacionTransporteDTO.CONVERTER_DTO.apply(e))
						.collect(Collectors.<OperacionesLiquidacionTransporteDTO>toList()),
				page, liquidadas.getTotalElements());
	}
	
	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }

	/**
	 * Metodo encargado de desconciliar los registros de costos_transporte 
	 * 
	 * @param entidad
	 * @return List<CostosTransporte>
	 * @author hector.mercado
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> conciliados = registros.getRegistroOperacion();
		var timestamp = new Timestamp(System.currentTimeMillis());
		
		conciliados.forEach(f->{
			var continuar = false;
			Long id = f.getIdRegistro();
			f.setOperacionEstado("NO PUDO REALIZAR LA OPERACION");
			
			//Obtener el estado del registro de la base de datos
			CostosTransporte costo = costosTransporteRepository.findById(id).orElse(null);
			
			if (Objects.nonNull(costo)) {
				String estado = costo.getEstadoConciliacionTransporte();
				Long idLiquidacion = costo.getIdLiquidacionTransporte();
				Integer tipo = costo.getTipoTransaccionTransporte();
				
				continuar = desconciliarParametroLiquidacionCosto(estado, tipo, idLiquidacion);
				
			    if (continuar) {
					//actualizar estado de registro inicial
					costo.setIdLiquidacionTransporte(0l);
					costo.setTipoTransaccionTransporte(0);
					costo.setEstadoConciliacionTransporte(Dominios.ESTADO_VALIDACION_EN_CONCILIACION);
					costo.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
					costo.setFechaModificacionTransporte(timestamp);
						
					costosTransporteRepository.save(costo);
			    	f.setOperacionEstado("DESCONCILIADO");
			    }
				
			}
			
		});
		
		return conciliados;
	}
	

	/**
	 * Metodo encargado de aceptar o rechazar los registros de costos_transporte 
	 * 
	 * @param entidad
	 * @return List<CostosTransporte>
	 * @author hector.mercado
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> remitidasAceptarRechazar(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> aceptadosRechazados = registros.getRegistroOperacion();
		var timestamp = new Timestamp(System.currentTimeMillis());
		
		aceptadosRechazados.forEach(f->{
			var continuar = false;
			Long id = f.getIdRegistro();
			String operacion = f.getOperacionEstado();
			String observacionATH = f.getObservacion();
			
			f.setOperacionEstado("NO PUDO REALIZAR LA OPERACION");
			
			//Obtener el estado del registro de la base de datos
			CostosTransporte costo = costosTransporteRepository.findById(id).orElse(null);
			
			if (Objects.nonNull(costo)) {
								
				if (operacion.equalsIgnoreCase("ACEPTAR"))
				{
					
					Long idLiquidacion = aceptarParametroLiquidacionCosto(costo);
					if (idLiquidacion.compareTo(0l)>0)
					{
						continuar = true;
						costo.setIdLiquidacionTransporte(idLiquidacion);
						costo.setTipoTransaccionTransporte(1);
						costo.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_MANUAL);
					}
				}
				
				if (operacion.equalsIgnoreCase("RECHAZAR"))
				{
					continuar = true;
					costo.setEstadoConciliacionTransporte(Constantes.ESTADO_CONCILIACION_RECHAZADA);
				}
				
			    if (continuar) {
					//actualizar estado de registro
			    	costo.setObservacionesAthTransporte(observacionATH);
					costo.setUsuarioModificacionTransporte(Constantes.USUARIO_PROCESA_ARCHIVO);
					costo.setFechaModificacionTransporte(timestamp);
					costosTransporteRepository.save(costo);
					f.setOperacionEstado("APLICADO");
			    }
			}
		});
		return aceptadosRechazados;
	}
	
	private boolean desconciliarParametroLiquidacionCosto(String estado, Integer tipo, Long idLiquidacion)
	{
		
		var continuar = false;
		
		//Validar si estado es conciliado manual
		//Validar si tipo es 1= eliminar el registro de la tabla parametrosLiquidacionCostos
		if (estado.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipo.equals(1)	
			)
		{
			var parametroLiquidacion = 
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(idLiquidacion);
			
			if (Objects.nonNull(parametroLiquidacion))
			{
				parametrosLiquidacionCostosService.f2eliminarParametrosLiquidacionCostos(parametroLiquidacion);
			}
			continuar = true;
		}
		
		//Validar si estado es conciliado manual
		//Validar si tipo es 2= actualizar valores antiguos de la tabla parametrosLiquidacionCostos
		if (estado.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipo.equals(2))
		{
			
			ObjectMapper objectMapper = new ObjectMapper();
			ParametrosLiquidacionCosto parametroLiquidacion;
			//Buscar valores antiguos
			List<EstadoConciliacionParametrosLiquidacion> oldList = estadoConciliacionParametrosLiquidacionService.buscarLiquidacion(idLiquidacion, 2);
			try {
				if (Objects.nonNull(oldList) && !oldList.isEmpty())
				{
					EstadoConciliacionParametrosLiquidacion old = oldList.get(0);
					
					parametroLiquidacion = objectMapper.readValue(old.getDatosParametrosLiquidacionCostos(), ParametrosLiquidacionCosto.class);
					if (Objects.nonNull(parametroLiquidacion))
					{
						parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiquidacion);
					}
					continuar = true;
				}

			} catch (JsonProcessingException e) {
				continuar = false;
			}
		   
		   				
		}
		
		
		return continuar;
		
	}
	
	private Long aceptarParametroLiquidacionCosto(CostosTransporte costo)
	{
	
		Long idLiquidacion = 0l;
	
		if (costo.getEstadoConciliacionTransporte().equals(Dominios.ESTADO_VALIDACION_EN_CONCILIACION))
		{
			var parametro = new ParametrosLiquidacionCosto();
			var valorLiq = new ValoresLiquidadosFlatEntity();
		
			var banco = bancoService.findBancoByAbreviatura(costo.getEntidadTransporte());
			
			parametro.setCodigoBanco(banco.getCodigoPunto());
			parametro.setFechaEjecucion(costo.getFechaServicioTransporte());
			parametro.setNombreCliente(costo.getRazonSocialTransporte());
			parametro.setCodigoPropioTdv(costo.getCodigoPuntoCargoTransporte()+costo.getNombrePuntoCargoTransporte());
			
			parametro.setTipoOperacion(costo.getNombreTipoServicioTransporte());
			parametro.setTipoServicio(costo.getTipoPedidoTransporte());
			parametro.setEscala(costo.getEscalaTransporte());
			
			parametro.setValorBilletes(costo.getValorTransportadoBillete().doubleValue());
			parametro.setValorMonedas(costo.getValorTransportadoMoneda().doubleValue());
			parametro.setValorTotal(costo.getValorTotalTransportado().doubleValue());
			
			parametro.setNumeroBolsas(costo.getNumeroBolsasMonedaTransporte().intValue());
			
			valorLiq.setCostoFijoParadaFlat(costo.getCostoFijoTransporte().doubleValue());
			valorLiq.setMilajePorRuteoFlat(costo.getCostoMilajeTransporte().doubleValue());
			valorLiq.setCostoMonedaFlat(costo.getCostoBolsaTransporte().doubleValue());
			valorLiq.setCostoCharterFlat(costo.getCostoFletesTransporte().doubleValue());
			valorLiq.setCostoEmisarioFlat(costo.getCostoEmisariosTransporte().doubleValue());
						
			parametro = parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametro);
			idLiquidacion= parametro.getIdLiquidacion();
			
			valorLiq.setIdLiquidacionFlat(idLiquidacion);
			valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valorLiq);
		}
		
		return idLiquidacion;
		
	}
	
	
}
