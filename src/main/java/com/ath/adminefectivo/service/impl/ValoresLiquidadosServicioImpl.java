package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaLiquidarCostosDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IParametrosLiquidacionCostosRepository;
import com.ath.adminefectivo.repositories.IValoresLiquidadosRepository;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class ValoresLiquidadosServicioImpl implements IValoresLiquidadosService {

	@Autowired
	IValoresLiquidadosRepository valoresLiquidadosRepository;

	@Autowired
	ParametroServiceImpl parametroServiceImpl;

	@Autowired
	LogProcesoDiarioRepository logProcesoDiarioRepository;

	@Autowired
	IPuntosService puntosService;

	@Autowired
	IDominioService dominioService;

	@Autowired
	ITransportadorasService transportadorasService;

	@Autowired
	ICiudadesService ciudadesService;
	
	@Autowired
	IParametrosLiquidacionCostosService parametrosLiquidacionCostosService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean ActualizaCostosFletesCharter(costosCharterDTO costos) {
		Boolean estado = false;
		try {
			ValoresLiquidados valores = valoresLiquidadosRepository.consultarPorIdLiquidacion(costos.getIdLiquidacion());
			if (Objects.isNull(valores)) {
				throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
			} else {
				
				//valores.setIdLiquidacion(costos.getIdLiquidacion());
				valores.setParametrosLiquidacionCosto(parametrosLiquidacionCostosService.getParametrosLiquidacionCostosById(costos.getIdLiquidacion()));
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
	public String procesarPackageCostos() {
		// Se obtiene fecha sistema para validaciones
		Date fecha = parametroServiceImpl.valorParametroDate(Constantes.FECHA_DIA_PROCESO);

		// Se valida que la conciliacion este cerrada para poder ejecutar el package
		List<LogProcesoDiario> logProcesoDiarios = logProcesoDiarioRepository.findByFechaCreacion(fecha);

		var procesoDiarioConciliacionCerrad = false;
		var procesoDiarioLiquidacionPendiente = false;
		for (int i = 0; i < logProcesoDiarios.size(); i++) {
			LogProcesoDiario item = logProcesoDiarios.get(i);
			if (item.getCodigoProceso().equals(Dominios.CODIGO_PROCESO_LOG_CONCILIACION)) {
				procesoDiarioConciliacionCerrad = item.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)
						? true
						: false;
			}
			if (item.getCodigoProceso().equals(Dominios.CODIGO_PROCESO_LOG_LIQUIDACION)) {
				procesoDiarioLiquidacionPendiente = item.getEstadoProceso()
						.equals(Dominios.ESTADO_PROCESO_DIA_PENDIENTE) ? true : false;
			}
		}
		;
		if (procesoDiarioConciliacionCerrad && procesoDiarioLiquidacionPendiente) {
			try {
				
				

				// Se ejecutan procedimientos
				System.out.println("INICIA EL PROCESO DE LLAMADO ");
				String parametro = valoresLiquidadosRepository.armar_parametros_liquida(fecha);
				System.out.println("CONSULTA DE PARAMETRO A armar_parametros_liquida(fecha) = "+parametro);
				if (Integer.parseInt(parametro) > 0) {
					String resultado = valoresLiquidadosRepository.liquidar_costos(Integer.parseInt(parametro));
					System.out.println("CONSULTA DE PARAMETRO A liquidar_costos(Integer.parseInt(parametro)) = "+resultado);

				} else {
					throw new NegocioException(
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getCode(),
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getDescription(),
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getHttpStatus());
				}
				return "Se proceso con exito";
			} catch (Exception e) {
				throw new NegocioException(ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getCode(),
						ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getDescription() + "  " + e,
						ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS.getHttpStatus());
			}
		} else {
			throw new NegocioException(
					ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getCode(),
					ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getDescription(),
					ApiResponseCode.ERROR_PROCESO_VALIDACION_CIERRE_CONSTO_VALORES_LIQUIDADOS.getHttpStatus());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ValoresLiquidadosDTO consultarLiquidacionCostos() {
			
		int idSeq = 0;

		Date fechaActual = parametroServiceImpl.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		List<ParametrosLiquidacionCostoDTO> respuestaLiquidarCostosDTO = this.generarRespuestaLiquidacionCostos(fechaActual);
		ValoresLiquidadosDTO valoresLiquidadosDTO = new ValoresLiquidadosDTO();
		// Sacamos los dos cumero de la cadena de texto
		
		
		Integer cantidad1 = 0;
		Integer cantidad2 = 0;
		if(!respuestaLiquidarCostosDTO.isEmpty()) {
			idSeq = respuestaLiquidarCostosDTO.get(0).getSeqGrupo();
			cantidad1 = valoresLiquidadosRepository.consultarCantidadValoresLiquidadosByIdSeqGrupo(idSeq);
			cantidad2 = valoresLiquidadosRepository.consultarCantidadErroresValoresLiquidadosByIdSeqGrupo(idSeq);
		}
			
		valoresLiquidadosDTO.setRespuestaLiquidarCostos(respuestaLiquidarCostosDTO);
		valoresLiquidadosDTO.setCantidadOperacionesLiquidadas(cantidad1);
		valoresLiquidadosDTO.setRegistrosConError(cantidad2);
		
		return valoresLiquidadosDTO;
		
	}
	
	/**
	 * Metodo encargado de realizar la respuesta de la liquidacion de costos
	 * que es el  DTO el cual se muestra en la tabla valores liquidados consulta
	 *
	 * @param valoresLiquidados
	 * @return List<ParametrosLiquidacionCostoDTO>
	 * @author duvan.naranjo
	 */

	private List<ParametrosLiquidacionCostoDTO> generarRespuestaLiquidacionCostos(
			Date fechaSistema) {
		System.out.println(fechaSistema);
		List<ParametrosLiquidacionCostoDTO> respuest = parametrosLiquidacionCostosService.consultarParametrosLiquidacionCostos(fechaSistema);
		
		respuest.forEach(valorLiquidado -> {
			valorLiquidado.setNombreBanco(puntosService.getNombrePunto(
					dominioService.valorTextoDominio(Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BANCO),
					valorLiquidado.getCodigoBanco()));
			
			valorLiquidado
					.setNombreTdv(transportadorasService.getNombreTransportadora(valorLiquidado.getCodigoTdv()));

			Puntos puntoOrigen = puntosService.getPuntoById(valorLiquidado.getPuntoOrigen());

			valorLiquidado.setNombrePuntoOrigen(puntoOrigen.getNombrePunto());
			valorLiquidado
					.setNombreCiudadPuntoOrigen(ciudadesService.getNombreCiudad(puntoOrigen.getCodigoCiudad()));

			Puntos puntoDestino = puntosService.getPuntoById(valorLiquidado.getPuntoDestino());

			valorLiquidado.setNombrePuntoDestino(puntoDestino.getNombrePunto());
			valorLiquidado
					.setNombreCiudadPuntoDestino(ciudadesService.getNombreCiudad(puntoDestino.getCodigoCiudad()));
		});
	
		return respuest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValoresLiquidados consultarValoresLiquidadosPorIdLiquidacion(Long idLiquidacion) {
		return valoresLiquidadosRepository.consultarPorIdLiquidacion(idLiquidacion);
	}
}
