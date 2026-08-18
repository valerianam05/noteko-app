package com.example.demo.service.event;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
public class S3Service {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String bucketName;
  private final Long presignedUrlDurationSeconds;

  public S3Service(
      S3Client s3Client,
      S3Presigner s3Presigner,
      @Value("${aws.s3.bucket}") String bucketName,
      @Value("${aws.s3.presigned-url-duration:86400}") Long presignedUrlDurationSeconds) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
    this.bucketName = bucketName;
    this.presignedUrlDurationSeconds = presignedUrlDurationSeconds;
  }

  public String uploadFileAndGenerateUrl(
      byte[] fileContent, String fileName, String entityId, String contentType) {
    try {
      String key = generateObjectKey(entityId, fileName);

      PutObjectRequest putRequest =
          PutObjectRequest.builder()
              .bucket(bucketName)
              .key(key)
              .contentType(contentType)
              .metadata(
                  Map.of(
                      "entityId", entityId,
                      "fileName", fileName,
                      "uploadedAt", String.valueOf(System.currentTimeMillis())))
              .build();

      s3Client.putObject(
          putRequest,
          RequestBody.fromInputStream(new ByteArrayInputStream(fileContent), fileContent.length));

      log.info("File uploaded to S3: {}", key);

      Duration duration = Duration.ofSeconds(presignedUrlDurationSeconds);

      GetObjectPresignRequest presignRequest =
          GetObjectPresignRequest.builder()
              .signatureDuration(duration)
              .getObjectRequest(GetObjectRequest.builder().bucket(bucketName).key(key).build())
              .build();

      PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
      String presignedUrl = presignedRequest.url().toString();

      log.info("Pre-signed URL generated, valid for {} seconds", presignedUrlDurationSeconds);

      return presignedUrl;

    } catch (S3Exception e) {
      log.error("S3 error: {}", e.getMessage());
      throw new RuntimeException("Failed to upload file to S3", e);
    } catch (Exception e) {
      log.error("Error: ", e);
      throw new RuntimeException("Failed to generate file link", e);
    }
  }

  private String generateObjectKey(String entityId, String fileName) {
    String cleanFileName = fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
    return String.format("exports/%s/%s_%s", entityId, System.currentTimeMillis(), cleanFileName);
  }
}
