package com.ath.adminefectivo.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosFiltroCostoProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesLiquidacionProcesamientoDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosAceptarRechazarListDTO;
import com.ath.adminefectivo.dto.compuestos.RegistrosConciliacionListDTO;
import com.ath.adminefectivo.entities.CostosProcesamiento;
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;
import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesProcesamientoRepository;
import com.ath.adminefectivo.repositories.ICostosProcesamientoRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IConciliacionOperacionesProcesamientoService;
import com.ath.adminefectivo.service.IEstadoConciliacionParametrosLiquidacionService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IValoresLiquidadosFlatService;
import com.ath.adminefectivo.utils.UtilsParsing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionConciliadaProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
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
				Constantes.OPERACIONES_LIQUIDACION_CONCILIADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidacionRemitidasNoIdentificadasProcesamiento(
			ParametrosFiltroCostoProcesamientoDTO filtros) {
		
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
				Constantes.OPERACIONES_LIQUIDACION_REMITIDAS_NO_IDENTIFICADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getLiquidadasNoCobradasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
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
				Constantes.OPERACIONES_LIQUIDACION_LIQUIDADAS_NO_COBRADAS, 
				filtros.getPage());

		return liquidacion(consulta, filtros.getPage());
	}

	@Override
	public Page<OperacionesLiquidacionProcesamientoDTO> getIdentificadasConDiferenciasProcesamiento(ParametrosFiltroCostoProcesamientoDTO filtros) {
		
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
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			CostosProcesamiento costo = costosProcesamientoRepository.findById(id).orElse(null);
			
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
						
					costosProcesamientoRepository.save(costo);
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
			CostosProcesamiento costo = costosProcesamientoRepository.findById(id).orElse(null);
			
			if (Objects.nonNull(costo)) {
								
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR))
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
				
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuar = true;
					costo.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_RECHAZADA);
				}
				
			    if (continuar) {
					//actualizar estado de registro
			    	costo.setObservacionesAth(observacionATH);
					costo.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
					costo.setFechaModificacion(timestamp);
					costosProcesamientoRepository.save(costo);
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
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			if (operacion.equalsIgnoreCase("ELIMINAR"))
			{
				costosTransporteService.eliminarParametroLiquidacionCosto(id);
				f.setOperacionEstado("ELIMINADA");
			}
				
			if (operacion.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
			{
				guardarCostoRechazado(id, observacionATH);
				f.setOperacionEstado("RECHAZADA");
				
			}
				
			
		});
		
		return eliminadosRechazados;
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
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(idLiquidacion).orElse(null);
			
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
	
	private Long aceptarParametroLiquidacionCosto(CostosProcesamiento costo)
	{
	
		Long idLiquidacion = 0l;
		int consecutivo = Math.toIntExact(costo.getConsecutivo());
		OperacionesLiquidacionProcesamientoEntity vwCosto = operacionesLiquidacion.findByRecordConsecutive(consecutivo)
                .orElse(null);
		
		if (vwCosto != null) {
			costo.setEstadoConciliacion(vwCosto.getState());
        }
	
		if (costo.getEstadoConciliacion().equals(Dominios.ESTADO_VALIDACION_EN_CONCILIACION))
		{
			var parametro = new ParametrosLiquidacionCosto();
		
			var banco = bancoService.findBancoByAbreviatura(costo.getEntidad());
			
			parametro.setCodigoBanco(banco.getCodigoPunto());
			parametro.setFechaEjecucion(costo.getFechaServicioTransporte());
			parametro.setNombreCliente(costo.getRazonSocial());
			parametro.setCodigoPropioTdv(costo.getNombrePuntoCargo());
			
			parametro.setTipoOperacion(costo.getNombreTipoServicio());
			
					
			parametro.setValorBilletes(costo.getValorProcesadoBillete().doubleValue());
			parametro.setValorMonedas(costo.getValorProcesadoMoneda().doubleValue());
			parametro.setValorTotal(costo.getValorTotalProcesado().doubleValue());
			
			parametro = parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametro);
			idLiquidacion= parametro.getIdLiquidacion();
			
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
			String operacion = f.getOperacionEstado();
			String observacionATH = f.getObservacion();
			
			f.setOperacionEstado(Constantes.ESTADO_OPERACION_NO_REALIZADA);
			
			//Obtener el estado del registro de la base de datos
			CostosProcesamiento costo = costosProcesamientoRepository.findById(id).orElse(null);
			
			if (Objects.nonNull(costo)) {
								
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_ACEPTAR))
				{
					Long idLiquidacion = f.getIdLiquidacion();

					aceptarCostoProcesamientoIdentificadasConDiferencia(costo, idLiquidacion);

					if (idLiquidacion.compareTo(0l)>0)
					{
						continuar = true;
						costo.setIdLiquidacion(idLiquidacion);
						costo.setTipoTransaccion(2);
						costo.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_MANUAL);
					}
				}
				
				if (operacion.equalsIgnoreCase(Constantes.OPERACION_RECHAZAR))
				{
					continuar = true;
					costo.setEstadoConciliacion(Constantes.ESTADO_CONCILIACION_RECHAZADA);
				}
				
			    if (continuar) {
			    	costo.setObservacionesAth(observacionATH);
					costo.setUsuarioModificacion(Constantes.USUARIO_PROCESA_ARCHIVO);
					costo.setFechaModificacion(timestamp);
					costosProcesamientoRepository.save(costo);
					f.setOperacionEstado("APLICADO");
			    }
			}
		});
		return aceptadosRechazados;
	}
	
	private void aceptarCostoProcesamientoIdentificadasConDiferencia(CostosProcesamiento costo, Long idLiquidacion)
	{
		var parametroLiquidacion = 
				parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(idLiquidacion).orElse(null);
	
		if (Objects.nonNull(parametroLiquidacion)) 
		{
			salvarValoresLiquidadosLiquidacionCostos(parametroLiquidacion);

			parametroLiquidacion.setValorBilletes(costo.getValorProcesadoBillete().doubleValue());
			parametroLiquidacion.setValorMonedas(costo.getValorProcesadoMoneda().doubleValue());
			parametroLiquidacion.setValorTotal(costo.getValorTotalProcesado().doubleValue());
			
			parametrosLiquidacionCostosService.f2actualizarParametrosLiquidacionCostos(parametroLiquidacion);
			
		}
	}

	private void salvarValoresLiquidadosLiquidacionCostos(ParametrosLiquidacionCosto parametroLiquidacionCosto){
			
			try {
				var objectMapper = new ObjectMapper();
				var imgParametroLiquidacionCostos = objectMapper.writeValueAsString(parametroLiquidacionCosto);

				var estadoConciliacion = new EstadoConciliacionParametrosLiquidacion();
		
				estadoConciliacion.setIdLiquidacion(parametroLiquidacionCosto.getIdLiquidacion());
				estadoConciliacion.setDatosParametrosLiquidacionCostos(imgParametroLiquidacionCostos);
				estadoConciliacion.setEstado(2);
				estadoConciliacion.setDatosValoresLiquidados("");

				estadoConciliacionParametrosLiquidacionService.save(estadoConciliacion); 

			} catch (Exception e) {
				// No se manjea la excepcion
			}

	}
	
}
