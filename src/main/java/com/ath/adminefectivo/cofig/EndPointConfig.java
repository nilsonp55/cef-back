package com.ath.adminefectivo.cofig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@PropertySource("classpath:endpoint.properties")
public class EndPointConfig   {

	@Value("${aws.s3.region}")
	private String region;
	
	@Value("${aws.s3.bucket}")
	private String bucketName;

    @Bean
    AmazonS3 getS3Client() {
        AmazonS3 client =
                AmazonS3ClientBuilder.standard()
                        .withRegion("us-east-1") // The first region to try your request against
                        .build();
        return client;
    }
}