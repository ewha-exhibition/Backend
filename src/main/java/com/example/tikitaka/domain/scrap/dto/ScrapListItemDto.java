package com.example.tikitaka.domain.scrap.dto;

import com.example.tikitaka.domain.scrap.entity.Scrap;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ScrapListItemDto {
    private Long scrapId;
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;     // S3 presigned 변환이 필요하면 여기서 가공
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private boolean isViewed;

    public static ScrapListItemDto from(Scrap s, boolean isViewed) {
        var e = s.getExhibition();
        return ScrapListItemDto.builder()
                .scrapId(s.getScrapId())
                .exhibitionId(e.getExhibitionId())
                .exhibitionName(e.getExhibitionName())
                .posterUrl(e.getPosterUrl())
                .place(e.getPlace())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .createdAt(s.getCreatedAt())
                .isViewed(isViewed)
                .build();
    }
}
