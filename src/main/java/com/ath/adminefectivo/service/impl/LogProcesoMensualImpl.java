package com.ath.adminefectivo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.LogProcesoMensualDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.LogProcesoMensual;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.repositories.LogProcesoMensualRepository;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.ILogProcesoMensualService;
import com.ath.adminefectivo.service.IParametroService;
import com.querydsl.core.types.Predicate;

/**
 * Servicios responsables de exponer los metodos referentes a los procesos 
 * diarios en ejecucion
 * @author duvan.naranjo
 */

@Service
public class LogProcesoMensualImpl implements ILogProcesoMensualService {

	@Autowired
	LogProcesoMensualRepository logProcesoMensualRepository;

	@Autowired
	IParametroService parametroService;
	
	@Autowired
	IFestivosNacionalesService festivosNacionalesService;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LogProcesoMensualDTO> getLogsProcesosMensual(Predicate predicate) {
		var logProcesoDiarios = logProcesoMensualRepository.findAll(predicate);
		
		List<LogProcesoMensualDTO> listLogProcesoMensualDto = new ArrayList<>();
		
		logProcesoDiarios.forEach(entity -> {
			System.out.println(entity.getIdLog());
			listLogProcesoMensualDto.add(LogProcesoMensualDTO.CONVERTER_DTO.apply(entity));
		});
		return listLogProcesoMensualDto;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogProcesoMensualDTO getLogsProcesosMensualByCodigoProcesoAndPendiente(String codigoProceso){
		var procesos = logProcesoMensualRepository.findByCodigoProcesoAndEstado(codigoProceso, Dominios.ESTADO_PROCESO_DIA_PENDIENTE);
		
		if(procesos.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_LOG_MENSUAL_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_LOG_MENSUAL_NO_EXISTE.getDescription()+codigoProceso+" con estado = "+ Dominios.ESTADO_PROCESO_DIA_PENDIENTE,
					ApiResponseCode.ERROR_LOG_MENSUAL_NO_EXISTE.getHttpStatus());
		}else if(procesos.size()>1) {
			throw new NegocioException(ApiResponseCode.ERROR_MAS_DE_UN_LOG_MENSUAL.getCode(),
					ApiResponseCode.ERROR_MAS_DE_UN_LOG_MENSUAL.getDescription()+codigoProceso+" con estado = "+ Dominios.ESTADO_PROCESO_DIA_PENDIENTE,
					ApiResponseCode.ERROR_MAS_DE_UN_LOG_MENSUAL.getHttpStatus());
		}else {
			return LogProcesoMensualDTO.CONVERTER_DTO.apply(procesos.get(0));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validarLogProceso(LogProcesoMensualDTO logProcesoMensual) {
		if(logProcesoMensual.getCodigoProceso().equals(Dominios.LOG_MENSUAL_LIQUIDACION)) {
			return this.validarLogProcesoMensual(logProcesoMensual);
		}
		return false;
	}

	private boolean validarLogProcesoMensual(LogProcesoMensualDTO logProcesoMensual) {
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		Date ultimoDiaHabil = festivosNacionalesService.consultarAnteriorHabil(logProcesoMensual.getMes());
		int anio = ultimoDiaHabil.getYear();
		int mes = ultimoDiaHabil.getMonth();
		if(fechaSistema.compareTo(ultimoDiaHabil) >= 0) {
			if(!logProcesoMensualRepository.existenLogParaMesAnioYEstado(Dominios.ESTADO_PROCESO_DIA_PENDIENTE, anio, mes)) {
				String mesAnio = mes+"-"+anio;
				if(logProcesoMensualRepository.existenCostosClasificacionPorMesAnio(mesAnio)){
					if(this.validarLiquidacionCostoDiaria(mes,anio, fechaSistema)) {
						logProcesoMensual.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
						LogProcesoMensualDTO logProcesoPorCrear = logProcesoMensual;
						
						logProcesoMensualRepository.save(LogProcesoMensualDTO.CONVERTER_ENTITY.apply(logProcesoMensual));
						
						logProcesoPorCrear.setIdLog(null);
						logProcesoPorCrear.setFechaCreacion(new Date());
						//logProcesoPorCrear.setFechaCierre(  Date.from(  LocalDate.now().with(    TemporalAdjusters.lastDayOfMonth() )  )  );
						System.out.println("El último día de este mes es: " + LocalDate.now().with( TemporalAdjusters.lastDayOfMonth() ));
						
					}else {
						//no existen liquidacion costos diaria para alguna fecha
						throw new NegocioException(ApiResponseCode.ERROR_LIQUIDACION_DIARIO_NO_EXISTE.getCode(),
								ApiResponseCode.ERROR_LIQUIDACION_DIARIO_NO_EXISTE.getDescription(),
								ApiResponseCode.ERROR_LIQUIDACION_DIARIO_NO_EXISTE.getHttpStatus());
					}
				}else {
					//no existen costos clasificacion para el mes tal y anio tal
					throw new NegocioException(ApiResponseCode.ERROR_COSTOS_CLASIFICACION_NO_EXISTE.getCode(),
							ApiResponseCode.ERROR_COSTOS_CLASIFICACION_NO_EXISTE.getDescription(),
							ApiResponseCode.ERROR_COSTOS_CLASIFICACION_NO_EXISTE.getHttpStatus());
				}
			}else {
				//mensaje de que aun no se han cerrado ciertos procesos del mes 
				throw new NegocioException(ApiResponseCode.ERROR_LOG_PROCESOS_NO_CERRADOS.getCode(),
						ApiResponseCode.ERROR_LOG_PROCESOS_NO_CERRADOS.getDescription()+ fechaSistema,
						ApiResponseCode.ERROR_LOG_PROCESOS_NO_CERRADOS.getHttpStatus());
			}
		}else {
			//mensaje que aun no se puede cerrar porque la fecha del sistema no es igual a la ultima fecha habil del mes
			throw new NegocioException(ApiResponseCode.ERROR_FECHA_NO_FINAL.getCode(),
					ApiResponseCode.ERROR_FECHA_NO_FINAL.getDescription()+ fechaSistema,
					ApiResponseCode.ERROR_FECHA_NO_FINAL.getHttpStatus());
		}
		
		return false;		
	}

	private boolean validarLiquidacionCostoDiaria(int mes, int anio, Date fechaSistema) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String fechaInicialMes = "01/"+mes+"/"+anio;
		try {
			Date fechaInicial = format.parse(fechaInicialMes);
			if(festivosNacionalesService.esFestivo(fechaInicial)) {
				fechaInicial = festivosNacionalesService.consultarSiguienteHabil(fechaInicial);
			}
			int dia = fechaInicial.getDay();
			String fecha;
			for (int i = dia; i < fechaSistema.getDay(); i++) {
				fecha = ""+i+"/"+mes+"/"+anio;
				if(!logProcesoMensualRepository.existenLiquidacionCostoDiariaByFecha(format.parse(fecha))) {
					return false;
				};
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


}
