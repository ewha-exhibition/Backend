package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.entity.ExhibitionImage;
import com.example.tikitaka.global.util.formatting.PeriodFormatting;
import lombok.Builder;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Builder
public class ExhibitionImageGet {
    private Long id;
    private String imageUrl;

    public static ExhibitionImageGet from(ExhibitionImage exhibitionImage) {
        return ExhibitionImageGet.builder()
                .id(exhibitionImage.getExhibitionImageId())
                .imageUrl(exhibitionImage.getImageUrl())
                .build();
    }
}
