package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.global.util.formatting.PeriodFormatting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecentExhibition {
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;
    private PeriodFormatting duration;
    private String place;

    private boolean isScrap;

    private boolean isOpen; // 현재 진행 중인지 여부



    public static RecentExhibition from(Exhibition exhibition, boolean isScrap) {
        return RecentExhibition.builder()
                .exhibitionId(exhibition.getExhibitionId())
                .exhibitionName(exhibition.getExhibitionName())
                .posterUrl(exhibition.getPosterUrl())
                .duration(new PeriodFormatting(exhibition.getStartDate(), exhibition.getEndDate()))
                .place(exhibition.getPlace())
                .isScrap(isScrap)
                .isOpen(!exhibition.isEnded())
                .build();
    }
}
