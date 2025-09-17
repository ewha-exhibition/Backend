package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.domain.club.dto.ClubCreate;
import com.example.tikitaka.domain.exhibition.vo.ExhibitionImageVo;
import com.example.tikitaka.domain.exhibition.vo.ExhibitionVo;
import lombok.Getter;

import java.util.List;

@Getter
public class ExhibitionPostRequest {
    private String memberId; // 인증 인가 구현 후 삭제 예정

    private ExhibitionCreate exhibition;

    private ClubCreate club;

    private List<ExhibitionImageVo> imagesVo;

}
