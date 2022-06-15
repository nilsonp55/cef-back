package com.ath.adminefectivo.delegate.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.delegate.IOperacionesProgramadasDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;

@Service
public class OperacionesProgramadasDelegateImpl implements IOperacionesProgramadasDelegate {

	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;

	@Autowired
	IArchivosCargadosService archivosCargadosService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarOperacionesProgramadas() {

		List<ArchivosCargadosDTO> listadoArchivosCargados = archivosCargadosService.getArchivosCargadosSinProcesar(Dominios.TIPO_ARCHIVO_IPPSV);
		if (!Objects.isNull(listadoArchivosCargados)) {
			List<OperacionesProgramadasDTO> operacionesProgramadas = operacionesProgramadasService
					.generarOperacionesProgramadas(listadoArchivosCargados);
			if (!operacionesProgramadas.isEmpty()) {
				return Constantes.MENSAJE_GENERO_OPERACIONES_PROGRAMADAS_CORRECTO;
			}
		}
		return Constantes.MENSAJE_NO_SE_ENCONTRARON_ARCHIVOS_OP;
	}

}
