package com.example.tikitaka.domain.exhibition.dto.response;

import com.example.tikitaka.global.util.formatting.PeriodFormatting;
import com.example.tikitaka.global.util.formatting.PriceFormatting;
import com.example.tikitaka.global.util.formatting.TimeFormatting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExhibitionDetailResponse {
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
    private List<String> images; // TODO: image id도 같이 반환하도록 수정
    private String link;
    private int scrapCount;
    private int reviewCount;
    private int cheerCount;
    private int questionCount;
    private boolean isHost;
    private boolean isScrap;

}
