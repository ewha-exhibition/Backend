package com.example.tikitaka.domain.exhibition.mapper;

import com.example.tikitaka.domain.club.entity.Club;
import com.example.tikitaka.domain.exhibition.dto.ExhibitionCreate;
import com.example.tikitaka.domain.exhibition.entity.Category;
import com.example.tikitaka.domain.exhibition.entity.Exhibition;
import com.example.tikitaka.domain.exhibition.entity.Status;
import com.example.tikitaka.domain.exhibition.vo.ExhibitionVo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExhibitionMapper {
    public Exhibition toExhibition(ExhibitionCreate req, String clubId) {
        String code = UUID.randomUUID().toString().substring(0, 8);
        String id = UUID.randomUUID().toString();

        return Exhibition.builder()
                .exhibitionId(id)
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
                .clubId(clubId)
                .category(Category.valueOf(req.getCategory()))
                .status(Status.ACTIVE)
                .build();
    }
}
