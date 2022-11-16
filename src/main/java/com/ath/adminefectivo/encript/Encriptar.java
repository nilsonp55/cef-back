package com.ath.adminefectivo.encript;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import java.io.*;
import java.nio.file.Files;
import javax.crypto.Cipher;
import java.security.interfaces.RSAPrivateCrtKey;

import javax.xml.bind.DatatypeConverter;
public class Encriptar  {

	private KeyPair keyPair;


	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}
	
	public PublicKey getPublicKey() {
		 
		 return keyPair.getPublic();
	}
	

	public PrivateKey  readPrivate() throws Exception {
		File privateKeyFile = new File("privateRSA.key");
		byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);       
		
		return	keyFactory.generatePrivate(privateKeySpec);	
	}
	
	public String encriptar(String texto) throws Exception {
		PrivateKey pk = this.readPrivate();
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, pk);
		byte[] encript =   encryptCipher.doFinal(texto.getBytes());
		String encoded = DatatypeConverter.printBase64Binary(encript);
		System.out.println("Texto cifrado:");
		System.out.println(encoded);
		return encoded;
	}
	
	public String desencriptar(String texto) throws Exception {
		PrivateKey pk = this.readPrivate();
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.DECRYPT_MODE, pk);
		byte[] decript =   encryptCipher.doFinal(texto.getBytes());
		String decoded = DatatypeConverter.printBase64Binary(decript);
		System.out.println("Texto descifrado:");
		System.out.println(decoded);
		return decoded;
	}

	public static void main(String args[]) throws Exception {
		Encriptar encript = new Encriptar();
		System.out.println("Encriptar...");
		encript.encriptar(args[0]);
	}

}