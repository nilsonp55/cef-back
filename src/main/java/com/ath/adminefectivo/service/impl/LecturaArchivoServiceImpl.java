package com.ath.adminefectivo.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IEncriptarService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LecturaArchivoServiceImpl implements ILecturaArchivoService {

	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IEncriptarService encriptarService;

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
	public List<String[]> leerArchivo(InputStream archivo, String delimitador, MaestrosDefinicionArchivoDTO maestroDefinicion) {

		String algoritmoEncriptado = this.validarEncriptado(maestroDefinicion.getTipoDeEncriptado());
		
		if(algoritmoEncriptado.equals(dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_ENCRIPTADO, Dominios.TIPO_ENCRIPTADO_NA))) {
			
			CSVParser parser = new CSVParserBuilder().withSeparator(delimitador.charAt(0)).withIgnoreQuotations(true).build();
			
			List<String[]> resultadoValidado = new ArrayList<>();
			try {
					CSVReader csvReader;
				if(maestroDefinicion.getIdMaestroDefinicionArchivo().equals(Dominios.TIPO_ARCHIVO_ISRPO)) {
					csvReader = new CSVReaderBuilder(new InputStreamReader(archivo, StandardCharsets.UTF_16)).withCSVParser(parser).build();
				}else {
					csvReader = new CSVReaderBuilder(new InputStreamReader(archivo)).withCSVParser(parser).build();
				}
				
				List<String[]> resultadoSinValidar = csvReader.readAll();
					resultadoSinValidar.forEach(linea ->{
					log.debug("csvReader.readAll(); "+ linea.length);
					if(linea.length > 2) {
						resultadoValidado.add(linea);
					}}
					);
					return resultadoValidado;
			} catch (Exception e) {
				throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
						ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
						ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
			}
			
			
		}else {
			return encriptarService.desencriptarArchivoPorAlgoritmo(algoritmoEncriptado, archivo,delimitador );
		}
		
		
		
		
	}

	private String validarEncriptado(String tipoEncriptado) {
		String valorEncriptado = dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_ENCRIPTADO, tipoEncriptado);
		if(Objects.isNull(valorEncriptado)) {
			throw new NegocioException(ApiResponseCode.ERROR_DOMINIO_TIPO_ENCRIPTADO.getCode(),
					ApiResponseCode.ERROR_DOMINIO_TIPO_ENCRIPTADO.getDescription(),
					ApiResponseCode.ERROR_DOMINIO_TIPO_ENCRIPTADO.getHttpStatus());
		}
		
		return valorEncriptado;
	}

}
