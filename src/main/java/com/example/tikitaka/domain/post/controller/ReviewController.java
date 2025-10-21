package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.request.ReviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.service.PostImageService;
import com.example.tikitaka.domain.post.service.PostService;
import com.example.tikitaka.domain.post.service.ReviewService;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final PostImageService postImageService;
    private final PostService postService;

    @GetMapping("/images")
    public S3Url getPosterUploadUrl() {
        return postImageService.getImageUploadUrl("reviews/images");
    }

    // TODO: 질문 생성, 추후 유저 내용 추가
    @PostMapping("/{exhibitionId}")
    public void exhibitionReviewAdd(
            @RequestBody
            ReviewPostRequest reviewPostRequest,
            @PathVariable
            Long exhibitionId) {
        reviewService.addReview(exhibitionId, reviewPostRequest);
    }

    // TODO: 추후 유저 내용 추가
    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionReviewList(
            @PathVariable
            Long exhibitionId,
            @RequestParam(required = true)
            int pageNum,
            @RequestParam(required = true)
            int limit
    ) {
        return reviewService.getExhibitionReviews(exhibitionId, pageNum, limit);
    }

    @DeleteMapping("/{postId}")
    public void reviewDelete(@PathVariable Long postId) {
        postService.deletePost(postId);
    }


}
