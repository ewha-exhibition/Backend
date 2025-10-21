package com.example.tikitaka.domain.scrap.dto.request;

import com.example.tikitaka.domain.club.dto.ClubCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionImageCreate;

import java.util.List;

// scrap 한 리스트 요청 dto
public class ScrapListRequestDto {

    private Long memberId;

    private ExhibitionCreate exhibition;

    private ClubCreate club;

    private List<ExhibitionImageCreate> images;
}
