package com.ath.adminefectivo.cofig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import software.amazon.awssdk.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@PropertySource("classpath:endpoint.properties")
public class EndPointConfig   {

	/*@Value("${aws.access_key_id}")
	private String accessKeyId;

	/*@Value("${aws.secret_access_key}")
	private String accessSecretKey;*/

	@Value("${aws.s3.region}")
	private String region;
	
	@Value("${aws.s3.bucket}")
	private String bucketName;


	
	
	@Bean
	public AmazonS3 getS3Client() {
		AmazonS3 client =
        AmazonS3ClientBuilder.standard()
                             .withRegion("us-east-1") // The first region to try your request against
							 .build();
		return client;

	}

}
