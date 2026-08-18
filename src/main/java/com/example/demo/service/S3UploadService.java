package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3UploadService {

  @Value("${app.s3.bucket}")
  private String bucketName;

  private final S3Client s3Client;

  public S3UploadService(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public void upload(byte[] content, String key, String contentType) {
    s3Client.putObject(
        PutObjectRequest.builder().bucket(bucketName).key(key).contentType(contentType).build(),
        RequestBody.fromBytes(content));
  }
}
