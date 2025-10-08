package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionImageService {
    private final S3UrlHandler s3UrlHandler;

    public S3Url getImageUploadUrl() {
        return s3UrlHandler.handle("exhibition/images");
    }
}
