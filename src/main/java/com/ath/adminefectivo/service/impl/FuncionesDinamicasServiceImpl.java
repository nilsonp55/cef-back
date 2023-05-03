package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoFuncionDinamicaDTO;
import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.repositories.IFuncionesDinamicasRepository;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;

@Service
public class FuncionesDinamicasServiceImpl implements IFuncionesDinamicasService {

	@Autowired
	IFuncionesDinamicasRepository funcionesDinamicasRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FuncionesDinamicasDTO> obtenerFuncionesDinamicasActivas() {
		List<FuncionesDinamicas> funcionesDinamicasEntity = funcionesDinamicasRepository.findByEstado(1);
		
		List<FuncionesDinamicasDTO> funcionesDinamicasDTO = new ArrayList<>();
		funcionesDinamicasEntity.forEach(funcionDinamicas ->{
			funcionesDinamicasDTO.add(FuncionesDinamicasDTO.CONVERTER_DTO.apply(funcionDinamicas));
		});
		return funcionesDinamicasDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> ejecutarFuncionDinamica(int idFuncion, String parametros) {
		List<ResultadoFuncionDinamicaDTO> respuesta =funcionesDinamicasRepository.ejecutar_procedimiento(idFuncion, parametros); 
		List<String> resultado = new ArrayList<>();
		respuesta.forEach(item ->{
			resultado.add(item.getResultado());
		});
		return resultado;
	}


	
}
