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

		List<CostosTransporte> list = new ArrayList<>();

		// Obtener Lineas Validadas
		List<ValidacionLineasDTO> lineas = validacionArchivo.getValidacionLineas();

		lineas.forEach(f -> {
			var costo = new CostosTransporte();
			var contenido = f.getContenido();
			costo.setEntidad(contenido.get(0));
			costo.setFactura(contenido.get(1));
			costo.setTipoRegistro(contenido.get(2));
			costo.setFechaServicioTransporte(UtilsString.toDate(contenido.get(3), listaDominioFecha));

			costo.setIdentificacionCliente(contenido.get(4));
			costo.setRazonSocial(contenido.get(5));
			costo.setCodigoPuntoCargo(contenido.get(6));
			costo.setNombrePuntoCargo(contenido.get(7));

			costo.setCodigoCiiuPunto(UtilsString.toInteger(contenido.get(8)));
			costo.setCiudadMunicipioPunto(contenido.get(9));
			costo.setCodigoCiiuFondo(UtilsString.toInteger(contenido.get(10)));
			costo.setCiudadFondo(contenido.get(11));

			costo.setNombreTipoServicio(contenido.get(12));
			costo.setTipoPedido(contenido.get(13));

			costo.setEscala(contenido.get(14));

			costo.setExclusivoMoneda(contenido.get(15));
			costo.setMonedaDivisa(contenido.get(16));

			costo.setTrmConversion(UtilsString.toDecimal(contenido.get(17), Constantes.DECIMAL_SEPARATOR));

			costo.setValorTransportadoBillete(UtilsString.toDecimal(contenido.get(18), Constantes.DECIMAL_SEPARATOR));
			costo.setValorTransportadoMoneda(UtilsString.toDecimal(contenido.get(19), Constantes.DECIMAL_SEPARATOR));
			costo.setValorTotalTransportado(UtilsString.toDecimal(contenido.get(20), Constantes.DECIMAL_SEPARATOR));

			costo.setNumeroFajos(UtilsString.toDecimal(contenido.get(21), Constantes.DECIMAL_SEPARATOR));
			costo.setNumeroBolsasMoneda(UtilsString.toDecimal(contenido.get(22), Constantes.DECIMAL_SEPARATOR));

			costo.setCostoFijo(UtilsString.toLong(contenido.get(23)));

			costo.setCostoMilaje(UtilsString.toDecimal(contenido.get(24), Constantes.DECIMAL_SEPARATOR));
			costo.setCostoBolsa(UtilsString.toDecimal(contenido.get(25), Constantes.DECIMAL_SEPARATOR));

			costo.setCostoFletes(UtilsString.toLong(contenido.get(26)));
			costo.setCostoEmisarios(UtilsString.toLong(contenido.get(27)));
			costo.setOtros1(UtilsString.toLong(contenido.get(28)));
			costo.setOtros2(UtilsString.toLong(contenido.get(29)));
			costo.setOtros3(UtilsString.toLong(contenido.get(30)));
			costo.setOtros4(UtilsString.toLong(contenido.get(31)));
			costo.setOtros5(UtilsString.toLong(contenido.get(32)));

			costo.setSubtotal(UtilsString.toDecimal(contenido.get(33), Constantes.DECIMAL_SEPARATOR));
			costo.setIva(UtilsString.toDecimal(contenido.get(34), Constantes.DECIMAL_SEPARATOR));
			costo.setValorTotal(UtilsString.toDecimal(contenido.get(35), Constantes.DECIMAL_SEPARATOR));

			costo.setObservacionesAth(contenido.get(36));
			costo.setObservacionesTdv(contenido.get(37));

			costo.setEstadoConciliacion(Constantes.ESTADO_PROCESO_PENDIENTE);
			costo.setEstado(Constantes.REGISTRO_ACTIVO);

			costo.setIdArchivoCargado(validacionArchivo.getIdArchivo());
			costo.setIdRegistro(Long.valueOf(f.getNumeroLinea()));

			costo.setUsuarioCreacion(Constantes.USUARIO_PROCESA_ARCHIVO);

			costo.setFechaCreacion(timestamp);

			costo.setUsuarioModificacion(null);

			costo.setFechaModificacion(null);

			list.add(costo);

		});

		return list;
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

		return ConciliacionCostosTransporteDTO.builder().consecutivo(ent.getConsecutivo()).entidad(ent.getEntidad())
				.factura(ent.getFactura()).tipoRegistro(ent.getTipoRegistro())
				.fechaServicioTransporte(ent.getFechaServicioTransporte())
				.identificacionCliente(ent.getIdentificacionCliente()).razonSocial(ent.getRazonSocial())
				.codigoPuntoCargo(ent.getCodigoPuntoCargo()).nombrePuntoCargo(ent.getNombrePuntoCargo())
				.codigoCiiuPunto(ent.getCodigoCiiuPunto()).ciudadMunicipioPunto(ent.getCiudadMunicipioPunto())
				.codigoCiiuFondo(ent.getCodigoCiiuFondo()).ciudadFondo(ent.getCiudadFondo())
				.nombreTipoServicio(ent.getNombreTipoServicio()).tipoPedido(ent.getTipoPedido()).escala(ent.getEscala())
				.exclusivoMoneda(ent.getExclusivoMoneda()).monedaDivisa(ent.getMonedaDivisa())
				.trmConversion(ent.getTrmConversion()).valorTransportadoBillete(ent.getValorTransportadoBillete())
				.valorTransportadoMoneda(ent.getValorTransportadoMoneda())
				.valorTotalTransportado(ent.getValorTotalTransportado()).numeroFajos(ent.getNumeroFajos())
				.numeroBolsasMoneda(ent.getNumeroBolsasMoneda()).costoFijo(ent.getCostoFijo())
				.costoMilaje(ent.getCostoMilaje()).costoBolsa(ent.getCostoBolsa()).costoFletes(ent.getCostoFletes())
				.costoEmisarios(ent.getCostoEmisarios()).otros1(ent.getOtros1()).otros2(ent.getOtros2())
				.otros3(ent.getOtros3()).otros4(ent.getOtros4()).otros5(ent.getOtros5()).subtotal(ent.getSubtotal())
				.iva(ent.getIva()).valorTotal(ent.getValorTotal()).estadoConciliacion(ent.getEstadoConciliacion())
				.estado(ent.getEstado()).observacionesAth(ent.getObservacionesAth())
				.observacionesTdv(ent.getObservacionesTdv()).idArchivoCargado(ent.getIdArchivoCargado())
				.idRegistro(ent.getIdRegistro()).usuarioCreacion(ent.getUsuarioCreacion())
				.fechaCreacion(ent.getFechaCreacion()).usuarioModificacion(ent.getUsuarioModificacion())
				.fechaModificacion(ent.getFechaModificacion()).build();
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
				String estado = costo.getEstadoConciliacion();
				Long idLiquidacion = costo.getIdLiquidacion();
				Integer tipo = costo.getTipoTransaccion();
				
				continuar = desconciliarParametroLiquidacionCosto(estado, tipo, idLiquidacion);
				
			    if (continuar) {
					//actualizar estado de registro inicial
					costo.setIdLiquidacion(0l);
					costo.setTipoTransaccion(0);
					costo.setEstadoConciliacion(Dominios.ESTADO_VALIDACION_EN_CONCILIACION);
					costo.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
					costo.setFechaModificacion(timestamp);
						
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
						costo.setIdLiquidacion(idLiquidacion);
						costo.setTipoTransaccion(1);
						costo.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_MANUAL);
					}
				}
				
				if (operacion.equalsIgnoreCase("RECHAZAR"))
				{
					continuar = true;
					costo.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_RECHAZADA);
				}
				
			    if (continuar) {
					//actualizar estado de registro
			    	costo.setObservacionesAth(observacionATH);
					costo.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
					costo.setFechaModificacion(timestamp);
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
	
		if (costo.getEstadoConciliacion().equals(Dominios.ESTADO_VALIDACION_EN_CONCILIACION))
		{
			var parametro = new ParametrosLiquidacionCosto();
			var valorLiq = new ValoresLiquidadosFlatEntity();
		
			var banco = bancoService.findBancoByAbreviatura(costo.getEntidad());
			
			parametro.setCodigoBanco(banco.getCodigoPunto());
			parametro.setFechaEjecucion(costo.getFechaServicioTransporte());
			parametro.setNombreCliente(costo.getRazonSocial());
			parametro.setCodigoPropioTdv(costo.getCodigoPuntoCargo()+costo.getNombrePuntoCargo());
			
			parametro.setTipoOperacion(costo.getNombreTipoServicio());
			parametro.setTipoServicio(costo.getTipoPedido());
			parametro.setEscala(costo.getEscala());
			
			parametro.setValorBilletes(costo.getValorTransportadoBillete().doubleValue());
			parametro.setValorMonedas(costo.getValorTransportadoMoneda().doubleValue());
			parametro.setValorTotal(costo.getValorTotalTransportado().doubleValue());
			
			parametro.setNumeroBolsas(costo.getNumeroBolsasMoneda().intValue());
			
			valorLiq.setCostoFijoParada(costo.getCostoFijo().doubleValue());
			valorLiq.setMilajePorRuteo(costo.getCostoMilaje().doubleValue());
			valorLiq.setCostoMoneda(costo.getCostoBolsa().doubleValue());
			valorLiq.setCostoCharter(costo.getCostoFletes().doubleValue());
			valorLiq.setCostoEmisario(costo.getCostoEmisarios().doubleValue());
						
			parametro = parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametro);
			idLiquidacion= parametro.getIdLiquidacion();
			
			valorLiq.setIdLiquidacion(idLiquidacion);
			valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valorLiq);
		}
		
		return idLiquidacion;
		
	}
	
	
}
