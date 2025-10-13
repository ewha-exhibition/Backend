package com.example.tikitaka.domain.post.dto.response;

import com.example.tikitaka.domain.post.dto.ExhibitionReview;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExhibitionReviewListResponse {
    private List<ExhibitionReview> reviews;
    private PageInfo pageInfo;

    public static ExhibitionReviewListResponse of(List<ExhibitionReview> reviews, PageInfo pageInfo) {
        return ExhibitionReviewListResponse.builder()
                .reviews(reviews)
                .pageInfo(pageInfo)
                .build();
    }
}
