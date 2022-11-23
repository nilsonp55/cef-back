package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaLiquidarCostosDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IValoresLiquidadosRepository;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IDominioService;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean ActualizaCostosFletesCharter(costosCharterDTO costos) {
		Boolean estado = false;
		try {
			ValoresLiquidados valores = valoresLiquidadosRepository.findByIdLiquidacion(costos.getIdLiquidacion());
			if (Objects.isNull(valores)) {
				throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
			} else {
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
				ValoresLiquidadosDTO valoresLiquidadosDTO = new ValoresLiquidadosDTO();
				List<RespuestaLiquidarCostosDTO> respuestaLiquidarCostosDTO = new ArrayList<>();

				// Se ejecutan procedimientos
				String parametro = valoresLiquidadosRepository.armar_parametros_liquida(fecha);
				if (Integer.parseInt(parametro) > 0) {
					String resultado = valoresLiquidadosRepository.liquidar_costos(Integer.parseInt(parametro));
					// Sacamos los dos cumero de la cadena de texto
					String cantidad1 = "";
					String cantidad2 = "";
					boolean separador = false;
					for (int i = 0; i < resultado.length(); i++) {
						char item = resultado.charAt(i);
						if (item != '-' && separador == false) {
							cantidad1 += item;
						}
						if (item == '-') {
							separador = true;
						}
						if (item != '-' && separador == true) {
							cantidad2 += item;
						}
					}
					// Se obtienen los valores liquidados por el proceso
					List<ValoresLiquidados> valoresLiquidados = valoresLiquidadosRepository
							.findByIdSeqGrupo(Integer.parseInt(parametro));

					respuestaLiquidarCostosDTO = this.generarRespuestaLiquidacionCostos(valoresLiquidados);

					valoresLiquidadosDTO.setRespuestaLiquidarCostos(respuestaLiquidarCostosDTO);
					valoresLiquidadosDTO.setCantidadOperacionesLiquidadas(Integer.parseInt(cantidad1));
					valoresLiquidadosDTO.setRegistrosConError(Integer.parseInt(cantidad2));

				} else {
					throw new NegocioException(
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getCode(),
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getDescription(),
							ApiResponseCode.ERROR_PROCESO_CONSTO_VALORES_LIQUIDADOS_SIN_PARAM.getHttpStatus());
				}
				return valoresLiquidadosDTO;
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

	private List<RespuestaLiquidarCostosDTO> generarRespuestaLiquidacionCostos(
			List<ValoresLiquidados> valoresLiquidados) {
		List<RespuestaLiquidarCostosDTO> respuestaLiquidarCostosDTO = new ArrayList<>();
		valoresLiquidados.forEach(valorLiquidado -> {
			RespuestaLiquidarCostosDTO liquidarCostosDTO = new RespuestaLiquidarCostosDTO();
			liquidarCostosDTO.setFechaEjecucion(valorLiquidado.getParametrosLiquidacionCosto().getFechaEjecucion());
			liquidarCostosDTO.setCodigoBanco(valorLiquidado.getParametrosLiquidacionCosto().getCodigoBanco());
			liquidarCostosDTO.setCodigoTdv(valorLiquidado.getParametrosLiquidacionCosto().getCodigoTdv());
			liquidarCostosDTO.setTipoOperacion(valorLiquidado.getParametrosLiquidacionCosto().getTipoOperacion());
			liquidarCostosDTO.setTipoServicio(valorLiquidado.getParametrosLiquidacionCosto().getTipoServicio());
			liquidarCostosDTO.setPuntoOrigen(valorLiquidado.getParametrosLiquidacionCosto().getPuntoOrigen());
			liquidarCostosDTO.setPuntoDestino(valorLiquidado.getParametrosLiquidacionCosto().getPuntoDestino());
			liquidarCostosDTO.setEscala(valorLiquidado.getParametrosLiquidacionCosto().getEscala());
			liquidarCostosDTO.setNumeroFajos(valorLiquidado.getParametrosLiquidacionCosto().getNumeroFajos());
			liquidarCostosDTO.setResiduoBilletes(valorLiquidado.getParametrosLiquidacionCosto().getResiduoBilletes());
			liquidarCostosDTO.setNumeroBolsas(valorLiquidado.getParametrosLiquidacionCosto().getNumeroBolsas());
			liquidarCostosDTO.setResiduoMonedas(valorLiquidado.getParametrosLiquidacionCosto().getResiduoMonedas());
			liquidarCostosDTO.setValorTotal(valorLiquidado.getParametrosLiquidacionCosto().getValorTotal());
			liquidarCostosDTO.setNumeroParadas(valorLiquidado.getParametrosLiquidacionCosto().getNumeroParadas());
			liquidarCostosDTO.setMilajeParadas(null);
			liquidarCostosDTO.setMilajePorRuteo(valorLiquidado.getMilajePorRuteo());
			liquidarCostosDTO.setMilajeVerificacion(valorLiquidado.getMilajeVerificacion());
			liquidarCostosDTO.setCostoRuteoVerificacion(null);
			liquidarCostosDTO.setClasificacionFajado(valorLiquidado.getClasificacionFajado());
			liquidarCostosDTO.setClasificacionNoFajado(valorLiquidado.getClasificacionNoFajado());
			liquidarCostosDTO.setBilleteResiduo(valorLiquidado.getParametrosLiquidacionCosto().getResiduoBilletes());
			liquidarCostosDTO.setClasificacionMonedas(null);
			liquidarCostosDTO.setModenaResiduo(valorLiquidado.getParametrosLiquidacionCosto().getResiduoMonedas());
			liquidarCostosDTO.setBilleteResiduo(valorLiquidado.getParametrosLiquidacionCosto().getResiduoBilletes());
			liquidarCostosDTO.setCostoEmisario(valorLiquidado.getCostoEmisario());
			liquidarCostosDTO.setCostoPaqueteo(valorLiquidado.getCostoPaqueteo());
			liquidarCostosDTO.setTasaAeroportuaria(valorLiquidado.getTasaAeroportuaria());
			liquidarCostosDTO.setCostoCharter(valorLiquidado.getCostoCharter());
			liquidarCostosDTO.setIdSeqGrupo(valorLiquidado.getIdSeqGrupo());

			liquidarCostosDTO
					.setFechaLiquidacion(parametroServiceImpl.valorParametroDate(Constantes.FECHA_DIA_PROCESO));
			liquidarCostosDTO.setNombreBancoAval(puntosService.getNombrePunto(
					dominioService.valorTextoDominio(Constantes.DOMINIO_TIPOS_PUNTO, Dominios.TIPOS_PUNTO_BANCO),
					liquidarCostosDTO.getCodigoBanco()));
			liquidarCostosDTO
					.setNombreTdv(transportadorasService.getNombreTransportadora(liquidarCostosDTO.getCodigoTdv()));

			Puntos puntoOrigen = puntosService.getPuntoById(liquidarCostosDTO.getPuntoOrigen());

			liquidarCostosDTO.setNombrePuntoOrigen(puntoOrigen.getNombrePunto());
			liquidarCostosDTO
					.setNombreCiudadPuntoOrigen(ciudadesService.getNombreCiudad(puntoOrigen.getCodigoCiudad()));

			Puntos puntoDestino = puntosService.getPuntoById(liquidarCostosDTO.getPuntoDestino());

			liquidarCostosDTO.setNombrePuntoDestino(puntoDestino.getNombrePunto());
			liquidarCostosDTO
					.setNombreCiudadPuntoDestino(ciudadesService.getNombreCiudad(puntoDestino.getCodigoCiudad()));

			respuestaLiquidarCostosDTO.add(liquidarCostosDTO);

		});

		return respuestaLiquidarCostosDTO;
	}
}
