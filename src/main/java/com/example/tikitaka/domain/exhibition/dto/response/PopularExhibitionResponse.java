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
    private boolean isScrap;

    public static PopularExhibitionResponse from(Exhibition exhibition, boolean isScrap) {
        return PopularExhibitionResponse.builder()
                .exhibitionId(exhibition.getExhibitionId())
                .exhibitionName(exhibition.getExhibitionName())
                .posterUrl(exhibition.getPosterUrl())
                .isScrap(isScrap)
                .build();
    }
}
