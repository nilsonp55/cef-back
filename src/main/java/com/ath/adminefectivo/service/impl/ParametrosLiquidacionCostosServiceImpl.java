package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IFuncionesDinamicasRepository;
import com.ath.adminefectivo.repositories.IParametrosLiquidacionCostosRepository;
import com.ath.adminefectivo.repositories.IEscalasRepository;
import com.ath.adminefectivo.repositories.ITarifasOperacionRepository;
import com.ath.adminefectivo.service.IEscalasService;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class ParametrosLiquidacionCostosServiceImpl implements IParametrosLiquidacionCostosService {

	@Autowired
	IParametrosLiquidacionCostosRepository parametrosLiquidacionCostosRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EstimadoClasificacionCostosDTO consultaEstimadosCostos(String transportadora, int bancoAval, int mesI,
			int anioI) {
		return parametrosLiquidacionCostosRepository.consultaEstimadosCostos(transportadora, bancoAval, mesI, anioI);
	}

	
	
	
}
