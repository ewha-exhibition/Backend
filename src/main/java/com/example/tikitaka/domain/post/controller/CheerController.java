package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.entity.PostType;
import com.example.tikitaka.domain.post.service.PreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheers")
public class CheerController {
    private final PreviewService previewService;
    // TODO: 추후 유저 추가

    @PostMapping("/{exhibitionId}")
    public void addCheer(
            @PathVariable
            Long exhibitionId,
            PreviewPostRequest previewPostRequest) {
        previewService.addPreview(exhibitionId, previewPostRequest, PostType.CHEER);
    }



    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionCheerList(
            @PathVariable
            Long exhibitionId,
            int pageNum,
            int limit
    ) {
        return previewService.getExhibitionPreviews(exhibitionId, PostType.CHEER, pageNum, limit);
    }



}
