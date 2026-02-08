package com.example.tikitaka.domain.scrap.controller;

import com.example.tikitaka.domain.scrap.service.ViewService;
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


}
