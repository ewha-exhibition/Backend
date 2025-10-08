package com.example.tikitaka.domain.exhibition.controller;

import com.example.tikitaka.domain.exhibition.dto.response.RecentExhibitionListResponse;
import com.example.tikitaka.domain.exhibition.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/latest")
    public RecentExhibitionListResponse recentExhibitionList(
            @RequestParam(required = false)
            String category,
            @RequestParam(required = true)
            int pageNum,
            @RequestParam(required = true)
            int limit
    ){
        return homeService.findRecentExhibition(category, pageNum, limit);
    }

}
