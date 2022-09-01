package com.ath.adminefectivo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IValoresLiquidadosRepository;
import com.ath.adminefectivo.service.IValoresLiquidadosService;

@Service
public class ValoresLiquidadosServicioImpl implements IValoresLiquidadosService{

	@Autowired
	IValoresLiquidadosRepository valoresLiquidadosRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ActualizaCostosFletesCharter(ParametrosLiquidacionCosto parametros) {
		ValoresLiquidados valores = valoresLiquidadosRepository.findByIdLiquidacion(
											parametros.getIdLiquidacion());
		if (Objects.isNull(valores)) {
			throw new NegocioException(ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_VALORES_LIQUIDADOS_NO_ENCONTRADO.getHttpStatus());
		}else {
			valores.setCostoCharter(parametros.getValorTotal());
			valoresLiquidadosRepository.save(valores);
		}
	}

}
