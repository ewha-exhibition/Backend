package com.example.tikitaka.domain.post.controller;

import com.example.tikitaka.domain.post.dto.response.ExhibitionPostListResponse;
import com.example.tikitaka.domain.post.service.CheerService;
import com.example.tikitaka.infra.s3.S3Url;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheers")
public class CheerController {
    private final CheerService cheerService;
    // TODO: 응원 생성

    // TODO: 전시 응원 조회 (페이지네이션)
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
