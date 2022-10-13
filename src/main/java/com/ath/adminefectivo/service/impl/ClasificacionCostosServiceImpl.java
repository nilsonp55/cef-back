package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.ClasificacionCostosDTO;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IClasificacionCostosRepository;
import com.ath.adminefectivo.repositories.IFuncionesDinamicasRepository;
import com.ath.adminefectivo.repositories.IEscalasRepository;
import com.ath.adminefectivo.repositories.ITarifasOperacionRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IClasificacionCostosService;
import com.ath.adminefectivo.service.IEscalasService;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class ClasificacionCostosServiceImpl implements IClasificacionCostosService {

	@Autowired
	IClasificacionCostosRepository clasificacionCostosRepository;
	
	@Autowired
	IBancosService bancosService;
	
	@Autowired
	IParametroService parametroService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CostosMensualesClasificacionDTO> getClasificacionMensualCostos(String transportadora, String mesAnio) {
		
		List<CostosMensualesClasificacionDTO> costosMensualesClasificacion = new ArrayList();
		
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		List<ClasificacionCostosDTO> listadoClasificacionCostos = clasificacionCostosRepository.findByTransportadoraAndMesAnio(transportadora, mesAnio);
		
		if(!Objects.isNull(listadoClasificacionCostos) && listadoClasificacionCostos.size()>3) {
			listadoClasificacionCostos.forEach(clasificacionCosto ->{
				CostosMensualesClasificacionDTO costoMensualBanco = new CostosMensualesClasificacionDTO();
				costoMensualBanco.setCodigoBanco(clasificacionCosto.getBancoAval());
				costoMensualBanco.setFechaSistema(fechaSistema);
				costoMensualBanco.setMesAnio(mesAnio);
				costoMensualBanco = this.procesarClasificacionCostosPorBanco(costoMensualBanco, transportadora);
				
				costosMensualesClasificacion.add(costoMensualBanco);
			});
		}else {
			if(!Objects.isNull(listadoClasificacionCostos)) {
				listadoClasificacionCostos.forEach(dato ->{
					clasificacionCostosRepository.delete(ClasificacionCostosDTO.CONVERTER_ENTITY.apply(dato));
				});
				List<BancosDTO> bancosAval = bancosService.getBancosPorAval(true);
				bancosAval.forEach(bancoAval -> {
					CostosMensualesClasificacionDTO costoMensualBanco = new CostosMensualesClasificacionDTO();
					
					costoMensualBanco.setCodigoBanco(bancoAval.getCodigoPunto());
					costoMensualBanco.setFechaSistema(fechaSistema);
					costoMensualBanco.setMesAnio(mesAnio);
					costoMensualBanco = this.procesarClasificacionCostosPorBanco(costoMensualBanco, transportadora);
					costosMensualesClasificacion.add(costoMensualBanco);
				});
			}
		}
		
		
		return costosMensualesClasificacion;
	}



	private CostosMensualesClasificacionDTO procesarClasificacionCostosPorBanco(
			CostosMensualesClasificacionDTO costoMensualBanco, String transportadora) {
		// TODO Auto-generated method stub
		return null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CostosMensualesClasificacionDTO> liquidarClasificacionCostos(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
