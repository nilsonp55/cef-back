package com.ath.adminefectivo.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

@Service
public class LecturaArchivoServiceImpl implements ILecturaArchivoService {

	@Autowired
	IDominioService dominioService;

	@Override
	public String obtenerDelimitadorArchivo(MaestrosDefinicionArchivoDTO maestroDefinicion) {

		String delimitador;

		if (Objects.isNull(maestroDefinicion.getDelimitadorBase())
				|| (Objects.equals(maestroDefinicion.getDelimitadorBase(), Constantes.DELIMITADOR_OTROS)
						&& Objects.isNull(maestroDefinicion.getDelimitadorOtro()))) {

			throw new AplicationException(ApiResponseCode.ERROR_DELIMITADOR_VACIO.getCode(),
					ApiResponseCode.ERROR_DELIMITADOR_VACIO.getDescription(),
					ApiResponseCode.ERROR_DELIMITADOR_VACIO.getHttpStatus());
		}

		if (Objects.equals(maestroDefinicion.getDelimitadorBase(), Constantes.DELIMITADOR_OTROS)) {
			delimitador = maestroDefinicion.getDelimitadorOtro();
		} else {
			delimitador = dominioService.valorTextoDominio(Constantes.DOMINIO_DELIMITADOR,
					maestroDefinicion.getDelimitadorBase());
		}

		return delimitador;
	}

	@Override
	public List<String[]> leerArchivo(InputStream archivo, String delimitador) {

		CSVParser parser = new CSVParserBuilder().withSeparator(delimitador.charAt(0)).withIgnoreQuotations(true).build();
		
		try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(archivo)).withCSVParser(parser).build()) {
			return csvReader.readAll();
		} catch (IOException | CsvException e) {
			throw new AplicationException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
					ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
					ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
		}
	}

}
