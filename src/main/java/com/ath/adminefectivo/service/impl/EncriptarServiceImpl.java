package com.ath.adminefectivo.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.encript.RSA;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IEncriptarService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.utils.S3Utils;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class EncriptarServiceImpl implements IEncriptarService {


	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IParametroService parametroService;
	
	@Autowired
	S3Utils s3utils;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encriptarArchivo(String path, String nombreArchivo) {
		try (BufferedReader bf = new BufferedReader(new FileReader(path + nombreArchivo));
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(path + "encriptado/" + nombreArchivo)))) {

			String bfRead;
			String textoEncriptado = "";
			RSA rsa = new RSA(parametroService, s3utils);
			while ((bfRead = bf.readLine()) != null) {
				if (!textoEncriptado.equals("")) {
					bw.newLine();
				}
				textoEncriptado = rsa.encrypt(bfRead);
				bw.write(textoEncriptado);
			}

		} catch (Exception e) {
			log.error("Encriptando archivo: {} - path: {} - mesnaje: {}", nombreArchivo, path, e.getMessage());
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}
		return "Se encripto el archivo exitosamente";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String desencriptarArchivo(String path, String nombreArchivo) {
		try (BufferedReader bf = new BufferedReader(new FileReader(path + nombreArchivo))) {

			String bfRead;
			RSA rsa = new RSA(parametroService, s3utils);
			while ((bfRead = bf.readLine()) != null) {
				rsa.decrypt(bfRead);
			}
		} catch (Exception e) {
			log.error("Desencriptando archivo: {} - path: {} - mensaje: {}", nombreArchivo, path, e.getMessage());
			throw new NegocioException(ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getCode(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getDescription() + " - " + e.getMessage(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getHttpStatus());
		}

		return "Se desencripto el archivo exitosamente";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String[]> desencriptarArchivoPorAlgoritmo(String algoritmoEncriptado,
			InputStream archivo, String delimitador) {
		
		if(algoritmoEncriptado.equals(dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_ENCRIPTADO, Dominios.TIPO_ENCRIPTADO_RSA))) {
			return this.desencriptarArchivoAlgoritmoRSA(archivo, delimitador);
		}
		return new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarLlaves() {
		RSA rsa = new RSA(parametroService, s3utils);
		rsa.createKeys();
		return "Se crearon las llaves de forma exitosa";
	}
	
	private List<String[]> desencriptarArchivoAlgoritmoRSA(InputStream archivo, String delimitador) {
		
		List<String[]> resultado = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(archivo));
		String textoEncriptado;
		RSA rsa = new RSA(parametroService, s3utils);
		
		try {
			while ((textoEncriptado = br.readLine()) != null) {
					String textoDesencriptado;
					textoDesencriptado = rsa.decrypt(textoEncriptado);
					resultado.add(textoDesencriptado.split(delimitador));
			}
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getCode(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getDescription()+ " - "+ e.getMessage(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getHttpStatus());
		}
		return resultado;
	}
	


	
	
	
}
