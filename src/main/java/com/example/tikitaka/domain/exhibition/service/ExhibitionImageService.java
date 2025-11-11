package com.example.tikitaka.domain.exhibition.service;

import com.example.tikitaka.domain.exhibition.ExhibitionErrorCode;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionImagePatch;
import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPatchRequest;
import com.example.tikitaka.domain.exhibition.entity.ExhibitionImage;
import com.example.tikitaka.domain.exhibition.repository.ExhibitionImageRepository;
import com.example.tikitaka.global.exception.BaseErrorException;
import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionImageService {
    private final S3UrlHandler s3UrlHandler;
    private final ExhibitionImageRepository exhibitionImageRepository;

    public void updateExhibitionImage(List<ExhibitionImagePatch> exhibitionImagePatchs) {
        exhibitionImagePatchs
                .forEach(imagePatch -> {
                    ExhibitionImage exhibitionImage = exhibitionImageRepository.findById(imagePatch.getId())
                            .orElseThrow(() -> new BaseErrorException(ExhibitionErrorCode.EXHIBITION_IMAGE_NOT_FOUND));
                    if(imagePatch.getUrl().isPresent()) {
                        exhibitionImage.updateImageUrl(imagePatch.getUrl().get());
                    }

                    if (imagePatch.getSequence().isPresent()) {
                        exhibitionImage.updateSequence(imagePatch.getSequence().get());
                    }
                });

    }

    public S3Url getImageUploadUrl() {
        return s3UrlHandler.handle("exhibition/images");
    }
}
