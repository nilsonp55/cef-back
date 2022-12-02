package com.ath.adminefectivo.encript;
import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Autowired;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.impl.ParametroServiceImpl;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Anass AIT BEN EL ARBI
 * <ul>
 *     <li>AES/CBC/NoPadding (128)</li>
 *     <li>AES/CBC/PKCS5Padding (128)</li>
 *     <li>AES/ECB/NoPadding (128)</li>
 *     <li>AES/ECB/PKCS5Padding (128)</li>
 *     <li>RSA/ECB/PKCS1Padding (1024, 2048)</li>
 *     <li>RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)</li>
 *     <li>RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)</li>
 * </ul>
 * <p>
 * for more details @see <a href="https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html">Java Ciphers</a>
 */

public class RSA {

	private final IParametroService parametroService;
	
    private PrivateKey privateKey = null;
    private PublicKey publicKey = null;
    
    private String instanceAlgorith = "";

    public RSA(IParametroService parametroService) {
        this.parametroService = parametroService;
        this.setKeys();
    }

    private boolean existsKeys() {
		// TODO Auto-generated method stub
		return false;
	}

	public void createKeys() {
    	try {
    		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    		int bytesEncript = parametroService.valorParametroEntero(Constantes.BYTES_RSA);
        	generator.initialize(bytesEncript);
        	KeyPair pair = generator.genKeyPair();// generateKeyPair();
        	
        	PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            
            this.privateKey = privateKey;
            this.publicKey = publicKey;
            
            this.saveKeysInBD();
            			
            
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
		
	}

	private void saveKeysInBD() {
		String privateKeyS = this.getPrivateKeyString();
		String publicKeysS = this.getPublicKeyString();
		
		if(!parametroService.actualizarValorParametro(Constantes.PARAMETRO_PRIVATE_KEY_RSA, privateKeyS)) {
			System.err.println("Ocurrio un fallo al insertar la llave privada de RSA");
		}
		
		if(!parametroService.actualizarValorParametro(Constantes.PARAMETRO_PUBLIC_KEY_RSA, publicKeysS)) {
			System.err.println("Ocurrio un fallo al insertar la llave publica de RSA");
		}
		String nombreArchivoLLavePublica = parametroService.valorParametro(Constantes.NAME_PUBLIC_KEY_RSA);
		String nombreArchivoLLavePrivada = parametroService.valorParametro(Constantes.NAME_PRIVATE_KEY_RSA);
		try {
			BufferedWriter archivoPublico = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivoLLavePublica)));
			BufferedWriter archivoPrivado = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivoLLavePrivada)));
			
			archivoPublico.write(publicKeysS);
			archivoPrivado.write(privateKeyS);
			
			archivoPublico.close();
			archivoPrivado.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String encrypt(String message) throws Exception{
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance(instanceAlgorith);
        cipher.init(Cipher.ENCRYPT_MODE,this.publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }
    private String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    public String decrypt(String encryptedMessage) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance(instanceAlgorith);
        cipher.init(Cipher.DECRYPT_MODE,this.privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage,"UTF8");
    }
    private byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    public static void main(String[] args) {
        RSA rsa = new RSA(new ParametroServiceImpl());
        try{
            String encryptedMessage = rsa.encrypt("Hello World");
            String decryptedMessage = rsa.decrypt(encryptedMessage);

            System.err.println("Encrypted:\n"+encryptedMessage);
            System.err.println("Decrypted:\n"+decryptedMessage);
        }catch (Exception ingored){}
    }
    
    private void getKeys(){
    	String privateKeyString = parametroService.valorParametro("privateKeyRSA");
    	String publicKeyString = parametroService.valorParametro("publicKeyRSA");
    	
    	//String privada = privateKey.getEncoded();	
    	
    }
    
    public String getPrivateKeyString() {
    	PKCS8EncodedKeySpec encodedKey = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
    	return bytesToString(encodedKey.getEncoded());
    }
    
    public String getPublicKeyString() {
    	X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(this.publicKey.getEncoded());
    	return bytesToString(encodedPublicKey.getEncoded());
    }
    
    public void setKeys() {
    	String privateKeyString = parametroService.valorParametro(Constantes.PARAMETRO_PRIVATE_KEY_RSA);
    	String publicKeyString = parametroService.valorParametro(Constantes.PARAMETRO_PUBLIC_KEY_RSA);
    	
    	instanceAlgorith = parametroService.valorParametro(Constantes.INSTANCIA_RSA);
    	
    	try {
			this.setPrivateKeyString(privateKeyString);
			this.setPublicKeyString(publicKeyString);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    public void setPrivateKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] encodedPrivateKey = stringToBytes(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        this.privateKey = privateKey;
    }

    public void setPublicKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] encodedPublicKey = stringToBytes(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        this.publicKey = publicKey;
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
