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
@RequestMapping("/scraps")
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
            @AuthenticationPrincipal Long memberId, // 로그인된 멤버
            @RequestParam(defaultValue = "1") @Min(0) int pageNum,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int limit
    ) {
        // pageNum, limit을 받아 Service에서 PageRequest.of(pageNum-1, limit) 처리
        return scrapService.findScrapList(memberId, pageNum, limit);
    }

    /**
     * 2. 스크랩 추가
     * - 이미 존재하면 무시
     * - 성공 시 204 No Content
     */
    @PostMapping("/{exhibitionId}")
    public void addScrap(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId
    ) {
        scrapService.addScrap(memberId, exhibitionId);
    }

    /**
     * 3. 스크랩 취소
     * - 존재하지 않아도 무시(idempotent)
     * - 성공 시 204 No Content
     */
    @DeleteMapping("/{exhibitionId}")
    public void removeScrap(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId
    ) {
        scrapService.removeScrap(memberId, exhibitionId);
    }

    /**
     * 4.1. 관람 표시 / 해제
     * - PATCH /scraps/{exhibitionId}/viewed?viewed=true
     * - 스크랩된 전시만 가능
     * - 성공 시 204 No Content
     */
    @PatchMapping("/{exhibitionId}/viewed")
    public void markViewed(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId,
            @RequestParam(defaultValue = "true") boolean viewed
    ) {
        scrapService.markViewed(memberId, exhibitionId, viewed);
    }
    /**
     * 4.2. 관람 표시/해제 (스크랩된 전시만 가능)
     * - PATCH /scraps/{exhibitionId}/viewed?viewed=true|false
     * - 스크랩하지 않은 전시를 관람표시하려 하면 400/404(서비스에서 예외) 발생
     */

    /**
     * 5. 스크랩 전시 중 관람한 전시 조회
     * - 존재하지 않아도 무시(idempotent)
     * - 성공 시 204 No Content
     */
    @GetMapping("/viewed")
    public ScrapListResponseDto getViewedScraps(
            @AuthenticationPrincipal  Long memberId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return scrapService.findScrapListByViewed(memberId, true, page, limit);
    }








}
