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
			File publicKeyFile = new File("key.public");
			byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			return	keyFactory.generatePublic(publicKeySpec);
	}

public String leer(String testing) throws Exception  {
	Cipher decryptCipher = Cipher.getInstance("RSA");
	decryptCipher.init(Cipher.DECRYPT_MODE, this.readPublic());	
	byte[] encryptedMessageBytes = DatatypeConverter.parseBase64Binary(testing);
	byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
		
		
		
		return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
	
}
	public static void main(String args[]) throws Exception {
		ReadKey rk = new ReadKey();
		System.out.println();
		System.out.println("El mensaje descifrado es:");
		System.out.println(rk.leer(args[0]));
		System.out.println();
	}
}