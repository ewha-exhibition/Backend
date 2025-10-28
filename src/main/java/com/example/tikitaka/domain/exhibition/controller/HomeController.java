package com.example.tikitaka.domain.exhibition.controller;

import com.example.tikitaka.domain.exhibition.dto.response.ExhibitionListResponse;
import com.example.tikitaka.domain.exhibition.dto.response.PopularExhibitionResponse;
import com.example.tikitaka.domain.exhibition.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibitions")
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/latest")
    public ExhibitionListResponse recentExhibitionList(
            @AuthenticationPrincipal
            String memberId,
            @RequestParam(required = false)
            String category,
            @RequestParam(required = true)
            int pageNum,
            @RequestParam(required = true)
            int limit
    ){
        return homeService.findRecentExhibition(memberId, category, pageNum, limit);
    }

    @GetMapping("/search")
    public ExhibitionListResponse searchExhibitionList(
            @AuthenticationPrincipal
            String memberId,
            @RequestParam(required = true)
            String keyword,
            @RequestParam(required = true)
            int pageNum,
            @RequestParam(required = true)
            int limit
    ){
        return homeService.findKeywordExhibition(memberId, keyword, pageNum, limit);
    }

    @GetMapping("/ranking")
    public List<PopularExhibitionResponse> popularExhibitionList(

    ){
        return homeService.findPopularExhibition();
    }

}
