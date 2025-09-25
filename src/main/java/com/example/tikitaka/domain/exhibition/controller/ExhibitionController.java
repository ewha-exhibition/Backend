package com.example.tikitaka.domain.exhibition.controller;

import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionDetailResponse;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionPostRequest;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @PostMapping
    public void exhibitionAdd(
            @RequestBody ExhibitionPostRequest request
    ) {
        exhibitionService.addExhibition(request);
    }

    @GetMapping("/{exhibitionIdx}")
    public List<Exhibition> exhibitionDetail(
            // 로그인 기능 추가 후 유저 정보 받기
            @PathVariable
            Long exhibitionIdx
    ){
        return exhibitionService.findExhibtion(exhibitionIdx);
    }
}
