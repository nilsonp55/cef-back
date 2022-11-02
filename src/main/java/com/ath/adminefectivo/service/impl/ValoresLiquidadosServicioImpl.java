package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IValoresLiquidadosRepository;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class ValoresLiquidadosServicioImpl implements IValoresLiquidadosService{

	@Autowired
	IValoresLiquidadosRepository valoresLiquidadosRepository;

	@Autowired
	ParametroServiceImpl parametroServiceImpl;

	@Autowired
	LogProcesoDiarioRepository logProcesoDiarioRepository;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean ActualizaCostosFletesCharter(costosCharterDTO costos) {
		Boolean estado = false;
		try {
			ValoresLiquidados valores = valoresLiquidadosRepository.findByIdLiquidacion(
					costos.getIdLiquidacion());
			if (Objects.isNull(valores)) {
				throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
			}else {
				valores.setIdLiquidacion(costos.getIdLiquidacion());
				valores.setCostoCharter(costos.getCostosCharter());
				valoresLiquidadosRepository.save(valores);
			}
			estado = true;
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
		}
		return estado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValoresLiquidadosDTO procesarPackageCostos() {
		//Se obtiene fecha sistema para validaciones
		Date fecha = parametroServiceImpl.valorParametroDate(Constantes.FECHA_DIA_PROCESO);

		//Se valida que la conciliacion este cerrada para poder ejecutar el package
		List<LogProcesoDiario> logProcesoDiarios = logProcesoDiarioRepository.findByFechaCreacion(fecha);

		var procesoDiarioConciliacionCerrad = false;
		var procesoDiarioLiquidacionPendiente = false;
		for(int i=0; i<logProcesoDiarios.size(); i++) {
			LogProcesoDiario item = logProcesoDiarios.get(i);
			if(item.getCodigoProceso().equals(Dominios.CODIGO_PROCESO_LOG_CONCILIACION)) {
				procesoDiarioConciliacionCerrad = 
						item.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO) ? true : false;
			}
			if(item.getCodigoProceso().equals(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION)) {
				procesoDiarioLiquidacionPendiente = 
						item.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_PENDIENTE) ? true : false;
			}
		};
		if(procesoDiarioConciliacionCerrad && procesoDiarioLiquidacionPendiente) {
			try {
				ValoresLiquidadosDTO valoresLiquidadosDTO = new ValoresLiquidadosDTO();
				//Se ejecutan procedimientos
				String parametro = valoresLiquidadosRepository.armar_parametros_liquida(fecha);
				if(Integer.parseInt(parametro) > 0) {
					String resultado= valoresLiquidadosRepository.liquidar_costos(Integer.parseInt(parametro));
					//Sacamos los dos cumero de la cadena de texto
					String cantidad1 = "";
					String cantidad2 = "";
					boolean separador = false;
					for(int i = 0; i<resultado.length(); i++) {
						char item =  resultado.charAt(i);
						if(item != '-' && separador == false) {
							cantidad1 += item;
						}
						if(item == '-') {
							separador = true;
						}
						if(item != '-' && separador == true) {
							cantidad2 += item;
						}
					}
					//Se obtienen los valores liquidados por el proceso
					List<ValoresLiquidados> valoresLiquidados = valoresLiquidadosRepository
							.findByIdSeqGrupo(Integer.parseInt(parametro));

					valoresLiquidadosDTO.setValoresLiquidados(valoresLiquidados);
					valoresLiquidadosDTO.setCantidadOperacionesLiquidadas(Integer.parseInt(cantidad1));
					valoresLiquidadosDTO.setRegistrosConError(Integer.parseInt(cantidad2));
				}
				else {
					throw new NegocioException(ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getCode(),
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getDescription(),
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getHttpStatus());
				}
				return valoresLiquidadosDTO;
			} catch (Exception e) {
				throw new NegocioException(ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getCode(),
						ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getDescription()+ "  "+e,
						ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getHttpStatus());
			}
		}
		else {
			throw new NegocioException(ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getCode(),
					ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getDescription(),
					ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getHttpStatus());
		}
	}
}
