package com.ath.adminefectivo.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.CostoBolsaMonedaAdicionalDTO;
import com.ath.adminefectivo.dto.ListCostoBolsaMonedaAdicionalDTO;
import com.ath.adminefectivo.entities.CostoBolsaMonedaAdicional;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.ICostoBolsaMonedaAdicionalRepository;
import com.ath.adminefectivo.service.ICostoBolsaMonedaAdicionalService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

/**
 * Clase que implementa la interface  ICostoBolsaMonedaAdicionalService para operaciones crud
 * de CostoBolsaMonedaAdicional
 * @author hector.mercado
 */
@Service
@Log4j2
public class CostoBolsaMonedaAdicionalServiceImpl implements ICostoBolsaMonedaAdicionalService {
	
	@Autowired
	ICostoBolsaMonedaAdicionalRepository costoBolsaMonedaAdicionalRepository;


	@Override
	public Page<CostoBolsaMonedaAdicionalDTO> getCostoBolsaMonedaAdicional(Predicate predicate, Pageable pageable) {
		Page<CostoBolsaMonedaAdicional> costoBolsaMonedaAdicional = costoBolsaMonedaAdicionalRepository.findAll(predicate, pageable);
		
		return new PageImpl<>(costoBolsaMonedaAdicional.getContent().stream().map(CostoBolsaMonedaAdicionalDTO
		.CONVERTER_DTO).toList(), costoBolsaMonedaAdicional.getPageable(), costoBolsaMonedaAdicional.getTotalElements());
	}

	@Override
	public Page<CostoBolsaMonedaAdicionalDTO> getCostoBolsaMonedaAdicionalByTransportadora(String codigoTdv) {
		
		List<CostoBolsaMonedaAdicional> costoBolsaMonedaAdicional = 
				costoBolsaMonedaAdicionalRepository.findByTransportadora(codigoTdv);
		Pageable pageRequest = PageRequest.of(Constantes.PAGINA_DEFAULT, Constantes.TAMANO_PAGINA);

	    int start = (int) pageRequest.getOffset();
	    int end = Math.min((start + pageRequest.getPageSize()), costoBolsaMonedaAdicional.size());

	    List<CostoBolsaMonedaAdicional> pageContent = costoBolsaMonedaAdicional.subList(start, end);
	  
		return new PageImpl<>(pageContent.stream().map(CostoBolsaMonedaAdicionalDTO
		.CONVERTER_DTO).toList(), pageRequest, costoBolsaMonedaAdicional.size());
	}

	@Override
	public CostoBolsaMonedaAdicionalDTO saveCostoBolsaMonedaAdicional(CostoBolsaMonedaAdicionalDTO costoBolsaMonedaAdicionalDTO) {
		
		CostoBolsaMonedaAdicional costoBolsaMonedaAdicional = CostoBolsaMonedaAdicionalDTO.CONVERTER_ENTITY.apply(costoBolsaMonedaAdicionalDTO);
		costoBolsaMonedaAdicional.setEstadoCostoBolsaMonedaAdicional(Dominios.ESTADO_GENERAL_OK);
		
		List<CostoBolsaMonedaAdicional> existen = costoBolsaMonedaAdicionalRepository.findByTransportadora(costoBolsaMonedaAdicionalDTO.getTransportadoraDTO().getCodigo());
		if (!Objects.isNull(existen)) 
		{
			for (CostoBolsaMonedaAdicional costotdv : existen)
			{
				if (!costotdv.getIdCostoBolsaAdicional().equals(costoBolsaMonedaAdicionalDTO.getIdCostoBolsaAdicional()))
				{
					throw new ConflictException("Existen 2  registros con la misma TDV asociada, valide la información y guarde nuevamente");
				}
			}

		}
		BigDecimal costoAdicional =  costoBolsaMonedaAdicional.getValorBolsasAdicional();
		if (costoAdicional.scale()>6)
		{
			BigDecimal newValue =  costoAdicional.setScale(6, RoundingMode.DOWN);
			costoBolsaMonedaAdicional.setValorBolsasAdicional(newValue);
		}
		return CostoBolsaMonedaAdicionalDTO.CONVERTER_DTO.apply(costoBolsaMonedaAdicionalRepository.save(costoBolsaMonedaAdicional));
	}

	@Override
	public boolean eliminarCostoBolsaMonedaAdicional(ListCostoBolsaMonedaAdicionalDTO registrosEliminar) {
		var resultado = true; 
		if (!Objects.isNull(registrosEliminar) && !Objects.isNull(registrosEliminar.getList()))
		{
			List<CostoBolsaMonedaAdicionalDTO> registros = registrosEliminar.getList();
			for (CostoBolsaMonedaAdicionalDTO costo : registrosEliminar.getList())
			{
				if (!eliminarCostoBolsoMonedaAdicional(costo))
					resultado = false;
			}
		}
		return resultado;
	}
	
	
	private boolean eliminarCostoBolsoMonedaAdicional(CostoBolsaMonedaAdicionalDTO dto)
	{
		try
		{
			Integer idCostoBolsaAdicional = dto.getIdCostoBolsaAdicional();
			var costoBolsaMonedaAdicional = costoBolsaMonedaAdicionalRepository.findById(idCostoBolsaAdicional);
			
			if (costoBolsaMonedaAdicional.isPresent()) {
				CostoBolsaMonedaAdicional costoBolsaMonedaAdicionalFound = costoBolsaMonedaAdicional.get();
				costoBolsaMonedaAdicionalFound.setEstadoCostoBolsaMonedaAdicional(Dominios.ESTADO_GENERAL_ELIMINADO);
				costoBolsaMonedaAdicionalRepository.save(costoBolsaMonedaAdicionalFound);
				return true;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		return false;
	}
	
	

}
