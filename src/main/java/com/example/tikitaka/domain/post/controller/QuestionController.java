package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.post.service.PostService;
import com.example.tikitaka.domain.post.service.PreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {
    private final PreviewService previewService;
    private final PostService postService;

    // TODO: 질문 생성
    @PostMapping("/{exhibitionId}")
    public void questionAdd(
            @AuthenticationPrincipal String memberId,
            @PathVariable
            Long exhibitionId,
            @RequestBody
            PreviewPostRequest previewPostRequest) {
        previewService.addPreview(memberId, exhibitionId, previewPostRequest, PostType.QUESTION);
    }



    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionQuestionList(
            @AuthenticationPrincipal
            String memberId,
            @PathVariable
            Long exhibitionId,
            @RequestParam(required = true)
            int pageNum,
            @RequestParam(required = true)
            int limit
    ) {
        return previewService.getExhibitionPreviews(memberId, exhibitionId, PostType.QUESTION, pageNum, limit);
    }

    @DeleteMapping("/{postId}")
    public void questionDelete(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long postId) {
        postService.deletePost(memberId, postId);
    }
}
