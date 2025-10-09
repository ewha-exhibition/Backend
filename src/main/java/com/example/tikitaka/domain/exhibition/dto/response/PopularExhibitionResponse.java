package com.example.tikitaka.domain.exhibition.dto.response;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PopularExhibitionResponse {
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;

    public static PopularExhibitionResponse from(Exhibition exhibition) {
        return PopularExhibitionResponse.builder()
                .exhibitionId(exhibition.getExhibitionId())
                .exhibitionName(exhibition.getExhibitionName())
                .posterUrl(exhibition.getPosterUrl())
                .build();
    }
}
