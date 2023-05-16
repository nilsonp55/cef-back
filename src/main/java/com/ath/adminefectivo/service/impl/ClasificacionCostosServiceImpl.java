package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.ClasificacionCostosDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.ClasificacionCostos;
import com.ath.adminefectivo.repositories.IClasificacionCostosRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IClasificacionCostosService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IParametrosLiquidacionCostosService;
import com.ath.adminefectivo.service.ITarifasOperacionService;

@Service
public class ClasificacionCostosServiceImpl implements IClasificacionCostosService {

	@Autowired
	IClasificacionCostosRepository clasificacionCostosRepository;
	
	@Autowired
	IBancosService bancosService;
	
	@Autowired
	IParametroService parametroService;
	
	@Autowired
	IParametrosLiquidacionCostosService parametrosLiquidacionCostosService;
	
	@Autowired
	ITarifasOperacionService tarifasOperacionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CostosMensualesClasificacionDTO> getClasificacionMensualCostos(String transportadora) {
		
		List<CostosMensualesClasificacionDTO> costosMensualesClasificacion = new ArrayList<>();
		Date fechaSistema = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		String fechaSistemaS = parametroService.valorParametro(Constantes.FECHA_DIA_PROCESO);
		String[] diaMesAnio = fechaSistemaS.split("/");
		String mesAnio = diaMesAnio[1]+"-"+diaMesAnio[2];
		List<ClasificacionCostos> listadoClasificacionCostos = clasificacionCostosRepository.findByTransportadoraAndMesAnio(transportadora, mesAnio);
		List<BancosDTO> bancosAval = bancosService.getBancosPorAval(true);
		
		if(!listadoClasificacionCostos.isEmpty() && listadoClasificacionCostos.size()>3) {
			listadoClasificacionCostos.forEach(clasificacionCosto ->{
				clasificacionCostosRepository.delete(clasificacionCosto);
				int bancoBanco = clasificacionCosto.getBancoAval(); 
				ClasificacionCostosDTO clasificacionCostoDTO =  this.procesarClasificacionCostosPorBanco(clasificacionCosto, transportadora);
				clasificacionCostoDTO .setMesAnio(mesAnio);
				
				String nombreBanco = bancosAval.stream()
				                     				.filter(banco -> banco.getCodigoPunto() == bancoBanco)
				                     				.findFirst().orElse(null).getNombreBanco();
				
				costosMensualesClasificacion.add(this.generarClasificacionMensualesDTO(clasificacionCostoDTO, fechaSistema,nombreBanco, transportadora));
			});
		}else {
			
				bancosAval.forEach(bancoAval -> {
					ClasificacionCostos clasificacionCosto = new ClasificacionCostos();
					clasificacionCosto.setMesAnio(mesAnio);
					clasificacionCosto.setBancoAval(bancoAval.getCodigoPunto());
					clasificacionCosto.setTransportadora(transportadora);
					
					ClasificacionCostosDTO clasificacionCostoDTO = this.procesarClasificacionCostosPorBanco(clasificacionCosto, transportadora);
					
					if(!Objects.isNull(clasificacionCosto)) {
						costosMensualesClasificacion.add(this.generarClasificacionMensualesDTO(clasificacionCostoDTO, fechaSistema, bancoAval.getNombreBanco(), transportadora));
					}
					
				});	
		}	
		return costosMensualesClasificacion;
	}

	private CostosMensualesClasificacionDTO generarClasificacionMensualesDTO(ClasificacionCostosDTO clasificacionCosto,
			Date fechaSistema, String nombreBanco, String codigoTdv) {
		return CostosMensualesClasificacionDTO.builder()
				.codigoBanco(clasificacionCosto.getBancoAval())
				.mesAnio(clasificacionCosto.getMesAnio()).cantidadEstimadaFajos(clasificacionCosto.getFajosEstimados())
				.cantidadEstimadaBolsas(clasificacionCosto.getBolsasEstimadas()).cantidadAsignadaRem(0).cantidadAsignadaBolsas(0)
				.cantidadAsignadaFajos(0).cantidadAsignadaRem(0).valorLiquidadoFajos(0).valorLiquidadoBolsas(0).valorLiquidadoRem(0)
				.valorTotalLiquidacion(0).fechaSistema(fechaSistema).nombreBanco(nombreBanco).codigoTdv(codigoTdv)
				.build();
	}

