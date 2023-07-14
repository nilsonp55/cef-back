package com.ath.adminefectivo.encript;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IParametroService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Anass AIT BEN EL ARBI
 *         <ul>
 *         <li>AES/CBC/NoPadding (128)</li>
 *         <li>AES/CBC/PKCS5Padding (128)</li>
 *         <li>AES/ECB/NoPadding (128)</li>
 *         <li>AES/ECB/PKCS5Padding (128)</li>
 *         <li>RSA/ECB/PKCS1Padding (1024, 2048)</li>
 *         <li>RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)</li>
 *         <li>RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)</li>
 *         </ul>
 *         <p>
 *         for more details @see <a href=
 *         "https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html">Java
 *         Ciphers</a>
 */

@Component
@Log4j2
public class AES256 {

	private	IParametroService parametroService;

	private PrivateKey privateKey = null;
	private PublicKey publicKey = null;

	private String secretKey;
	private String saltValue;

	private String instanceAlgorith = "AES/GCM/NoPadding";
	
	private SecureRandom random = new SecureRandom();

	public AES256(IParametroService parametroService) {
		this.parametroService = parametroService;
		this.secretKey = parametroService.valorParametro(Parametros.VALUE_SECRET_KEY);
		this.saltValue = parametroService.valorParametro(Parametros.SALT_VALUE);
	}

	public String encryptAES(String text) {
		try {
			/* Declare a byte array. */
			byte[] iv = new byte[16];
			random.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			/* Create factory for secret keys. */
			SecretKeyFactory factory = SecretKeyFactory.getInstance(parametroService.valorParametro(Parametros.AES256));

			/* PBEKeySpec class implements KeySpec interface. */
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltValue.getBytes(), 100000,
					parametroService.valorParametroEntero(Parametros.BYTES_AES));// BYTES_AES ALGORITMO_AES

			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance(parametroService.valorParametro(Parametros.INSTANCIA_AES));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

			/* Retruns encrypted value. */
			return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException
				| NoSuchPaddingException e) {
			log.error("Falied to encrypt: {}", e);
		}
		return null;
	}

	public String decryptAES(String text) {
		try {
			/* Declare a byte array. */
			byte[] iv = new byte[16];
			random.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			/* Create factory for secret keys. */
			SecretKeyFactory factory = SecretKeyFactory.getInstance(parametroService.valorParametro(Parametros.AES256));
			
			/* PBEKeySpec class implements KeySpec interface. */
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltValue.getBytes(), 100000, parametroService.valorParametroEntero(Parametros.BYTES_AES));
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance(parametroService.valorParametro(Parametros.INSTANCIA_AES));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			
			/* Retruns decrypted value. */
			return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
				| InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException
				| NoSuchPaddingException e) {
			log.error("Falied to decrypt: {}", e);
		}
		return null;
	}
		

	/**
	 * Metodo encargado de realizar la creación de las llaves tanto publicas como
	 * privadas, las cuales se crean con los bytes que este parametro se encuentra
	 * en la tabla parametros.
	 * 
	 * @author prv_dnaranjo
	 */
	public void createKeys() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			int bytesEncript = parametroService.valorParametroEntero(Constantes.BYTES_RSA);
			generator.initialize(bytesEncript);
			KeyPair pair = generator.genKeyPair();

			this.privateKey = pair.getPrivate();
			this.publicKey = pair.getPublic();

			this.saveKeys();

		} catch (Exception e) {
			log.error("failed to createKeys: {}", e);
		}

	}

	/**
	 * metodo encargado de realizar el guardado de las llaves generadas en el metodo
	 * createKeys() en el presente metodo, se encarga de almacenar las llaves en la
	 * base de datos y en un archivo el cual se crea en la raiz del proyecto.
	 * 
	 * @author prv_dnaranjo
	 */
	private void saveKeys() {
		String privateKeyS = this.getPrivateKeyString();
		String publicKeysS = this.getPublicKeyString();

		if (!parametroService.actualizarValorParametro(Constantes.PARAMETRO_PRIVATE_KEY_RSA, privateKeyS)) {
			throw new NegocioException(ApiResponseCode.ERROR_INSERTANDO_LLAVE_PRIVADA_RSA.getCode(),
					ApiResponseCode.ERROR_INSERTANDO_LLAVE_PRIVADA_RSA.getDescription(),
					ApiResponseCode.ERROR_INSERTANDO_LLAVE_PRIVADA_RSA.getHttpStatus());
		}

		if (!parametroService.actualizarValorParametro(Constantes.PARAMETRO_PUBLIC_KEY_RSA, publicKeysS)) {
			throw new NegocioException(ApiResponseCode.ERROR_INSERTANDO_LLAVE_PUBLICA_RSA.getCode(),
					ApiResponseCode.ERROR_INSERTANDO_LLAVE_PUBLICA_RSA.getDescription(),
					ApiResponseCode.ERROR_INSERTANDO_LLAVE_PUBLICA_RSA.getHttpStatus());
		}
		String nombreArchivoLLavePublica = parametroService.valorParametro(Constantes.NAME_PUBLIC_KEY_RSA);
		try(BufferedWriter archivoPublico = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(nombreArchivoLLavePublica))) ) {
			archivoPublico.write(publicKeysS);
		} catch (IOException e) {
			log.error("Guardando archivo publico: {}", e.getMessage());
		}

	}

	/**
	 * Metodo encargado de realizar la encriptacion de un texto basado en un
	 * algoritmo que se encuentra alojado en base de datos en la tabla parametros
	 * ademas de utilizar la llave privada creada anteriormente
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 * @author prv_dnaranjo
	 */
	public String encrypt(String message) throws Exception {
		byte[] messageToBytes = message.getBytes();
		Cipher cipher = Cipher.getInstance(instanceAlgorith);
		cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
		byte[] encryptedBytes = cipher.doFinal(messageToBytes);
		return encode(encryptedBytes);
	}

	private String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	/**
	 * Metodo encargado de realizar la desencriptacion de un texto encriptado basado
	 * en un algoritmo que se encuentra alojado en base de datos en la tabla
	 * parametros ademas de utilizar la llave privada creada anteriormente
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 * @author prv_dnaranjo
	 */
	public String decrypt(String encryptedMessage) throws Exception {
		byte[] encryptedBytes = decode(encryptedMessage);
		Cipher cipher = Cipher.getInstance(instanceAlgorith);
		cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
		byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
		return new String(decryptedMessage, StandardCharsets.UTF_8);
	}

	private byte[] decode(String data) {
		return Base64.getDecoder().decode(data);
	}

	public String getPrivateKeyString() {
		PKCS8EncodedKeySpec encodedKey = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
		return bytesToString(encodedKey.getEncoded());
	}

	public String getPublicKeyString() {
		X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(this.publicKey.getEncoded());
		return bytesToString(encodedPublicKey.getEncoded());
	}

	public void setPrivateKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] encodedPrivateKey = stringToBytes(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		this.privateKey = keyFactory.generatePrivate(privateKeySpec);
		
	}

	public void setPublicKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] encodedPublicKey = stringToBytes(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		this.publicKey = keyFactory.generatePublic(publicKeySpec);
	}

	public String bytesToString(byte[] b) {
		byte[] b2 = new byte[b.length + 1];
		b2[0] = 1;
		System.arraycopy(b, 0, b2, 1, b.length);
		return new BigInteger(b2).toString(36);
	}

	public byte[] stringToBytes(String s) {
		byte[] b2 = new BigInteger(s, 36).toByteArray();
		return Arrays.copyOfRange(b2, 1, b2.length);
	}
}
