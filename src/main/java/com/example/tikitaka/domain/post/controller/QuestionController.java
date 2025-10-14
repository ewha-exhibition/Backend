package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.post.service.PostService;
import com.example.tikitaka.domain.post.service.PreviewService;
import lombok.RequiredArgsConstructor;
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
            @PathVariable
            Long exhibitionId,
            @RequestBody
            PreviewPostRequest previewPostRequest) {
        previewService.addPreview(exhibitionId, previewPostRequest, PostType.QUESTION);
    }



    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionQuestionList(
            @PathVariable
            Long exhibitionId,
            int pageNum,
            int limit
    ) {
        return previewService.getExhibitionPreviews(exhibitionId, PostType.QUESTION, pageNum, limit);
    }

    @DeleteMapping("/{postId}")
    public void questionDelete(@PathVariable Long postId) {
        postService.deletePost(postId);
    }
}
