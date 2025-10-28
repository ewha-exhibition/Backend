package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.request.ReviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.service.PostImageService;
import com.example.tikitaka.domain.post.service.PostService;
import com.example.tikitaka.domain.post.service.ReviewService;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/{exhibitionId}")
    public void exhibitionReviewAdd(
            @AuthenticationPrincipal
            String memberId,
            @RequestBody
            ReviewPostRequest reviewPostRequest,
            @PathVariable
            Long exhibitionId) {
        reviewService.addReview(memberId, exhibitionId, reviewPostRequest);
    }


    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionReviewList(
            @AuthenticationPrincipal
            String memberId,
            @PathVariable
            Long exhibitionId,
            @RequestParam(required = true)
            int pageNum,
            @RequestParam(required = true)
            int limit
    ) {
        return reviewService.getExhibitionReviews(memberId, exhibitionId, pageNum, limit);
    }

    @DeleteMapping("/{postId}")
    public void reviewDelete(@PathVariable Long postId) {
        postService.deletePost(postId);
    }


}
