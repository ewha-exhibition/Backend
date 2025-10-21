package com.example.tikitaka.domain.scrap.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ScrapListItemDto {
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;     // S3 presigned 변환이 필요하면 여기서 가공
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isViewed;
}
