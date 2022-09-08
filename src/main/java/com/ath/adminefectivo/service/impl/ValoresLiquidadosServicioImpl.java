package com.ath.adminefectivo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
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

}
