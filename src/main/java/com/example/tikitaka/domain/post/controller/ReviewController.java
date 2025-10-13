package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.service.ReviewImageService;
import com.example.tikitaka.domain.post.service.ReviewService;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @GetMapping("/images")
    public S3Url getPosterUploadUrl() {
        return reviewImageService.getImageUploadUrl("reviews/images");
    }

    // TODO: 질문 생성

    // TODO: 특정 전시 질문 상세 (페이지네이션)


}
