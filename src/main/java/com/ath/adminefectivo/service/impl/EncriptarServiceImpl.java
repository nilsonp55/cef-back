package com.ath.adminefectivo.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.encript.Encriptar;
import com.ath.adminefectivo.encript.GenerarRSA;
import com.ath.adminefectivo.encript.ReadKey;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IEncriptarService;


@Service
public class EncriptarServiceImpl implements IEncriptarService {


	@Autowired
	IDominioService dominioService;
	
	
	Encriptar encriptador = new Encriptar();
	GenerarRSA generarRSA= new GenerarRSA();
	ReadKey readKey = new ReadKey();

	@Override
	public String encriptarArchivo(String path, String nombreArchivo) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path+nombreArchivo));
			FileWriter fw = new FileWriter(path+"encriptado/"+nombreArchivo);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"encriptado/"+nombreArchivo)));
			generarRSA.generarClaves(1024);
			generarRSA.grabarLlaves();
			String bfRead;
			String textoEncriptado;
			while ((bfRead = bf.readLine()) != null) {
					System.out.println(bfRead);
					textoEncriptado = encriptador.encriptar(bfRead);
					bw.write(textoEncriptado);
					bw.newLine();
					
					
			}
			bw.close();
		} catch (Exception e) {
			System.out.println("Archivo con ruta "+path+nombreArchivo +" no existe. " +e);
			// TODO: handle exception
		}
		
		return "Se encripto el archivo exitosamente";
	}

	@Override
	public String desencriptarArchivo(String path, String nombreArchivo) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path+nombreArchivo));
			String bfRead;
			String textoDesencriptado;
			while ((bfRead = bf.readLine()) != null) {
					
					textoDesencriptado = readKey.leer(bfRead);
					System.out.println(textoDesencriptado);
					
					
			}
		} catch (Exception e) {
			System.out.println("Archivo con ruta "+path+nombreArchivo +" no existe. " +e);
			// TODO: handle exception
		}
		
		return "Se desencripto el archivo exitosamente";
	}
	
	
	
}
