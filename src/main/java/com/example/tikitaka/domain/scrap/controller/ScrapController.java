package com.example.tikitaka.domain.scrap.controller;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.scrap.dto.response.ScrapListResponseDto;
import com.example.tikitaka.domain.scrap.service.ScrapService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
@Validated
public class ScrapController {

    private final ScrapService scrapService;


    /**
     * 1. 스크랩 목록 조회
     * - 쿼리 파라미터:
     *   - pageNum: 1부터 시작(기본 1)
     *   - limit  : 페이지당 개수(기본 10)
     * - 정렬:
     *   - 1순위 endDate(전시 종료일) 오름차순 - 2순위 isViewed(관람 여부) 오름차순(false 먼저)
     * - 반환:
     *   - username, exhibitions[], pageInfo { pageNum, limit, totalPages, totalElements }
     */
    @GetMapping
    public ScrapListResponseDto getMemberScraps(
            @AuthenticationPrincipal Member member, // 로그인된 멤버
            @RequestParam(defaultValue = "1") @Min(1) int pageNum,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int limit
    ) {
        // pageNum, limit을 받아 Service에서 PageRequest.of(pageNum-1, limit) 처리
        return scrapService.findScrapList(member.getMemberId(), pageNum, limit);
    }



}
