package com.example.tikitaka.domain.scrap.controller;

import com.example.tikitaka.domain.scrap.dto.response.ScrapListResponseDto;
import com.example.tikitaka.domain.scrap.dto.response.ViewsResponseDto;
import com.example.tikitaka.domain.scrap.service.ViewService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/views")
public class ViewController {
    private final ViewService viewService;

    @PostMapping("/{exhibitionId}")
    public void viewAdd(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId
    ) {
        viewService.addView(memberId, exhibitionId);
    }

    @DeleteMapping("/{exhibitionId}")
    public void viewDelete(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long exhibitionId
    ) {
        viewService.removeView(memberId, exhibitionId);
    }

    @GetMapping
    public ViewsResponseDto getMemberScraps(
            @AuthenticationPrincipal Long memberId, // 로그인된 멤버
            @RequestParam(defaultValue = "0") @Min(0) int pageNum,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int limit
    ) {
        // pageNum, limit을 받아 Service에서 PageRequest.of(pageNum-1, limit) 처리
        return viewService.findViewList(memberId, pageNum, limit);
    }


}
