package com.example.tikitaka.infra.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey")
    private String secretKey;

    @Bean
    public S3Presigner s3Presigner() {
        final AwsBasicCredentials awsBasicCredentials =
                AwsBasicCredentials.create(accessKey, secretKey);
        final StaticCredentialsProvider staticCredentialsProvider =
                StaticCredentialsProvider.create(awsBasicCredentials);
        return S3Presigner.builder()
                .credentialsProvider(staticCredentialsProvider)
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

}
