package com.example.tikitaka.domain.exhibition.dto;

import com.example.tikitaka.global.util.formatting.PeriodFormatting;
import com.example.tikitaka.global.util.formatting.PriceFormatting;
import com.example.tikitaka.global.util.formatting.TimeFormatting;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ExhibitionCreate {
    private String exhibitionName;
    private String posterUrl;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dateException;
    private int price;
    private String link;
    private String content;
    private String category;

    @Getter
    @Builder
    public static class ExhibitionDetailResponse {
        private Long exhibitionId;
        private String exhibitionName;
        private String posterUrl;
        private String place;
        private PriceFormatting price;
        private String clubName;
        private PeriodFormatting period;
        private TimeFormatting duration;
        private String dateException;
        private String content;
        private List<String> images;
        private String link;
        private int scrapCount;
        private int reviewCount;
        private int cheeringCount;
        private int questionCount;
        private String userRole;

    }
}
