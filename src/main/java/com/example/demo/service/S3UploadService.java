package com.example.demo.service;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class S3UploadService {

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.presigned-url-duration}")
  private long presignedUrlDurationSeconds;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public S3UploadService(S3Client s3Client, S3Presigner s3Presigner) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
  }

  public void upload(byte[] content, String key, String contentType) {
    s3Client.putObject(
        PutObjectRequest.builder().bucket(bucketName).key(key).contentType(contentType).build(),
        RequestBody.fromBytes(content));
  }

  public String generatePresignedUrl(String key) {
    GetObjectPresignRequest presignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(presignedUrlDurationSeconds))
            .getObjectRequest(GetObjectRequest.builder().bucket(bucketName).key(key).build())
            .build();

    return s3Presigner.presignGetObject(presignRequest).url().toString();
  }
}
