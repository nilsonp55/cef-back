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
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

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

  public List<String> getObjectsFromPathS3(String path) {

    S3Client s3Client = S3Client.builder().region(Region.of(awsRegion)).build();

    ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName)
        .prefix(path).delimiter("/").maxKeys(999).build();

    ListObjectsV2Iterable listObjectsV2Iterable =
        s3Client.listObjectsV2Paginator(listObjectsV2Request);

    List<S3Object> objects;
    List<String> list = new ArrayList<>();
    for (ListObjectsV2Response page : listObjectsV2Iterable) {
      objects = page.contents();
      list.addAll(objects.stream().map(S3Object::key).toList());
    }

    String name;
    List<String> list2 = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {

      if (!list.get(i).equals(path)) {
        name = list.get(i).replace(path, "");
        list2.add(name);
      }
    }
    return list2;
  }

}
