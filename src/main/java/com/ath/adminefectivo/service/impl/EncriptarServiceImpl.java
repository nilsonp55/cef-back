package com.ath.adminefectivo.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;

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


@Service
public class EncriptarServiceImpl implements IEncriptarService {


	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IParametroService parametroService;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encriptarArchivo(String path, String nombreArchivo) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path+nombreArchivo));
			FileWriter fw = new FileWriter(path+"encriptado/"+nombreArchivo);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"encriptado/"+nombreArchivo)));

			String bfRead;
			String textoEncriptado = "";
			RSA rsa = new RSA(parametroService);
	        try{
	        	while ((bfRead = bf.readLine()) != null) {
					if(!textoEncriptado.equals("")) {
						bw.newLine();
					}						
					textoEncriptado =rsa.encrypt(bfRead); 
					bw.write(textoEncriptado);
			}
			bw.close();
	        }catch (Exception ingored){}
			
		} catch (Exception e) {
			System.out.println("Archivo con ruta "+path+nombreArchivo +" no existe. " +e);
		}
		
		return "Se encripto el archivo exitosamente";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String desencriptarArchivo(String path, String nombreArchivo) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path+nombreArchivo));
			String bfRead;
			String textoDesencriptado;
			RSA rsa = new RSA(parametroService);
			while ((bfRead = bf.readLine()) != null) {
					textoDesencriptado = rsa.decrypt(bfRead);
			}
		} catch (BadPaddingException e) {
			throw new NegocioException(ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getCode(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getDescription()+ " - "+ e.getMessage(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getHttpStatus());
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getCode(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getDescription()+ " - "+ e.getMessage(),
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
		
		List<String[]> resultado = new ArrayList<>();
		if(algoritmoEncriptado.equals(dominioService.valorTextoDominio(Constantes.DOMINIO_TIPO_ENCRIPTADO, Dominios.TIPO_ENCRIPTADO_RSA))) {
			return this.desencriptarArchivoAlgoritmoRSA(archivo, delimitador);
		}else {
			
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarLlaves() {
		RSA rsa = new RSA(parametroService);
		rsa.createKeys();
		return "Se crearon las llaves de forma exitosa";
	}
	
	private List<String[]> desencriptarArchivoAlgoritmoRSA(InputStream archivo, String delimitador) {
		
		List<String[]> resultado = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(archivo));
		String textoEncriptado;
		RSA rsa = new RSA(parametroService);
		
		try {
			while ((textoEncriptado = br.readLine()) != null) {
					String textoDesencriptado;
					try {
						textoDesencriptado = rsa.decrypt(textoEncriptado);
						resultado.add(textoDesencriptado.split(delimitador));
					} catch (Exception e) {
						throw new NegocioException(ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getCode(),
								ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getDescription()+ " - "+ e.getMessage(),
								ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getHttpStatus());
					}
			}
		} catch (IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getCode(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getDescription()+ " - "+ e.getMessage(),
					ApiResponseCode.ERROR_DESENCRIPTANDO_CADENA.getHttpStatus());
		}
		return null;
	}


	
	
	
}
