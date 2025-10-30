package com.example.tikitaka.domain.exhibition.dto.request;

import com.example.tikitaka.domain.club.dto.ClubCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionImageCreate;
import lombok.Getter;

import java.util.List;

@Getter
public class ExhibitionPostRequest {

    private ExhibitionCreate exhibition;
    private ClubCreate club;
    private List<ExhibitionImageCreate> images;

}
