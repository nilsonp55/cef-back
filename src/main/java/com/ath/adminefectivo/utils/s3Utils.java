package com.ath.adminefectivo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase para generar funcionalidades con respect al s3 del PP
 * @author Bayron Perez
 */
public class s3Utils {

	private String bucketNameFormat;

	private AmazonS3 s3;

	
	/**
	 * Metodo encargado de realizar la conexion con AWS s3 antes de realiar cualquier ejecución
	 * Version prueba #1
	 * @author Bayron Perez
	 */
	public void conexionS3(String bucketName) {

		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct " +
							"location (~/.aws/credentials), and is in valid format.",
							e);
		}

		s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion("us-east-1")
				.build();

		bucketNameFormat = bucketName + UUID.randomUUID();

		System.out.println("===========================================");
		System.out.println("Se logro establecer conexion con Amazon S3");
		System.out.println("===========================================\n");
	}


	/**
	 * Metodo encargado de cargar un archivo dentro del repositorio S3
	 * @param file
	 */
	public void updateFileOption2(String bucketName, File file, String key) {
		System.out.println("Uploading a new object to S3 from a file\n");
		try {
			s3.putObject(new PutObjectRequest(bucketName, key, file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Metodo encargado de cargar un objeto dentro del bucket S3
	 * @param file
	 * @throws IOException 
	 */
	public void downloadFile(String bucketName, File file, String key) throws IOException {
		System.out.println("Descargando archivos del bucket");
		try {
			S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
			System.out.println("Contenido descargado: "  + object.getObjectMetadata().getContentType());
			displayTextInputStream(object.getObjectContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * Metodo encargado de cargar una lista de objetos dentro del bucket S3
	 * @param file
	 * @throws IOException 
	 */
	public void downloadFiles(String bucketName, String prefijo) throws IOException {
		try {
			System.out.println("Listing objects");
			ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
					.withBucketName(bucketName)
					.withPrefix(prefijo));
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				System.out.println(" - " + objectSummary.getKey() + "  " +
						"(size = " + objectSummary.getSize() + ")");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Metodo encargado de eliminar un objeto dentro del bucket S3
	 * @param file
	 * @throws IOException 
	 */
	public void deleteFile(String bucketName, String key) throws IOException {
		try {
			System.out.println("Borrando el objeto\n");
            s3.deleteObject(bucketName, key);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo encargado de validar...
	 * @param input
	 * @throws IOException
	 */
	private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null) break;

			System.out.println("    " + line);
		}
		System.out.println();
	}
	
}
