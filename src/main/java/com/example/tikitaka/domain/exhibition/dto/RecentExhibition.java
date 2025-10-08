package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecentExhibition {
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;
    private DateFormatting duration;
    private String place;

    // 추후 스크랩 여부 함께 반환
    // private boolean isScrap;

    private boolean isOpen; // 현재 진행 중인지 여부



    public static RecentExhibition from(Exhibition exhibition) {
        return RecentExhibition.builder()
                .exhibitionId(exhibition.getExhibitionId())
                .exhibitionName(exhibition.getExhibitionName())
                .posterUrl(exhibition.getPosterUrl())
                .duration(new DateFormatting(exhibition.getStartDate(), exhibition.getEndDate()))
                .place(exhibition.getPlace())
                .isOpen(!exhibition.isEnded())
                .build();
    }
}
