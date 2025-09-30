package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.domain.club.dto.ClubCreate;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static lombok.Builder.*;

@Getter
public class ExhibitionPostRequest {
    private String memberId; // 인증 인가 구현 후 삭제 예정

    private ExhibitionCreate exhibition;

    private ClubCreate club;

    private List<ExhibitionImageCreate> images = List.of();

}
