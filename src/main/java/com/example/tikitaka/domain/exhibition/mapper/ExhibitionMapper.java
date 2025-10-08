package com.example.tikitaka.domain.exhibition.mapper;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionDetailResponse;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionImageCreate;
import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.entity.ExhibitionImage;
import com.example.tikitaka.domain.exhibition.entity.Status;
import com.example.tikitaka.global.util.formatting.DateFormatting;
import com.example.tikitaka.global.util.formatting.PriceFormatting;
import com.example.tikitaka.global.util.formatting.TimeFormatting;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ExhibitionMapper {

    //
    public Exhibition toExhibition(ExhibitionCreate req, Club club) {
        String code = UUID.randomUUID().toString().substring(0, 8);

        return Exhibition.builder()
                .exhibitionName(req.getExhibitionName())
                .posterUrl(req.getPosterUrl())
                .place(req.getPlace())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .dateException(req.getDateException())
                .price(req.getPrice())
                .link(req.getLink())
                .content(req.getContent())
                .code(code)
                .scrapCount(0)
                .reviewCount(0)
                .cheeringCount(0)
                .questionCount(0)
                .viewCount(0)
                .club(club)
                .category(Category.valueOf(req.getCategory()))
                .status(Status.ACTIVE)
                .isDeleted(false)
                .build();
    }

    public List<ExhibitionImage> toExhibitionImages(Exhibition exhibition, List<ExhibitionImageCreate> images) {
        return images.stream().map(
                image -> ExhibitionImage.builder()
                        .exhibition(exhibition)
                        .imageUrl(image.getUrl())
                        .sequence(image.getSequence())
                        .build()
        ).toList();
    }

    public ExhibitionDetailResponse toDetailResponse(Exhibition exhibition, List<String> images) {
        return ExhibitionDetailResponse.builder()
                .exhibitionId(exhibition.getExhibitionId())
                .exhibitionName(exhibition.getExhibitionName())
                .posterUrl(exhibition.getPosterUrl())
                .place(exhibition.getPlace())
                .price(new PriceFormatting(exhibition.getPrice()))
                .clubName(exhibition.getClub().getName())
                .period(new DateFormatting(exhibition.getStartDate(), exhibition.getEndDate()))
                .duration(new TimeFormatting(exhibition.getStartTime(), exhibition.getEndTime()))
                .dateException(exhibition.getDateException())
                .content(exhibition.getContent())
                .images(images)
                .link(exhibition.getLink())
                .scrapCount(exhibition.getScrapCount())
                .reviewCount(exhibition.getReviewCount())
                .cheeringCount(exhibition.getCheeringCount())
                .questionCount(exhibition.getQuestionCount())
                .userRole("") // 추후 추가 예정
                .build();
    }



}
