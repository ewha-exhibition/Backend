package com.example.tikitaka.domain.post.dto.response;

import com.example.tikitaka.domain.post.dto.ExhibitionPost;
import com.example.tikitaka.global.dto.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExhibitionPostListResponse {
    private List<ExhibitionPost> comments;
    private PageInfo pageInfo;

    public static ExhibitionPostListResponse of(List<ExhibitionPost> comments, PageInfo pageInfo) {
        return ExhibitionPostListResponse.builder()
                .comments(comments)
                .pageInfo(pageInfo)
                .build();
    }
}
