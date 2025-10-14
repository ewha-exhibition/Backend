package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.request.PreviewPostRequest;
import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.service.CheerService;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheers")
public class CheerController {
    private final CheerService cheerService;
    // TODO: 추후 유저 추가

    @PostMapping("/{exhibitionId}")
    public void addCheer(
            @PathVariable
            Long exhibitionId,
            PreviewPostRequest previewPostRequest) {
        cheerService.addCheer(exhibitionId, previewPostRequest);
    }



    @GetMapping("/{exhibitionId}")
    public ExhibitionPostListResponse exhibitionCheerList(
            @PathVariable
            Long exhibitionId,
            int pageNum,
            int limit
    ) {
        return cheerService.getExhibitionCheers(exhibitionId, pageNum, limit);
    }



}
