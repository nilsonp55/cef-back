package com.ath.adminefectivo.encript;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import java.io.*;
import java.nio.file.Files;
import javax.crypto.Cipher;


import javax.xml.bind.DatatypeConverter;
public class GenerarRSA  {

	private KeyPair keyPair;

	public void generarClaves(int size) throws NoSuchAlgorithmException {
		System.out.println("Generando claves de "+size+" bits. ");
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(size);
		KeyPair pair = generator.generateKeyPair();
		this.keyPair = pair;
	}
	
	public void borrar() throws Exception {
		try {
		File file = new File("publicRSA.key");
		file.delete();
		
		File file2 = new File("privateRSA.key");
		file2.delete();
		} catch(Exception e) {
			System.out.println("No se han borrado archivos. Continua el proceso.");
		}


	} 
	
	public void grabarLlaves() throws Exception {
		
		FileOutputStream fos = new FileOutputStream("publicRSA.key");
		fos.write(this.getPublicKey().getEncoded());
		
		FileOutputStream fos2 = new FileOutputStream("privateRSA.key");
		fos2.write(this.getPrivateKey().getEncoded());

	} 
	
	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}
	
	public PublicKey getPublicKey() {
		 
		 return keyPair.getPublic();
	}
	
	

	public static void main(String args[]) throws Exception {


		GenerarRSA keyRSA = new GenerarRSA();
	
		System.out.println("Generando claves...");
		keyRSA.borrar();
		int tamanio = Integer.parseInt(args[0]);
		keyRSA.generarClaves(tamanio);
		keyRSA.grabarLlaves();
		System.out.println("Claves generadas.");
		
	}

}