package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.post.service.PreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {
    private final PreviewService previewService;

    // TODO: 질문 생성


    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionQuestionList(
            @PathVariable
            Long exhibitionId,
            int pageNum,
            int limit
    ) {
        return previewService.getExhibitionPreviews(exhibitionId, PostType.QUESTION, pageNum, limit);
    }
}
