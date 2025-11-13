package com.example.tikitaka.domain.exhibition.controller;

import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPatchRequest;
import com.example.tikitaka.global.context.CurrentUserContext;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.request.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.dto.response.ExhibitionDetailResponse;
import com.example.tikitaka.domain.exhibition.service.ExhibitionImageService;
import com.example.tikitaka.domain.exhibition.service.ExhibitionService;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.global.annotation.OptionalAuth;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibition")
@Slf4j
public class ExhibitionController {
    private final ExhibitionService exhibitionService;
    private final ExhibitionImageService exhibitionImageService;

    @PostMapping
    public void exhibitionAdd(
            @AuthenticationPrincipal Long memberId,
            @RequestBody ExhibitionPostRequest request
    ) {
        exhibitionService.addExhibition(memberId, request);
    }

    @OptionalAuth
    @GetMapping("/{exhibitionId}")
    public ExhibitionDetailResponse exhibitionDetail(
            @PathVariable
            Long exhibitionId
    ){
        Long memberId = CurrentUserContext.getMemberId();
        return exhibitionService.findExhibition(memberId, exhibitionId);
    }

    @DeleteMapping("/{exhibitionId}")
    public void exhibitionDelete(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId
    ) {
        exhibitionService.deleteExhibition(memberId, exhibitionId);
    }

    @PatchMapping("/{exhibitionId}")
    public void exhibitionUpdate(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId,
            @RequestBody ExhibitionPatchRequest request
    ) {
        exhibitionService.updateExhibition(memberId, exhibitionId, request);
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
