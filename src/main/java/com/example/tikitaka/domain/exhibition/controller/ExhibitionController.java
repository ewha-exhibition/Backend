package com.example.tikitaka.domain.exhibition.controller;

import com.example.tikitaka.domain.exhibition.dto.CreateExhibitionRequest;
import com.example.tikitaka.domain.exhibition.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exhibition")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @PostMapping
    public void exhibitionAdd(
            @RequestBody CreateExhibitionRequest request
    ) {
        exhibitionService.addExhibition(request);
    }
}
