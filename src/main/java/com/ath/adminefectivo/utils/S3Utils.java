package com.ath.adminefectivo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;

import lombok.extern.log4j.Log4j2;

/**
 * Clase para generar funcionalidades con respect al s3 del PP
 * 
 * @author Bayron Perez
 */
@Service
@Log4j2
public class S3Utils {

	@Autowired
	private AmazonS3 s3;

	@Value("${aws.s3.bucket}")
	private String bucketName;


	/**
	 * upload file
	 * 
	 * @Miller Caro
	 */
	public void uploadFile(MultipartFile file, String keyName) {
		try {
			File mainFile = new File(file.getOriginalFilename());
			s3.putObject(bucketName, keyName, mainFile);
		} catch (AmazonServiceException e) {
			throw new NegocioException(ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getHttpStatus());
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getHttpStatus());
		}

	}

	/**
	 * Lista las keys de todos los objetos del bucket
	 * 
	 * @return
	 */
	public List<String> getObjectsFromS3() {
		ListObjectsV2Result result = s3.listObjectsV2(bucketName);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		return objects.stream().map(item -> 
			item.getKey()
		).collect(Collectors.toList());
	}

	/**
	 * Trae una lista los archivos de una carpeta en especifico
	 * Recibe una ruta donde se encuentra el archivo
	 * Nota: el metodo de conexionS3(bucketName) solo se debe de llamar cuando son pruebas locales, para despliegues en el Fargate toca comentarla
	 * @param path
	 * @return
	 */
	public List<String> getObjectsFromPathS3(String path) {
		ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(path)
				.withDelimiter("/");
		ListObjectsV2Result listing = s3.listObjectsV2(req);
		List<S3ObjectSummary> objects = listing.getObjectSummaries();
		List<String> list = objects.stream().map(item -> 
			item.getKey()
		).collect(Collectors.toList());
		List<String> list2 = new ArrayList<>();
		String name;
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals(path.substring(0, path.length()))) {
				name = list.get(i).replace(path, "");
				list2.add(name);
			}
		}
		return list2;
	}

	/**
	 * Lista los arcivos del bucket
	 * 
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public InputStream downloadFile(String key) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		S3Object object;
		try {
			object = s3.getObject(bucketName, key);
			InputStream is = object.getObjectContent();
			while ((len = is.read(buffer, 0, buffer.length)) > -1 ) { 
				  baos.write(buffer, 0, len); 
			} 
			baos.flush();
		} catch (AmazonServiceException e) {
			throw new NegocioException(ApiResponseCode.ERROR_LECTURA_CARGUE_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_LECTURA_CARGUE_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_LECTURA_CARGUE_ARCHIVO.getHttpStatus());
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		} catch (Throwable t) {
			throw new NegocioException(ApiResponseCode.ERROR_LECTURA_CARGUE_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_LECTURA_CARGUE_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_LECTURA_CARGUE_ARCHIVO.getHttpStatus());
		}
		InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
		object.close();
		return is1;
	}
	
	/**
	 * Lista los arcivos del bucket
	 * 
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public Boolean consultarArchivo(String key) throws IOException {
		Boolean salida = true;
		S3Object object = null;
		try {
			object = s3.getObject(bucketName, key);
			}
		catch (Exception e) {
			salida = false;
		}
		if(!Objects.isNull(object)){
			object.close();
		}
		return salida;
	}
	

	/**
	 * Metodo para mover un objeto de un bucket S3
	 * 
	 * @param origenBucket
	 * @param destinationBucket
	 * @param keyOrigin
	 * @param keyDestination
	 */
	public void moverObjeto(String keyOrigin, String keyDestination) {
		try {

			s3.copyObject(bucketName, keyOrigin, bucketName, keyDestination);
			deleteObjectBucket(keyOrigin);
		} catch (AmazonServiceException e) {
			throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
					ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
					ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
		}

	}

	/**
	 * Metodo que elimina un objeto de un bucket
	 * 
	 * @param objectKey
	 */
	public void deleteObjectBucket(String objectKey) {
		try {
			s3.deleteObject(bucketName, objectKey);
		} catch (AmazonServiceException e) {
			throw new NegocioException(ApiResponseCode.ERROR_ELIMINAR_ARCHIVO_FISICO.getCode(),
					ApiResponseCode.ERROR_ELIMINAR_ARCHIVO_FISICO.getDescription(),
					ApiResponseCode.ERROR_ELIMINAR_ARCHIVO_FISICO.getHttpStatus());
		}
	}
	
	public void guardarArchivoEnBytes(ByteArrayOutputStream archivo, String key, String nombreArchivo) {
		
		try {
			String pathArchivo = key+nombreArchivo;
			File archivoFile = new File(pathArchivo);			
			FileUtils.writeByteArrayToFile (archivoFile, archivo.toByteArray());

			s3.putObject(bucketName, pathArchivo, archivoFile);
		} catch (AmazonServiceException e) {
			throw new NegocioException(ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getHttpStatus());
		} catch (Exception e) {
			throw new NegocioException(ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getHttpStatus());
		}
		
	}
	
	public void convertAndSaveArchivoEnBytes(MultipartFile archivo, String key, String nombreArchivo) {
		log.info("file to convert: {}", nombreArchivo);
		FileOutputStream fos = null;
		try {
			
			String pathArchivo = key+nombreArchivo;
			File file = new File(pathArchivo);
			file.createNewFile();
			fos = new FileOutputStream(new File(pathArchivo));
			byte[] bytearr = archivo.getBytes();
			log.debug("byte length: {} - Size: {}", bytearr.length, archivo.getSize());			
			fos.write(archivo.getBytes());			
			s3.putObject(bucketName, pathArchivo, file);
			
		} catch (SdkClientException | IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_GUARDANDO_ARCHIVO.getHttpStatus());
		} finally {
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					log.error("closed file: {}", e.getMessage());
				}
		}
	}
}
