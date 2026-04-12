package com.example.tikitaka.domain.scrap.dto;

import com.example.tikitaka.domain.scrap.entity.Scrap;
import com.example.tikitaka.domain.scrap.entity.View;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewsDto {
    private Long exhibitionId;
    private String exhibitionName;
    private String posterUrl;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isReviewed;

    public static ViewsDto from(View v, boolean isReviewed) {
        var e = v.getExhibition();
        return ViewsDto.builder()
                .exhibitionId(e.getExhibitionId())
                .exhibitionName(e.getExhibitionName())
                .posterUrl(e.getPosterUrl())
                .place(e.getPlace())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .isReviewed(isReviewed)
                .build();
    }
}
