package com.ath.adminefectivo.delegate.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IContabilidadDelegate;
import com.ath.adminefectivo.service.IContabilidadService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;

@Service
public class ContabilidadDelegateImpl implements IContabilidadDelegate {

	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;
	
	@Autowired
	IContabilidadService contabilidadService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarContabilidad(String tipoContabilidad) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String f1 = "2022-05-28";
		String f2 = "2022-06-05";
		Date fechaInicio = null;
		Date fechaFin = null;
		try {
			fechaInicio = sdf.parse(f1);
			fechaFin = sdf.parse(f2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		var operacionesProgramadas = operacionesProgramadasService.getOperacionesProgramadasPorFechas(tipoContabilidad, fechaInicio,fechaFin);
		operacionesProgramadas.forEach(op ->{
			System.out.println("////  "+op);
		});
		if(!operacionesProgramadas.isEmpty()) {
			int resultado = contabilidadService.generarContabilidad(tipoContabilidad, operacionesProgramadas);
			if(resultado > 0) {
				return "MENSAJE EXITOSO";
			}else {
				return "NO SE ENCONTRARON OPERACIONES POR PROCESAR";
			}
		}
		return "NO SE ENCONTRARON OPERACIONES POR PROCESAR";
	}


}
