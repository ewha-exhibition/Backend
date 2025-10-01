package com.example.tikitaka.domain.exhibition.controller;

import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionDetailResponse;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.service.ExhibitionImageService;
import com.example.tikitaka.domain.exhibition.service.ExhibitionService;
import com.example.tikitaka.infra.s3.S3Url;
import com.example.tikitaka.infra.s3.S3UrlHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;
    private final ExhibitionImageService exhibitionImageService;

    @PostMapping
    public void exhibitionAdd(
            @RequestBody ExhibitionPostRequest request
    ) {
        exhibitionService.addExhibition(request);
    }

    @GetMapping("/{exhibitionId}")
    public ExhibitionDetailResponse exhibitionDetail(
            // 로그인 기능 추가 후 유저 정보 받기
            @PathVariable
            Long exhibitionIdx
    ){
        return exhibitionService.findExhibtion(exhibitionIdx);
    }

    @DeleteMapping("/{exhibitionId}")
    public void exhibitionDelete(
            // 로그인 기능 추가 후 유저 정보 받기
            @PathVariable Long exhibitionId
    ) {
        exhibitionService.deleteExhibition(exhibitionId);
    }

    @PatchMapping("/{exhibitionId}")
    public void exhibitionUpdate(
            // 로그인 기능 추가 후 유저 정보 받기
            @PathVariable Long exhibitionId,
            @RequestBody ExhibitionCreate request
    ) {
        exhibitionService.updateExhibition(exhibitionId, request);
    }

    @GetMapping("/posters")
    public S3Url getPosterUploadUrl() {
        return exhibitionService.getImageUploadUrl();
    }

    @GetMapping("/images")
    public S3Url getImagesUploadUrl() {
        return exhibitionImageService.getImageUploadUrl();
    }


}
