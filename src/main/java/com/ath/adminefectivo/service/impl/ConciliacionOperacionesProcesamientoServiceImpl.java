package com.ath.adminefectivo.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.entities.CostosProcesamiento;
import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;
import com.ath.adminefectivo.entities.OtrosCostosFondo;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;
import com.ath.adminefectivo.entities.id.OtrosCostosFondoPK;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesProcesamientoRepository;
import com.ath.adminefectivo.repositories.ICostosProcesamientoRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import com.ath.adminefectivo.service.ICostosProcesamientoService;
import com.ath.adminefectivo.service.IDetalleLiquidacionProcesamiento;
import com.ath.adminefectivo.service.IDetalleLiquidacionTransporte;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;
import com.ath.adminefectivo.service.IOtrosCostosFondoService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import com.ath.adminefectivo.utils.UtilsParsing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConciliacionOperacionesProcesamientoServiceImpl implements IConciliacionOperacionesProcesamientoService {

	@Autowired
	IParametrosLiquidacionCostosService parametrosLiquidacionCostosService;
	
	@Autowired
	IEstadoConciliacionParametrosLiquidacionService estadoConciliacionParametrosLiquidacionService;
	
	@Autowired
	IBancosService bancoService;
	
	@Autowired
	IConciliacionOperacionesProcesamientoRepository operacionesLiquidacion;
	
	@Autowired
	ICostosProcesamientoRepository costosProcesamientoRepository;
	
	@Autowired
	IValoresLiquidadosFlatService valoresLiquidadosFlatService;
	
	@Autowired
	CostosTransporteServiceImpl costosTransporteService;
	
	@Autowired
	IOtrosCostosFondoService otrosCostosFondo;
	
	@Autowired
	ICostosProcesamientoService costosProcesamientoService;
	
	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getProcessServiceDate());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFinalProcessServiceDate());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntity(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getClientIdentification(), 
				filtros.getSocialReason(), 
				filtros.getCargoPointCode(),
				filtros.getCargoPointName(), 
				filtros.getCityBackground(), 
				filtros.getServiceTypeName(), 
				filtros.getCurrency(), 
				filtros.getStatus(),
				Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			ParametrosFiltroCostoProcesamientoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getProcessServiceDate());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFinalProcessServiceDate());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntity(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getClientIdentification(), 
				filtros.getSocialReason(), 
				filtros.getCargoPointCode(),
				filtros.getCargoPointName(), 
				filtros.getCityBackground(), 
				filtros.getServiceTypeName(), 
				filtros.getCurrency(), 
				filtros.getStatus(),
				Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, 
				filtros.getPage());
		
		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getProcessServiceDate());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFinalProcessServiceDate());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntity(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getClientIdentification(), 
				filtros.getSocialReason(), 
				filtros.getCargoPointCode(),
				filtros.getCargoPointName(), 
				filtros.getCityBackground(), 
				filtros.getServiceTypeName(), 
				filtros.getCurrency(), 
				filtros.getStatus(),
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, 
				filtros.getPage());
	
		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getProcessServiceDate());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFinalProcessServiceDate());
		costosProcesamientoService.persistirMaestroLlavesProcesamiento();
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntity(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getClientIdentification(), 
				filtros.getSocialReason(), 
				filtros.getCargoPointCode(),
				filtros.getCargoPointName(), 
				filtros.getCityBackground(), 
				filtros.getServiceTypeName(), 
				filtros.getCurrency(), 
				filtros.getStatus(),
				Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, 
				filtros.getPage());
		
		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getEliminadasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
		var ldtFechaServicioTransporte = convertToLocalDateTime(filtros.getProcessServiceDate());
		var ldtFechaServicioTransporteFinal = convertToLocalDateTime(filtros.getFinalProcessServiceDate());
		
		var consulta = operacionesLiquidacion.conciliadasLiquidadasProcesamiento(filtros.getEntity(), 
				ldtFechaServicioTransporte,
				ldtFechaServicioTransporteFinal, 
				filtros.getClientIdentification(), 
				filtros.getSocialReason(), 
				filtros.getCargoPointCode(),
				filtros.getCargoPointName(), 
				filtros.getCityBackground(), 
				filtros.getServiceTypeName(), 
				filtros.getCurrency(), 
				filtros.getStatus(),
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS_ELIMINADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }
	
	private Page<OperacionesLiquidacionProcesamientoDTO> liquidacion(
			Page<OperacionesLiquidacionProcesamientoEntity> liquidadas, Pageable page) {

		return new PageImpl<>(
				liquidadas.getContent().stream().map(e -> OperacionesLiquidacionProcesamientoDTO.CONVERTER_DTO.apply(e))
						.collect(Collectors.<OperacionesLiquidacionProcesamientoDTO>toList()),
				page, liquidadas.getTotalElements());
	}
	
	
	/**
	 * Metodo encargado de desconciliar los registros de costos_procesamiento
	 * 
	 * @param RegistrosConciliacionListDTO
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author hector.mercado
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> desconciliar(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> conciliados = registros.getRegistroOperacion();
		var timestamp = new Timestamp(System.currentTimeMillis());
		
		conciliados.forEach(f->{
			var continuar = false;
			Long id = f.getIdRegistro();
			
			List<IDetalleLiquidacionProcesamiento> detalles = obtenerDetalleLiquidacionProcesamiento(
					Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, id);
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			List<CostosProcesamiento> costoProcesamientoList = obtenerCostoProcesamientoList(
					Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, id);
			
			if (costoProcesamientoList != null && !costoProcesamientoList.isEmpty()) {
				
				String estado = costoProcesamientoList.get(0).getEstadoConciliacion();
				Long idLiquidacion = costosTransporteService.obtenerIdPorTipo(detalles, Constantes.LISTA_CONCILIACION_IDLIQUIDACIONTDV);				
				Integer tipo = costoProcesamientoList.get(0).getTipoTransaccion();
				
				continuar = desconciliarParametroLiquidacionCosto(estado, tipo, idLiquidacion);
				
			    if (continuar) {
					//actualizar estado de registro inicial
			    	 for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
			    		 costoProcesamiento.setIdLiquidacion(idLiquidacion);
			    		 costoProcesamiento.setTipoTransaccion(0);
			    		 costoProcesamiento.setEstadoConciliacion(Dominios.ESTADO_VALIDACION_EN_CONCILIACION);
			    		 costoProcesamiento.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
			    		 costoProcesamiento.setFechaModificacion(timestamp);
			    	 }
					costosProcesamientoRepository.saveAll(costoProcesamientoList);
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
	 * @return List<RegistroOperacionConciliacionDTO>
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
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos		
			List<CostosProcesamiento> costoProcesamientoList = obtenerCostoProcesamientoList(
					Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, id);
			
			if (costoProcesamientoList != null && !costoProcesamientoList.isEmpty()) {
								
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR))
				{
					
					Long idLiquidacion = aceptarParametroLiquidacionCosto(costoProcesamientoList.get(0), id);
					if (idLiquidacion.compareTo(0l)>0)
					{
						continuar = true;
						for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
							costoProcesamiento.setIdLiquidacion(idLiquidacion);
							costoProcesamiento.setTipoTransaccion(1);
							costoProcesamiento.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_MANUAL);
						}
					}
				}
				
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuar = true;
					for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
						costoProcesamiento.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_RECHAZADA);
					}
				}
				
			    if (continuar) {
					//actualizar estado de registro
			    	for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
			    		costoProcesamiento.setObservacionesAth(observacionATH);
			    		costoProcesamiento.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
			    		costoProcesamiento.setFechaModificacion(timestamp);
			    	}
					costosProcesamientoRepository.saveAll(costoProcesamientoList);
					f.setOperacionEstado("APLICADO");
			    }
			}
		});
		return aceptadosRechazados;
	}
	
	/**
	 * Metodo encargado de eliminar o rechazar los registros de costos_transporte 
	 * 
	 * @param registros
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author hector.mercado
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> liquidadasEliminarRechazar(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> eliminadosRechazados = registros.getRegistroOperacion();
		
		eliminadosRechazados.forEach(f->{
			Long id = f.getIdRegistro();
			String operacion = f.getOperacionEstado();
			String observacionATH = f.getObservacion();
			
			List<IDetalleLiquidacionProcesamiento> detalles = obtenerDetalleLiquidacionProcesamiento(
					Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, id);
			
			List<Long> idLiquidacionList = UtilsParsing.parseStringToList(detalles.get(0).getIdsLiquidacionApp());
			
			for (Long idLiquidacion : idLiquidacionList) {
				f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);

				if (operacion.equalsIgnoreCase("ELIMINAR")) {
					costosTransporteService.eliminarParametroLiquidacionCosto(idLiquidacion, Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO);
					f.setOperacionEstado("ELIMINADA");
				}

				if (operacion.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR)) {
					guardarCostoRechazado(idLiquidacion, observacionATH);
					f.setOperacionEstado("RECHAZADA");

				}
			}
			
		});
		
		return eliminadosRechazados;
	}
	
	private boolean desconciliarParametroLiquidacionCosto(String estado, Integer tipo, Long idLiquidacion) {

		var continuar = false;

		// Validar si estado es conciliado manual
		// Validar si tipo es 1= eliminar el registro de la tabla
		// parametrosLiquidacionCostos
		if (estado.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipo.equals(1)) {
			var parametroLiquidacion = parametrosLiquidacionCostosService
					.getParametrosLiquidacionCostosByIdFlat(idLiquidacion);

			if (Objects.nonNull(parametroLiquidacion)) {
				parametrosLiquidacionCostosService.f2eliminarParametrosLiquidacionCostos(parametroLiquidacion);
			}
			continuar = true;
		}

		// Validar si estado es conciliado manual
		// Validar si tipo es 2= actualizar valores antiguos de la tabla
		// parametrosLiquidacionCostos
		if (estado.equals(Dominios.ESTADO_VALIDACION_CONCILIACION_MANUAL) && tipo.equals(2)) {

			ObjectMapper objectMapper = new ObjectMapper();
			ParametrosLiquidacionCosto parametroLiquidacion;
			ValoresLiquidadosFlatEntity valoresLiquidadosFlat;
			List<OtrosCostosFondo> costosAlmacenamiento;

			// Buscar valores antiguos
			List<EstadoConciliacionParametrosLiquidacion> oldList = estadoConciliacionParametrosLiquidacionService
					.buscarLiquidacion(idLiquidacion, 2);
			try {
				if (Objects.nonNull(oldList) && !oldList.isEmpty()) {
					EstadoConciliacionParametrosLiquidacion old = oldList.get(0);

					parametroLiquidacion = objectMapper.readValue(old.getDatosParametrosLiquidacionCostos(),
							ParametrosLiquidacionCosto.class);
					valoresLiquidadosFlat = objectMapper.readValue(old.getDatosValoresLiquidadosProc(),
							ValoresLiquidadosFlatEntity.class);
					costosAlmacenamiento = objectMapper.readValue(old.getDatosOtrosCostosFondo(),
							new TypeReference<List<OtrosCostosFondo>>() {
							});
					var valoresLiquidados = valoresLiquidadosFlatService.consultarPorIdLiquidacion(idLiquidacion);

					if (Objects.nonNull(parametroLiquidacion)) {
						parametrosLiquidacionCostosService
								.f2actualizarParametrosLiquidacionCostos(parametroLiquidacion);
					}

					if (Objects.nonNull(valoresLiquidadosFlat)) {
						
						if (valoresLiquidados != null) {
							valoresLiquidadosFlat.setCostoCharterFlat(valoresLiquidados.getCostoCharterFlat());
							valoresLiquidadosFlat.setCostoEmisarioFlat(valoresLiquidados.getCostoEmisarioFlat());
							valoresLiquidadosFlat.setCostoFijoParadaFlat(valoresLiquidados.getCostoFijoParadaFlat());
							valoresLiquidadosFlat.setCostoMonedaFlat(valoresLiquidados.getCostoMonedaFlat());
							valoresLiquidadosFlat.setMilajePorRuteoFlat(valoresLiquidados.getMilajePorRuteoFlat());
							valoresLiquidadosFlat.setMilajeVerificacionFlat(valoresLiquidados.getMilajeVerificacionFlat());
							valoresLiquidadosFlat.setTasaAeroportuariaFlat(valoresLiquidados.getTasaAeroportuariaFlat());
							valoresLiquidadosFlat.setClasificacionMonedaFlat(valoresLiquidados.getClasificacionMonedaFlat());
						}

						valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosFlat);
					}

					if (Objects.nonNull(costosAlmacenamiento)) {
						for (OtrosCostosFondo item : costosAlmacenamiento) {
							otrosCostosFondo.save(item);
						}
					}

					continuar = true;
				}

			} catch (JsonProcessingException e) {
				continuar = false;
			}

		}

		return continuar;

	}
	
	private Long aceptarParametroLiquidacionCosto(CostosProcesamiento costo, Long consecutivoVista)
	{
	
		Long idLiquidacion = 0l;
		int consecutivo = Math.toIntExact(consecutivoVista);
		OperacionesLiquidacionProcesamientoEntity vwCosto = operacionesLiquidacion.consultarConsecutivoRegistroProc(consecutivo)
                .orElse(null);
		
		if (vwCosto != null) {
			costo.setEstadoConciliacion(vwCosto.getState());
        }
	
		if (costo.getEstadoConciliacion().equals(Dominios.ESTADO_VALIDACION_EN_CONCILIACION))
		{
			var parametro = new ParametrosLiquidacionCosto();
			var valoresLiquidadosFlatEntity = new ValoresLiquidadosFlatEntity();
			List<OtrosCostosFondo> costosAlmacenamiento;
			
			var banco = bancoService.findBancoByAbreviatura(costo.getEntidad());
			
			parametro.setCodigoBanco(banco.getCodigoPunto());
			parametro.setFechaEjecucion(costo.getFechaServicioTransporte());
			parametro.setCodigoTdv(costo.getCodigoTdv());
			parametro.setNombreCliente(costo.getRazonSocial());
			parametro.setCodigoPropioTdv(costo.getCodigoPuntoCargo()+ costo.getNombrePuntoCargo());
			
			parametro.setTipoOperacion(costo.getTipoOperacion());
			
					
			parametro.setValorBilletes(vwCosto.getProcessedBillnameTdv().doubleValue());
			parametro.setValorMonedas(vwCosto.getProcessedCoinnameTdv().doubleValue());
			parametro.setValorTotal(vwCosto.getTotalProcessednameTdv().doubleValue());
			parametro.setEntradaSalida(costo.getEntradaSalida());
			
			if (costo.getTipoOperacion().equals("RECOLECCION") || costo.getTipoOperacion().equals("RETIRO")) {
			    parametro.setPuntoOrigen(costo.getCodigoPuntoInterno());
			    parametro.setPuntoDestino(costo.getCodigoPuntoFondo());
			} else {
			    parametro.setPuntoDestino(costo.getCodigoPuntoInterno());
			    parametro.setPuntoOrigen(costo.getCodigoPuntoFondo());
			}
						
			valoresLiquidadosFlatEntity.setClasificacionFajadoFlat(vwCosto.getClassificationBundledTdv().doubleValue());
			valoresLiquidadosFlatEntity.setClasificacionNoFajadoFlat(vwCosto.getClassificationUnbundledTdv().doubleValue());
			valoresLiquidadosFlatEntity.setCostoPaqueteoFlat(vwCosto.getPackagingCostTdv().doubleValue());
			valoresLiquidadosFlatEntity.setModenaResiduoFlat(vwCosto.getCoinResidueTdv().doubleValue());
			valoresLiquidadosFlatEntity.setBilleteResiduoFlat(vwCosto.getBillResidueTdv().doubleValue());
			
			parametro = parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametro);
			idLiquidacion= parametro.getIdLiquidacion();
			
			valoresLiquidadosFlatEntity.setIdLiquidacionFlat(idLiquidacion);
			valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosFlatEntity);
			
			actualizarCrearCostosAlmacenamiento(costo, vwCosto);
		}
		
		return idLiquidacion;
		
	}
	
	private Long guardarCostoRechazado(Long id, String observacion)
	{
		var timestamp = new Timestamp(System.currentTimeMillis());
		var costo = new CostosProcesamiento();
		var parametroLiquidacion = 
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(id).orElse(null);

		if (Objects.nonNull(parametroLiquidacion))
		{
			var banco = bancoService.findBancoByCodigoPunto(parametroLiquidacion.getCodigoBanco());
			BigDecimal subTotal = BigDecimal.ZERO;
			costo.setEntidad(banco.getAbreviatura());
			costo.setFactura("PROCESAMIENTO CLIENTES");
			costo.setTipoRegistro("PROCESAMIENTO");
			costo.setFechaServicioTransporte(parametroLiquidacion.getFechaEjecucion());
			costo.setFechaProcesamiento(timestamp);
			costo.setIdentificacionCliente("0");
			costo.setRazonSocial(parametroLiquidacion.getNombreCliente() != null
					&& !parametroLiquidacion.getNombreCliente().isEmpty() ? parametroLiquidacion.getNombreCliente()
							: "SIN ESPECIFICAR");
			costo.setCodigoPuntoCargo("0");
			costo.setNombrePuntoCargo(parametroLiquidacion.getCodigoPropioTdv());
			costo.setCodigoCiiuFondo(0);
			costo.setCiudadFondo("0");
			costo.setNombreTipoServicio(parametroLiquidacion.getTipoOperacion());
			costo.setMonedaDivisa("COP");
			costo.setTrmConversion(BigDecimal.ZERO);
			costo.setValorProcesadoBillete(UtilsParsing.doubleToDecimal(parametroLiquidacion.getValorBilletes()));
			costo.setValorProcesadoMoneda(UtilsParsing.doubleToDecimal(parametroLiquidacion.getValorMonedas()));
			costo.setValorTotalProcesado(UtilsParsing.doubleToDecimal(parametroLiquidacion.getValorTotal()));
			costo.setFactorLiquidacion("FAJOS");
			costo.setBaseLiquidacion(BigDecimal.ZERO);
			costo.setTarifa(BigDecimal.ZERO);
			costo.setCostoSubtotal(
				    UtilsParsing.doubleToDecimal(parametroLiquidacion.getValorBilletes())
				        .add(UtilsParsing.doubleToDecimal(parametroLiquidacion.getValorMonedas()))
				        .add(UtilsParsing.doubleToDecimal(parametroLiquidacion.getValorTotal()))
				);
										
			costo.setPorcentajeAiu(0);
			costo.setPorcentajeIva(0);
			costo.setIva(BigDecimal.ZERO);
			costo.setValorTotal(subTotal);				
			costo.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_RECHAZADA);
			costo.setEstado(Constantes.REGISTRO_ACTIVO);
			costo.setObservacionesAth(observacion);
			costo.setIdArchivoCargado(0l);
			costo.setIdRegistro(id);
			costo.setUsuarioCreacion(Constantes.USUARIO_PROCESA_ARCHIVO);
			costo.setFechaCreacion(timestamp);
			costo.setUsuarioModificacion(null);
			costo.setFechaModificacion(null);
			costo.setIdLiquidacion(id);
			costo.setTipoTransaccion(3);
			
			costosProcesamientoRepository.save(costo);
			
		}
		
		return id;
	
	}
	
	@Override
	public List<RegistroAceptarRechazarDTO> identificadasConDiferenciaAceptarRechazar(RegistrosAceptarRechazarListDTO registros) {
		
		List<RegistroAceptarRechazarDTO> aceptadosRechazados = registros.getRegistroOperacion();
		var timestamp = new Timestamp(System.currentTimeMillis());
		
		aceptadosRechazados.forEach(f->{
			var continuar = false;
			Long id = f.getIdRegistro();
			
			List<IDetalleLiquidacionProcesamiento> detalles = obtenerDetalleLiquidacionProcesamiento(
					Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, id);
			
			String operacion = f.getOperacionEstado();
			String observacionATH = f.getObservacion();
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			List<CostosProcesamiento> costoProcesamientoList = obtenerCostoProcesamientoList(
					Constantes.OPERACIONES_LIQUIDACION_IDENTIFICADAS_CON_DIFERENCIAS, id);
			
			CostosProcesamiento costoProc = calcularDiferenciasCostosProcesamiento(detalles, costoProcesamientoList.get(0));
			
			if (costoProcesamientoList != null && !costoProcesamientoList.isEmpty()) {
								
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR)) {

					Long idLiquidacion = costosTransporteService.obtenerIdPorTipo(detalles,
							Constantes.LISTA_CONCILIACION_IDLIQUIDACIONAPP);

					aceptarCostoProcesamientoIdentificadasConDiferencia(costoProc, idLiquidacion);

					if (idLiquidacion.compareTo(0l) > 0) {
						continuar = true;
						for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
							costoProcesamiento.setIdLiquidacion(idLiquidacion);
							costoProcesamiento.setTipoTransaccion(2);
							costoProcesamiento.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_MANUAL);
						}
					}
				}
				
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR)) {
					continuar = true;
					for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
						costoProcesamiento.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_RECHAZADA);
					}
				}
				
			    if (continuar) {
			    	
			    	for (CostosProcesamiento costoProcesamiento : costoProcesamientoList) {
			    		costoProcesamiento.setObservacionesAth(observacionATH);
			    		costoProcesamiento.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
			    		costoProcesamiento.setFechaModificacion(timestamp);
			    	}
					costosProcesamientoRepository.saveAll(costoProcesamientoList);
					f.setOperacionEstado("APLICADO");
			    }
			}
		});
		return aceptadosRechazados;
	}
	
	private void aceptarCostoProcesamientoIdentificadasConDiferencia(CostosProcesamiento costo, Long idLiquidacion) {
		
		var parametroLiquidacion = parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(idLiquidacion)
				.orElse(null);
		
		var valoresLiquidadosParametro = valoresLiquidadosFlatService
				.getParametrosValoresLiquidadosByIdLiquidacion(idLiquidacion);

		var costosAlmacenamiento = otrosCostosFondo.consultarPorFechaSaldoYCodigoPuntoFondo(costo.getCodigoPuntoFondo(),
				costo.getFechaProcesamiento());

		if (Objects.nonNull(parametroLiquidacion)) {
			salvarValoresLiquidadosLiquidacionCostos(parametroLiquidacion, valoresLiquidadosParametro,
					costosAlmacenamiento);

			parametroLiquidacion.setValorBilletes(costo.getValorProcesadoBillete().doubleValue());
			parametroLiquidacion.setValorMonedas(costo.getValorProcesadoMoneda().doubleValue());
			parametroLiquidacion.setValorTotal(costo.getValorTotalProcesado().doubleValue());

			parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiquidacion);
			
			valoresLiquidadosParametro
					.setClasificacionFajadoFlat(costosTransporteService.aplicarAjuste(costo.getClasificacionFajado(),
							valoresLiquidadosParametro.getClasificacionFajadoFlat(), Double.class));

			valoresLiquidadosParametro.setClasificacionNoFajadoFlat(
					costosTransporteService.aplicarAjuste(costo.getClasificacionNoFajado(),
							valoresLiquidadosParametro.getClasificacionNoFajadoFlat(), Double.class));
			
			valoresLiquidadosParametro.setCostoPaqueteoFlat(costosTransporteService.aplicarAjuste(
					costo.getCostoPaqueteo(), valoresLiquidadosParametro.getCostoPaqueteoFlat(), Double.class));
			
			valoresLiquidadosParametro.setModenaResiduoFlat(costosTransporteService.aplicarAjuste(
					costo.getMonedaResiduo(), valoresLiquidadosParametro.getModenaResiduoFlat(), Double.class));
			
			valoresLiquidadosParametro.setBilleteResiduoFlat(costosTransporteService.aplicarAjuste(
					costo.getBilleteResiduo(), valoresLiquidadosParametro.getBilleteResiduoFlat(), Double.class));
			
			valoresLiquidadosFlatService.f2actualizarvaloresLiquidadosRepository(valoresLiquidadosParametro);
			
			
			for (OtrosCostosFondo item : costosAlmacenamiento) {
				
				String concepto = item.getId().getConcepto() != null ? item.getId().getConcepto().toUpperCase() : "";

				if (concepto.contains("ALMACENAMIENTO BILLETE")) {
					if (costo.getValorAlmacenamientoBillete() != null) {
						item.setValorLiquidado(costosTransporteService.aplicarAjuste(
								costo.getValorAlmacenamientoBillete().doubleValue(), item.getValorLiquidado(),
								Double.class));
					}
				} else if (concepto.contains("ALMACENAMIENTO MONEDA")) {
					if (costo.getValorAlmacenamientoBillete() != null) {
						item.setValorLiquidado(costosTransporteService.aplicarAjuste(
								costo.getValorAlmacenamientoMoneda().doubleValue(), item.getValorLiquidado(),
								Double.class));
					}
				}
				
				otrosCostosFondo.save(item);
			}

		}
	}

	private void salvarValoresLiquidadosLiquidacionCostos(ParametrosLiquidacionCosto parametroLiquidacionCosto,
			ValoresLiquidadosFlatEntity valoresLiquidadosFlagEntity, List<OtrosCostosFondo> otrosCostosFondo) {

		try {
			var objectMapper = new ObjectMapper();
			var imgParametroLiquidacionCostos = objectMapper.writeValueAsString(parametroLiquidacionCosto);
			var imgValoresLiquidadosProc = objectMapper.writeValueAsString(valoresLiquidadosFlagEntity);
			var imgCostosAlmacenamiento = objectMapper.writeValueAsString(otrosCostosFondo);

			var estadoConciliacion = new EstadoConciliacionParametrosLiquidacion();
			
			// Consulta si existe una entidad previamente guardada
			var existeEstadoLiquidacion = estadoConciliacionParametrosLiquidacionService
			                    .buscarLiquidacion(parametroLiquidacionCosto.getIdLiquidacion(), 2);

			estadoConciliacion.setIdLiquidacion(parametroLiquidacionCosto.getIdLiquidacion());
			estadoConciliacion.setDatosParametrosLiquidacionCostos(imgParametroLiquidacionCostos);
			estadoConciliacion.setEstado(2);
			
			// Si existe al menos un registro previo, usamos el valor existente
			if (existeEstadoLiquidacion != null && !existeEstadoLiquidacion.isEmpty() && existeEstadoLiquidacion.get(0).getDatosValoresLiquidados() != null) {
			    estadoConciliacion.setDatosValoresLiquidados(existeEstadoLiquidacion.get(0).getDatosValoresLiquidados());
			} else {
			    estadoConciliacion.setDatosValoresLiquidados("");
			}
			
			estadoConciliacion.setDatosValoresLiquidadosProc(imgValoresLiquidadosProc);
			estadoConciliacion.setDatosOtrosCostosFondo(imgCostosAlmacenamiento);

			estadoConciliacionParametrosLiquidacionService.save(estadoConciliacion);

		} catch (Exception e) {
			// No se manjea la excepcion
		}

	}

	/**
	 * Metodo encargado de reintegrar los registros eliminados de costos_procesamiento
	 * 
	 * @param registros
	 * @return List<RegistroOperacionConciliacionDTO>
	 * @author jose.pabon
	 */	
	@Override
	public List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasTransporte(RegistrosConciliacionListDTO registros) {
		return costosTransporteService.reintegrarLiquidadasTransporte(registros);
	}
	
	@Override
	public List<IDetalleLiquidacionProcesamiento> obtenerDetalleLiquidacionProcesamiento(String modulo, Long idLlave) {
		
		List<IDetalleLiquidacionProcesamiento> resultados = operacionesLiquidacion
				.obtenerDetallesPorModuloProcesamiento(modulo, idLlave);
		return resultados;
	}
	
	@Override
	public List<IDetalleLiquidacionProcesamiento> obtenerDetalleProcesamientoPorIdArchivo(Integer idArchivo) {

		List<IDetalleLiquidacionProcesamiento> resultados = operacionesLiquidacion
				.obtenerDetallesPorIdArchivoProcesamiento(idArchivo);
		return resultados;
	}
	
	@Override
	public List<IDetalleLiquidacionProcesamiento> obtenerEstadoProcesamientoPorLlave(BigInteger idLlave) {

		List<IDetalleLiquidacionProcesamiento> resultados = operacionesLiquidacion
				.countConciliadasProcesamientoByLlave(idLlave);
		return resultados;
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
	public List<RegistroOperacionConciliacionDTO> reintegrarLiquidadasProcesamiento(RegistrosConciliacionListDTO registros) {
		
		List<RegistroOperacionConciliacionDTO> registrosReintegrados = registros.getRegistroOperacion();
		
		registrosReintegrados.forEach(f->{
			boolean continuar = false;
			Long id = f.getIdRegistro();
			String operacion = f.getOperacionEstado();
			
			f.setOperacionEstado("NO PUDO REALIZAR LA OPERACION");
			
			if (operacion.equalsIgnoreCase("REINTEGRAR"))
			{
				List<IDetalleLiquidacionProcesamiento> detalles = obtenerDetalleLiquidacionProcesamiento(
						Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS_ELIMINADAS, id);
				
				List<Long> idLiquidacionList = UtilsParsing.parseStringToList(detalles.get(0).getIdsLiquidacionApp());
				
				for (Long idLiq : idLiquidacionList) {
	                continuar = costosTransporteService.reintegrarRegistrosLiquidados(idLiq, continuar, Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO);
	                if (!continuar) break;
	            }
			}
			if (continuar) {
				f.setOperacionEstado("REINTEGRADA");
			}
		});
		return registrosReintegrados;
	}

	@Override
	public List<CostosProcesamiento> obtenerCostoProcesamientoList(String operacion, Long idRegistro) {
	   
		List<IDetalleLiquidacionProcesamiento> detalles = obtenerDetalleLiquidacionProcesamiento(operacion, idRegistro);
	    
		if (detalles.isEmpty()) {
	        return new ArrayList<>();
	    }

	    List<Long> listaConsecutivos = UtilsParsing.parseStringToList(detalles.get(0).getConsecutivoRegistro());
	    List<CostosProcesamiento> costoProcesamientoList = new ArrayList<>();

	    for (Long idReg : listaConsecutivos) {
	        CostosProcesamiento costoProcesamiento = costosProcesamientoRepository.findById(idReg).orElse(null);
	        if (costoProcesamiento != null) {
	        	costoProcesamientoList.add(costoProcesamiento);
	        }
	    }

	    return costoProcesamientoList;
	}
	
	@Override
	public CostosProcesamiento calcularDiferenciasCostosProcesamiento(List<IDetalleLiquidacionProcesamiento> detalles,
	                                                                   CostosProcesamiento costoProcesamiento) {
	    
	    CostosProcesamiento resultado = new CostosProcesamiento();
	    BeanUtils.copyProperties(costoProcesamiento, resultado);
	    
	    Double clasificacionFajado = 0.0;
	    Double clasificacionNoFajado = 0.0;
	    Double costoPaqueteo = 0.0;
	    Double monedaResiduo = 0.0;
	    Double billeteResiduo = 0.0;
	    BigDecimal valorAlmacenamientoBillete = BigDecimal.ZERO;
	    BigDecimal valorAlmacenamientoMoneda = BigDecimal.ZERO;

	    for (IDetalleLiquidacionProcesamiento d : detalles) {
	        if (d.getClasificacionFajado() != null && d.getClasificacionFajadoTdv() != null) {
	            clasificacionFajado += d.getClasificacionFajadoTdv() - d.getClasificacionFajado();
	        }

	        if (d.getClasificacionNoFajado() != null && d.getClasificacionNoFajadoTdv() != null) {
	            clasificacionNoFajado += d.getClasificacionNoFajadoTdv() - d.getClasificacionNoFajado();
	        }

	        if (d.getCostoPaqueteo() != null && d.getCostoPaqueteoTdv() != null) {
	            costoPaqueteo += d.getCostoPaqueteoTdv() - d.getCostoPaqueteo();
	        }

	        if (d.getMonedaResiduo() != null && d.getMonedaResiduoTdv() != null) {
	            monedaResiduo += d.getMonedaResiduoTdv() - d.getMonedaResiduo();
	        }

	        if (d.getBilleteResiduo() != null && d.getBilleteResiduoTdv() != null) {
	            billeteResiduo += d.getBilleteResiduoTdv() - d.getBilleteResiduo();
	        }

	        if (d.getValorAlmacenamientoBillete() != null && d.getValorAlmacenamientoBilleteTdv() != null) {
	            valorAlmacenamientoBillete = valorAlmacenamientoBillete.add(
	                d.getValorAlmacenamientoBilleteTdv().subtract(d.getValorAlmacenamientoBillete())
	            );
	        }

	        if (d.getValorAlmacenamientoMoneda() != null && d.getValorAlmacenamientoMonedaTdv() != null) {
	            valorAlmacenamientoMoneda = valorAlmacenamientoMoneda.add(
	                d.getValorAlmacenamientoMonedaTdv().subtract(d.getValorAlmacenamientoMoneda())
	            );
	        }
	    }

	    resultado.setClasificacionFajado(clasificacionFajado);
	    resultado.setClasificacionNoFajado(clasificacionNoFajado);
	    resultado.setCostoPaqueteo(costoPaqueteo);
	    resultado.setMonedaResiduo(monedaResiduo);
	    resultado.setBilleteResiduo(billeteResiduo);
	    resultado.setValorAlmacenamientoBillete(valorAlmacenamientoBillete);
	    resultado.setValorAlmacenamientoMoneda(valorAlmacenamientoMoneda);

	    return resultado;
	}
	
	public void actualizarCrearCostosAlmacenamiento(CostosProcesamiento costo, OperacionesLiquidacionProcesamientoEntity vwCosto) {
		List<OtrosCostosFondo> costosAlmacenamiento = otrosCostosFondo
				.consultarPorFechaSaldoYCodigoPuntoFondo(costo.getCodigoPuntoFondo(), costo.getFechaProcesamiento());

		if (costosAlmacenamiento != null && !costosAlmacenamiento.isEmpty()) {
			for (OtrosCostosFondo item : costosAlmacenamiento) {
				String concepto = item.getId().getConcepto() != null ? item.getId().getConcepto().toUpperCase() : "";

				if (concepto.contains("ALMACENAMIENTO BILLETE") && vwCosto.getStorageValueBillTdv() != null) {
					item.setValorLiquidado(vwCosto.getStorageValueBillTdv().doubleValue());
				} else if (concepto.contains("ALMACENAMIENTO MONEDA") && vwCosto.getStorageValueCoinTdv() != null) {
					item.setValorLiquidado(vwCosto.getStorageValueCoinTdv().doubleValue());
				}

				otrosCostosFondo.save(item);
			}
			
		} else {
			
			if (costo.getValorAlmacenamientoBillete() != null) {
				OtrosCostosFondoPK idBillete = new OtrosCostosFondoPK();
				idBillete.setCodigoPuntoFondo(costo.getCodigoPuntoFondo());
				idBillete.setFechaSaldo(costo.getFechaProcesamiento());
				idBillete.setConcepto("ALMACENAMIENTO BILLETE");

				OtrosCostosFondo nuevoBillete = new OtrosCostosFondo();
				nuevoBillete.setId(idBillete);
				nuevoBillete.setFechaCreacion(new Date());
				nuevoBillete.setFechaLiquidacion(costo.getFechaProcesamiento());
				nuevoBillete.setIdSeqGrupo(0);
				nuevoBillete.setValorLiquidado(vwCosto.getStorageValueBillTdv().doubleValue());
				otrosCostosFondo.save(nuevoBillete);
			}

			if (costo.getValorAlmacenamientoMoneda() != null) {
				OtrosCostosFondoPK idMoneda = new OtrosCostosFondoPK();
				idMoneda.setCodigoPuntoFondo(costo.getCodigoPuntoFondo());
				idMoneda.setFechaSaldo(costo.getFechaProcesamiento());
				idMoneda.setConcepto("ALMACENAMIENTO MONEDA");

				OtrosCostosFondo nuevoMoneda = new OtrosCostosFondo();
				nuevoMoneda.setId(idMoneda);
				nuevoMoneda.setFechaCreacion(new Date());
				nuevoMoneda.setFechaLiquidacion(costo.getFechaProcesamiento());
				nuevoMoneda.setIdSeqGrupo(0);
				nuevoMoneda.setValorLiquidado(vwCosto.getStorageValueCoinTdv().doubleValue());
				otrosCostosFondo.save(nuevoMoneda);
			}
		}
	}
}
