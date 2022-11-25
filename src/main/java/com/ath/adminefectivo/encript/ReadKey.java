package com.ath.adminefectivo.encript;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import java.io.*;
import java.nio.file.Files;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class ReadKey {

	public PublicKey  readPublic() throws Exception  {
			File publicKeyFile = new File("publicRSA.key");
			byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/OAEPWithMD5AndMGF1Padding");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			return	keyFactory.generatePublic(publicKeySpec);
	}
	
	public PrivateKey  readPrivate() throws Exception {
		File privateKeyFile = new File("privateRSA.key");
		byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/OAEPWithMD5AndMGF1Padding");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);       
		
		return	keyFactory.generatePrivate(privateKeySpec);	
	}

	public String leerPublic(String testing) throws Exception  {
		Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWithMD5AndMGF1Padding");
		decryptCipher.init(Cipher.DECRYPT_MODE, this.readPublic());	
		byte[] encryptedMessageBytes = DatatypeConverter.parseBase64Binary(testing);
		byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
	System.out.println("decryptedMessageBytes "+decryptedMessageBytes);
		return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
		
	}
	
	public String leerPrivate(String testing) throws Exception  {
		Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWithMD5AndMGF1Padding");
		decryptCipher.init(Cipher.DECRYPT_MODE, this.readPrivate());	
		byte[] encryptedMessageBytes = DatatypeConverter.parseBase64Binary(testing);
		byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
		System.out.println("decryptedMessageBytes   -- " +decryptedMessageBytes);
		return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
		
	}
	public static void main(String args[]) throws Exception {
		ReadKey rk = new ReadKey();
		System.out.println();
		System.out.println("El mensaje descifrado es:");
		System.out.println(rk.leerPublic(args[0]));
		System.out.println();
	}
}