	private ClasificacionCostosDTO procesarClasificacionCostosPorBanco(ClasificacionCostos clasificacionCosto,
			String transportadora) {
		String mesAnio = clasificacionCosto.getMesAnio();
		String[] mesAnioA = mesAnio.split(Constantes.SEPARADOR_COSTOS_CLASIFICACION_MES_ANIO);
		int mesI = Integer.parseInt(mesAnioA[0]);
		int anioI = Integer.parseInt(mesAnioA[1]);
		Integer fajosEstimados = 0;
		Integer bolsasEstimados = 0;

		EstimadoClasificacionCostosDTO estimadoBanco = parametrosLiquidacionCostosService.consultaEstimadosCostos(transportadora,clasificacionCosto.getBancoAval(), mesI, anioI);
		if(!Objects.isNull(estimadoBanco)) {
			if(!Objects.isNull(estimadoBanco.getEstimadaFajos())) {
				fajosEstimados = Math.toIntExact(estimadoBanco.getEstimadaFajos());
				bolsasEstimados = Math.toIntExact(estimadoBanco.getEstimadaBolsas());
			}else {
				fajosEstimados = 0;
				bolsasEstimados = 0;
			}
		}

		clasificacionCosto.setFajosEstimados(fajosEstimados);
		clasificacionCosto.setBolsasEstimadas(bolsasEstimados);
		clasificacionCosto.setCantidadFajos(0);
		clasificacionCosto.setCantidadMonedas(0);
		clasificacionCosto.setCantidadRem(0);
		clasificacionCosto.setValorClasifFajos(0);
		clasificacionCosto.setValorClasifMonedas(0);
		clasificacionCosto.setValorClasifRem(0);
		clasificacionCosto.setEstado(1);
		clasificacionCosto.setFechaModificacion(new Date());
		return ClasificacionCostosDTO.CONVERTER_DTO.apply(clasificacionCostosRepository.save(clasificacionCosto));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CostosMensualesClasificacionDTO> liquidarClasificacionCostos(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		listadoCostosMensuales.forEach(costoMensual -> {
			List<TarifasOperacionDTO> listadoTarifasOperacionDTO = tarifasOperacionService.getTarifasOperacionByCodigoBancoAndCodigoTdv(costoMensual.getCodigoBanco(), costoMensual.getCodigoTdv(), costoMensual.getFechaSistema());
			if(!Objects.isNull(listadoTarifasOperacionDTO)) {
				listadoTarifasOperacionDTO.forEach(tarifaOp -> {
					if(tarifaOp.getComisionAplicar().equals(Constantes.COMISION_APLICAR_CLASIFICACION_DETERIORADO)) {
						costoMensual.setValorLiquidadoRem(costoMensual.getCantidadAsignadaRem() * tarifaOp.getValorTarifa().intValue());
					}else if(tarifaOp.getComisionAplicar().equals(Constantes.COMISION_APLICAR_CLASIFICACION_FAJADO)) {
						costoMensual.setValorLiquidadoFajos(costoMensual.getCantidadAsignadaFajos() * tarifaOp.getValorTarifa().intValue());
					}else if(tarifaOp.getComisionAplicar().equals(Constantes.COMISION_APLICAR_CLASIFICACION_MONEDA)) {
						costoMensual.setValorLiquidadoBolsas((costoMensual.getCantidadAsignadaBolsas() * tarifaOp.getValorTarifa().intValue()));
					}
				});
				costoMensual.setValorTotalLiquidacion(costoMensual.getValorLiquidadoRem() + 
													  costoMensual.getValorLiquidadoBolsas() + 
													  costoMensual.getValorLiquidadoFajos());
			}
		});
		return listadoCostosMensuales;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales) {
		if(!listadoCostosMensuales.isEmpty()) {
			this.eliminarExistentesMesAnio(listadoCostosMensuales.get(0).getMesAnio(), listadoCostosMensuales.get(0).getCodigoTdv());
			List<ClasificacionCostosDTO> listadoClasificacionCostosDTO = new ArrayList<>();
			
			listadoCostosMensuales.forEach(costoMensual -> 
				listadoClasificacionCostosDTO.add(this.generarClasificacionCosto(costoMensual))
			);
			if(listadoClasificacionCostosDTO.size() > 0) {
				return Constantes.MENSAJE_GENERO_CLASIFICACION_COSTOS_CORRECTO;
			}
		}
		
		return Constantes.MENSAJE_GENERO_CLASIFICACION_COSTOS_ERRONEO;
	}

	/**
	 * Metodo encargado de generar la operacion clasificacion a traves del DTO de costosMensualesClasificacionDTO
	 * y guardarlo 
	 * 
	 * @param costoMensual
	 * @return ClasificacionCostosDTO
	 * @author duvan.naranjo
	 */
	@Transactional
	private ClasificacionCostosDTO generarClasificacionCosto(CostosMensualesClasificacionDTO costoMensual) {
		
		ClasificacionCostos costoClasificacion = new ClasificacionCostos();
		costoClasificacion.setBancoAval(costoMensual.getCodigoBanco());
		costoClasificacion.setBolsasEstimadas(costoMensual.getCantidadEstimadaBolsas());
		costoClasificacion.setCantidadFajos(costoMensual.getCantidadAsignadaFajos());
		costoClasificacion.setCantidadMonedas(costoMensual.getCantidadAsignadaBolsas());
		costoClasificacion.setCantidadRem(costoMensual.getCantidadAsignadaRem());
		costoClasificacion.setEstado(1);
		costoClasificacion.setFajosEstimados(costoMensual.getCantidadEstimadaFajos());
		costoClasificacion.setFechaModificacion(new Date());
		costoClasificacion.setMesAnio(costoMensual.getMesAnio());
		costoClasificacion.setTransportadora(costoMensual.getCodigoTdv());
		costoClasificacion.setUsuarioModificacion("admin");
		costoClasificacion.setValorClasifFajos(Math.toIntExact(costoMensual.getValorLiquidadoFajos()));
		costoClasificacion.setValorClasifMonedas(Math.toIntExact(costoMensual.getValorLiquidadoBolsas()));
		costoClasificacion.setValorClasifRem(Math.toIntExact(costoMensual.getValorLiquidadoRem()));
		
		return ClasificacionCostosDTO.CONVERTER_DTO.apply(clasificacionCostosRepository.save(costoClasificacion));
	}

	private void eliminarExistentesMesAnio(String mesAnio, String transportadora) {
		List<ClasificacionCostos> existentesClasificacionCostos = clasificacionCostosRepository.findByTransportadoraAndMesAnio(transportadora, mesAnio);
		if(!existentesClasificacionCostos.isEmpty()) {
			existentesClasificacionCostos.forEach(clasifCosto ->
				clasificacionCostosRepository.delete(clasifCosto)
			);
		}	
	}

}
