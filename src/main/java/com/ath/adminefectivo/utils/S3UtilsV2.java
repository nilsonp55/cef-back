package com.ath.adminefectivo.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

@Service
@Log4j2
public class S3UtilsV2 {

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.region}")
  private String awsRegion;

  public List<String> getObjectsFromPathS3V2(String path) {
    List<String> list2 = new ArrayList<>();

    S3Client s3Client = S3Client.builder().region(Region.of(awsRegion)).build();
    String nextContinuationToken = null;
    long totalObjects = 0;

    do {
      ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
          .bucket(bucketName).continuationToken(nextContinuationToken);

      ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
      nextContinuationToken = response.nextContinuationToken();

      totalObjects += response.contents().stream().peek(System.out::println).reduce(0,
          (subtotal, element) -> subtotal + 1, Integer::sum);
    } while (nextContinuationToken != null);

    log.info("Number of objects in the bucket: {}", totalObjects);

    s3Client.close();

    return list2;
  }

}
